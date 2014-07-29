package org.arcanum.fe.abe.bns14.engines;

import org.arcanum.Element;
import org.arcanum.Matrix;
import org.arcanum.circuit.ArithmeticCircuit;
import org.arcanum.circuit.ArithmeticGate;
import org.arcanum.fe.abe.bns14.params.BNS14EncryptionParameters;
import org.arcanum.fe.abe.bns14.params.BNS14PublicKeyParameters;
import org.arcanum.fe.abe.bns14.params.BNS14SecretKeyParameters;
import org.arcanum.field.util.ElementUtils;
import org.arcanum.kem.AbstractKeyEncapsulationMechanism;
import org.arcanum.trapdoor.mp12.engines.MP12HLP2ErrorTolerantOneTimePad;
import org.arcanum.trapdoor.mp12.engines.MP12PLP2MatrixSolver;
import org.arcanum.util.io.ElementStreamReader;
import org.arcanum.util.io.ElementStreamWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BNS14KEMEngine extends AbstractKeyEncapsulationMechanism {

    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof BNS14EncryptionParameters))
                throw new IllegalArgumentException("BNS14EncryptionParameters are required for encryption.");

            BNS14EncryptionParameters encKey = (BNS14EncryptionParameters) key;
            BNS14PublicKeyParameters publicKey = encKey.getPublicKey();

            this.keyBytes = publicKey.getKeyLengthInBytes();
            // TODO: adjust outBytes
//            publicKey.getRandomnessField().getLengthInBytes()
//            publicKey.getRandomnessField().getLengthInBytes()
//            publicKey.getPrimitiveLatticePk().getZq().getLengthInBytes() * ell
//            publicKey.getRandomnessField().getLengthInBytes() * ell

//            this.outBytes = (encKey.getAssignment().length() + 1) * ((TORBNS14PublicKeyParameters)publicKey.getCipherParametersOut()).getOwfOutputField().getLengthInBytes();
        } else {
            if (!(key instanceof BNS14SecretKeyParameters))
                throw new IllegalArgumentException("BNS14DecryptionParameters are required for decryption.");
        }
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof BNS14SecretKeyParameters) {
            // Decrypt
            BNS14SecretKeyParameters sk = (BNS14SecretKeyParameters) key;
            BNS14PublicKeyParameters pk = sk.getPublicKey();

            // Load the ciphertext
            ElementStreamReader reader = new ElementStreamReader(in, inOff);

            // Evaluate the circuit against the ciphertext
            ArithmeticCircuit circuit = sk.getCircuit();

            // Read cin, cout;
            Element cin = reader.readElement(pk.getRandomnessField());
            Element cout = reader.readElement(pk.getRandomnessField());

            // evaluate the circuit
            Map<Integer, Element> evaluations = new HashMap<Integer, Element>();
            Map<Integer, Element> keys = new HashMap<Integer, Element>();

            MP12PLP2MatrixSolver solver = new MP12PLP2MatrixSolver();
            solver.init(pk.getPrimitiveLatticePk());

            for (ArithmeticGate gate : sk.getCircuit()) {
                int index = gate.getIndex();

                switch (gate.getType()) {
                    case INPUT:
                        gate.set(reader.readElement(pk.getLatticePk().getZq()));
                        evaluations.put(index, reader.readElement(pk.getRandomnessField()));
                        keys.put(index, pk.getBAt(index));

                        break;
                    case OR:
                        // addition
                        gate.evaluate();

                        Element cGate = evaluations.get(gate.getInputIndexAt(0)).getField().newZeroElement();
                        Element B = pk.getBAt(0).getField().newZeroElement();

                        for (int i = 0, k = gate.getNumInputs(); i < k; i++) {
                            Matrix R = (Matrix) solver.processElements(
                                    pk.getPrimitiveLatticePk().getG().duplicate().mulZn(gate.getAlphaAt(i))
                            );

                            B.add(keys.get(gate.getInputIndexAt(i)).mul(R));
                            cGate.add(R.mulFromTranspose(evaluations.get(gate.getInputIndexAt(i))));
                        }

                        evaluations.put(index, cGate);
                        keys.put(index, B);
                        break;

                    case AND:
                        // multiplication
                        gate.evaluate();

                        cGate = evaluations.get(gate.getInputIndexAt(0)).getField().newZeroElement();

                        // Compute R_0 = SolveR(G, T_G, \alpha G)
                        Element R = solver.processElements(
                                (gate.getAlphaAt(0).isOne()) ?
                                        pk.getPrimitiveLatticePk().getG() :
                                        pk.getPrimitiveLatticePk().getG().duplicate().mulZn(gate.getAlphaAt(0))
                        );
                        for (int j = 1, k = gate.getNumInputs(); j < k; j++) {

                            Element x = gate.get().getField().newOneElement();
                            for (int i = j; i < k; i++) {
                                x.mul(gate.getInputAt(i).get());
                            }

                            // R_j = SolveR(G, T_G, - B_{j-1} R_{j-1})
                            Element symdrome = keys.get(gate.getInputIndexAt(j - 1)).mul(R).negate();
                            Element Rnext = solver.processElements(symdrome);

                            if (!x.isZero()) {
                                if (x.isOne())
                                    cGate.add(((Matrix) R.duplicate().mul(x)).mulFromTranspose(evaluations.get(gate.getInputIndexAt(j))));
                                else
                                    cGate.add(((Matrix) R).mulFromTranspose(evaluations.get(gate.getInputIndexAt(j))));
                            }

                            R = Rnext;
                        }

                        cGate.add(((Matrix) R).mulFromTranspose(evaluations.get(gate.getInputIndexAt(gate.getNumInputs() - 1))));

                        evaluations.put(index, cGate);
                        keys.put(index, keys.get(gate.getInputIndexAt(gate.getNumInputs() - 1)).mul(R));
                        break;
                }
            }
            Element cf = evaluations.get(circuit.getOutputGate().getIndex());

            Element cfPrime = ElementUtils.union(cin, cf);

            MP12HLP2ErrorTolerantOneTimePad otp = new MP12HLP2ErrorTolerantOneTimePad();
            Element key = sk.getSkC().mul(cfPrime);

            otp.init(key);

            return otp.processElementsToBytes(cout);
        } else {
            // Encrypt
            BNS14EncryptionParameters encKey = (BNS14EncryptionParameters) key;
            BNS14PublicKeyParameters publicKey = encKey.getPublicKey();

            ElementStreamWriter writer = new ElementStreamWriter(getOutputBlockSize());
            try {
                // 1. Choose the key
                byte[] bytes = new byte[publicKey.getKeyLengthInBytes()];
                publicKey.getParameters().getRandom().nextBytes(bytes);
                writer.write(bytes);

                // 2. Encrypt the key
                Element s = publicKey.getSecretField().newRandomElement();
                Element e0 = publicKey.sampleError();
                Element e1 = publicKey.sampleError();

                // cin
                writer.write(publicKey.getLatticePk().getA().mul(s).add(e0));

                // cout
                MP12HLP2ErrorTolerantOneTimePad otp = new MP12HLP2ErrorTolerantOneTimePad();
                otp.init(publicKey.getD().mul(s).add(e1));
                writer.write(otp.processBytes(bytes));

                // c_i's
                for (int i = 0, ell = publicKey.getParameters().getEll(); i < ell; i++) {
                    Element Si = publicKey.sampleUniformOneMinusOneMarix();
                    Element ei = Si.mul(e0);

                    writer.write(encKey.getXAt(i));
                    writer.write(
                            publicKey.getBAt(i).duplicate()
                                    .add(publicKey.getLatticePk().getG().duplicate().mulZn(encKey.getXAt(i)))
                                    .mul(s)
                                    .add(ei)
                    );
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return writer.toBytes();
        }
    }


}