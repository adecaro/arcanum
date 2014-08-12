package org.arcanum.fe.abe.gghsw13.engines;

import org.arcanum.Element;
import org.arcanum.common.fe.params.EncryptionParameters;
import org.arcanum.common.io.ElementStreamWriter;
import org.arcanum.common.io.PairingStreamReader;
import org.arcanum.common.kem.PairingKeyEncapsulationMechanism;
import org.arcanum.fe.abe.gghsw13.params.GGHSW13Parameters;
import org.arcanum.fe.abe.gghsw13.params.GGHSW13PublicKeyParameters;
import org.arcanum.fe.abe.gghsw13.params.GGHSW13SecretKeyParameters;
import org.arcanum.program.Assignment;
import org.arcanum.program.assignment.BooleanAssignment;
import org.arcanum.program.circuit.BooleanCircuit;
import org.arcanum.program.circuit.BooleanGate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GGHSW13KEMEngine extends PairingKeyEncapsulationMechanism {

    public void initialize() {
        GGHSW13Parameters parameters;

        if (forEncryption) {
            if (!(key instanceof EncryptionParameters))
                throw new IllegalArgumentException("GGHSW13EncryptionParameters are required for encryption.");

            parameters = ((EncryptionParameters<GGHSW13PublicKeyParameters, Boolean>) key).getMpk().getParameters();
        } else {
            if (!(key instanceof GGHSW13SecretKeyParameters))
                throw new IllegalArgumentException("GGHSW13SecretKeyParameters are required for decryption.");

            parameters = ((GGHSW13SecretKeyParameters) key).getParameters();
        }

        this.pairing = parameters.getPairing();
        this.keyBytes = pairing.getFieldAt(pairing.getDegree()).getCanonicalRepresentationLengthInBytes();
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof GGHSW13SecretKeyParameters) {
            // Decrypt
            GGHSW13SecretKeyParameters sk = (GGHSW13SecretKeyParameters) key;

            // Load the ciphertext
            PairingStreamReader reader = new PairingStreamReader(pairing, in, inOff);

            Assignment<Boolean> assignment = new BooleanAssignment(reader.readString());
            Element gs = reader.readG1Element();

            // compute hamming with
            Element[] cs = new Element[sk.getParameters().getN()];
            for (int i = 0; i < assignment.getLength(); i++)
                if (assignment.getAt(i))
                    cs[i] = reader.readG1Element();

            // Evaluate the circuit against the ciphertext
            BooleanCircuit circuit = sk.getCircuit();
            Element root = pairing.pairing(sk.getKeyElementsAt(-1)[0], gs);

            // evaluate the circuit
            Map<Integer, Element> evaluations = new HashMap<Integer, Element>();
            for (BooleanGate gate : sk.getCircuit()) {
                int index = gate.getIndex();

                switch (gate.getType()) {
                    case INPUT:
                        gate.set(assignment.getAt(index));

                        if (gate.get()) {
                            Element[] keys = sk.getKeyElementsAt(index);
                            Element t1 = pairing.pairing(keys[0], gs);
                            Element t2 = pairing.pairing(keys[1], cs[index]);

                            evaluations.put(index, t1.mul(t2));
                        }

                        break;

                    case OR:
                        gate.evaluate();

                        if (gate.getInputAt(0).get()) {
                            Element[] keys = sk.getKeyElementsAt(index);
                            Element t1 = pairing.pairing(
                                    evaluations.get(gate.getInputAt(0).getIndex()),
                                    keys[0]
                            );

                            Element t2 = pairing.pairing(
                                    keys[2],
                                    gs
                            );

                            evaluations.put(index, t1.mul(t2));
                        } else if (gate.getInputAt(1).get()) {
                            Element[] keys = sk.getKeyElementsAt(index);
                            Element t1 = pairing.pairing(
                                    evaluations.get(gate.getInputAt(1).getIndex()),
                                    keys[1]
                            );

                            Element t2 = pairing.pairing(
                                    keys[3],
                                    gs
                            );

                            evaluations.put(index, t1.mul(t2));
                        }

                        break;

                    case AND:
                        gate.evaluate();

                        if (gate.get()) {
                            Element[] keys = sk.getKeyElementsAt(index);
                            Element t1 = pairing.pairing(
                                    evaluations.get(gate.getInputAt(0).getIndex()),
                                    keys[0]
                            );

                            Element t2 = pairing.pairing(
                                    evaluations.get(gate.getInputAt(1).getIndex()),
                                    keys[1]
                            );

                            Element t3 = pairing.pairing(
                                    keys[2],
                                    gs
                            );

                            evaluations.put(index, t1.mul(t2).mul(t3));
                        }

                        break;
                }
            }

            if (circuit.getOutputGate().get()) {
                Element result = root.mul(evaluations.get(circuit.getOutputGate().getIndex()));

                return result.toCanonicalRepresentation();
            } else
                return new byte[]{-1};
        } else {
            // Encrypt the massage under the specified attributes
            EncryptionParameters<GGHSW13PublicKeyParameters, Boolean> encKey = (EncryptionParameters<GGHSW13PublicKeyParameters, Boolean>) key;
            GGHSW13PublicKeyParameters publicKey = encKey.getMpk();
            Assignment<Boolean> assignment = encKey.getAssignment();

            ElementStreamWriter writer = new ElementStreamWriter(getOutputBlockSize());
            try {
                // Sample the randomness
                Element s = pairing.getZr().newRandomElement().getImmutable();

                Element mask = publicKey.getH().powZn(s);
                writer.write(mask.toCanonicalRepresentation());

                writer.write(assignment.toString());
                writer.write(pairing.getFieldAt(1).newElement().powZn(s));
                int n = publicKey.getParameters().getN();
                for (int i = 0; i < n; i++) {
                    if (assignment.getAt(i))
                        writer.write(publicKey.getHAt(i).powZn(s));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return writer.toBytes();
        }
    }


}