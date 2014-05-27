package org.arcanum.fe.abe.gvw13.engines;

import org.arcanum.Element;
import org.arcanum.ElementCipher;
import org.arcanum.circuit.BooleanCircuit;
import org.arcanum.circuit.BooleanGate;
import org.arcanum.fe.abe.gvw13.params.GVW13EncryptionParameters;
import org.arcanum.fe.abe.gvw13.params.GVW13PublicKeyParameters;
import org.arcanum.fe.abe.gvw13.params.GVW13SecretKeyParameters;
import org.arcanum.kem.AbstractKeyEncapsulationMechanism;
import org.arcanum.tor.gvw13.params.TORGVW13PublicKeyParameters;
import org.arcanum.util.io.ElementStreamReader;
import org.arcanum.util.io.PairingStreamWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GVW13KEMEngine extends AbstractKeyEncapsulationMechanism {

    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof GVW13EncryptionParameters))
                throw new IllegalArgumentException("GVW13EncryptionParameters are required for encryption.");

            GVW13EncryptionParameters encKey = (GVW13EncryptionParameters) key;
            GVW13PublicKeyParameters publicKey = encKey.getPublicKey();

            this.keyBytes = publicKey.getParameters().getKeyLengthInBytes();
            // TODO: adjust outBytes
            this.outBytes = (encKey.getAssignment().length() + 1) * ((TORGVW13PublicKeyParameters)publicKey.getCipherParametersOut()).getOwfOutputField().getLengthInBytes();
        } else {
            if (!(key instanceof GVW13SecretKeyParameters))
                throw new IllegalArgumentException("GVW13SecretKeyParameters are required for decryption.");
        }
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof GVW13SecretKeyParameters) {
            // Decrypt
            GVW13SecretKeyParameters sk = (GVW13SecretKeyParameters) key;

            // Load the ciphertext
            ElementStreamReader reader = new ElementStreamReader(in, inOff);

            String assignment = reader.readString();

            // Evaluate the circuit against the ciphertext
            BooleanCircuit circuit = sk.getCircuit();

            Element e = reader.readElement(sk.getCiphertextElementField());

            // evaluate the circuit
            Map<Integer, Element> evaluations = new HashMap<Integer, Element>();
            for (BooleanGate gate : sk.getCircuit()) {
                int index = gate.getIndex();

                switch (gate.getType()) {
                    case INPUT:
                        gate.set(assignment.charAt(index) == '1');

                        // Read input
                        Element element = reader.readElement(sk.getCiphertextElementField());
                        evaluations.put(index, element);

                        break;
                    case OR:
                    case AND:
                        gate.evaluate();

                        // Init TOR for recoding
                        ElementCipher tor = sk.getParameters().getTor();
                        tor.init(sk.getCipherParametersAt(index, gate.getInputAt(0).get() ? 1 : 0, gate.getInputAt(1).get() ? 1 : 0));

                        evaluations.put(
                                index,
                                // recode
                                tor.processElements(
                                        evaluations.get(gate.getInputIndexAt(0)),
                                        evaluations.get(gate.getInputIndexAt(1))
                                )
                        );
                        break;
                }
            }

            Element key = evaluations.get(circuit.getOutputGate().getIndex());

            ElementCipher tor = sk.getParameters().getTor();
            tor.init(sk.getCipherParametersOut());
            tor.init(key);

            return tor.processElementsToBytes(e);
        } else {
            GVW13EncryptionParameters encKey = (GVW13EncryptionParameters) key;
            GVW13PublicKeyParameters publicKey = encKey.getPublicKey();

            ElementCipher tor = publicKey.getParameters().getTor();
            String assignment = encKey.getAssignment();

            PairingStreamWriter writer = new PairingStreamWriter(getOutputBlockSize());
            try {
                Element s = publicKey.getParameters().getRandomnessField().newRandomElement();

                // choose random bit string
                byte[] bytes = new byte[publicKey.getParameters().getKeyLengthInBytes()];
                publicKey.getParameters().getRandom().nextBytes(bytes);
                writer.write(bytes);

                // encrypt bytes
                tor.init(publicKey.getCipherParametersOut());
                Element key = tor.processElements(s);

                tor.init(key);
                Element e = tor.processBytes(bytes);

                writer.write(assignment);
                writer.write(e);
                for (int i = 0, n = assignment.length(); i < n; i++) {
                    // init for encoding
                    tor.init(publicKey.getCipherParametersAt(i, assignment.charAt(i) == '1'));

                    // encode
                    e = tor.processElements(s);
                    writer.write(e);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return writer.toBytes();
        }
    }


}