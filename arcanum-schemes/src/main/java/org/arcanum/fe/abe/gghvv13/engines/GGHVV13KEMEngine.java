package org.arcanum.fe.abe.gghvv13.engines;

import org.arcanum.Element;
import org.arcanum.common.fe.params.EncryptionParameters;
import org.arcanum.common.io.ElementStreamWriter;
import org.arcanum.common.io.PairingStreamReader;
import org.arcanum.common.kem.PairingKeyEncapsulationMechanism;
import org.arcanum.fe.abe.gghvv13.params.GGHVV13Parameters;
import org.arcanum.fe.abe.gghvv13.params.GGHVV13PublicKeyParameters;
import org.arcanum.fe.abe.gghvv13.params.GGHVV13SecretKeyParameters;
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
public class GGHVV13KEMEngine extends PairingKeyEncapsulationMechanism {

    public void initialize() {
        GGHVV13Parameters parameters;

        if (forEncryption) {
            if (!(key instanceof EncryptionParameters))
                throw new IllegalArgumentException("GGHSW13EncryptionParameters are required for encryption.");

            parameters = ((EncryptionParameters<GGHVV13PublicKeyParameters, Boolean>) key).getMpk().getParameters();
        } else {
            if (!(key instanceof GGHVV13SecretKeyParameters))
                throw new IllegalArgumentException("GGHSW13SecretKeyParameters are required for decryption.");

            parameters = ((GGHVV13SecretKeyParameters) key).getParameters();
        }
        this.pairing = parameters.getPairing();
        this.keyBytes = pairing.getFieldAt(pairing.getDegree()).getCanonicalRepresentationLengthInBytes();
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof GGHVV13SecretKeyParameters) {
            // Decrypt
            GGHVV13SecretKeyParameters sk = (GGHVV13SecretKeyParameters) key;

            // Load the ciphertext
            PairingStreamReader reader = new PairingStreamReader(pairing, in, inOff);

            Assignment<Boolean> assignment = new BooleanAssignment(reader.readString());
            Element gs = reader.readG1Element();
            Element gamma1 = reader.readG1Element();

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
                            Element t1 = pairing.getG1().newOneElement();
                            for (int i = 0, n = assignment.getLength(); i < n; i++) {
                                if (assignment.getAt(i))
                                    t1.mul(sk.getMAt(i + 1, index));
                            }

                            Element t2 = sk.getMAt(0, index);

                            evaluations.put(index,
                                    pairing.pairing(gs, t1).mul(pairing.pairing(gamma1, t2))
                            );
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
            EncryptionParameters<GGHVV13PublicKeyParameters, Boolean> encKey = (EncryptionParameters<GGHVV13PublicKeyParameters, Boolean>) key;
            GGHVV13PublicKeyParameters publicKey = encKey.getMpk();
            Assignment<Boolean> assignment = encKey.getAssignment();

            ElementStreamWriter writer = new ElementStreamWriter(getOutputBlockSize());
            try {
                // Sample the randomness
                Element s = pairing.getZr().newRandomElement().getImmutable();

                Element mask = publicKey.getH().powZn(s);
                writer.write(mask.toCanonicalRepresentation());

                writer.write(assignment.toString());
                writer.write(pairing.getFieldAt(1).newElement().powZn(s));

                Element gamma1 = pairing.getFieldAt(1).newZeroElement();
                for (int i = 0, n = assignment.getLength(); i < n; i++) {
                    if (assignment.getAt(i))
                        gamma1.mul(publicKey.getHAt(i));
                }
                gamma1.powZn(s);
                writer.write(gamma1);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return writer.toBytes();
        }
    }


}