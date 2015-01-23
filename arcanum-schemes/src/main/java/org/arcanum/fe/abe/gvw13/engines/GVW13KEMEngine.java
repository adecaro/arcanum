package org.arcanum.fe.abe.gvw13.engines;

import org.arcanum.Element;
import org.arcanum.common.cipher.engine.ElementCipher;
import org.arcanum.common.cipher.params.ElementCipherParameters;
import org.arcanum.common.fe.params.EncryptionParameters;
import org.arcanum.common.io.ElementStreamReader;
import org.arcanum.common.io.ElementStreamWriter;
import org.arcanum.common.kem.AbstractKeyEncapsulationMechanism;
import org.arcanum.fe.abe.gvw13.params.GVW13PublicKeyParameters;
import org.arcanum.fe.abe.gvw13.params.GVW13SecretKeyParameters;
import org.arcanum.program.Assignment;
import org.arcanum.program.circuit.BooleanCircuit;
import org.arcanum.program.circuit.BooleanGate;
import org.arcanum.tor.gvw13.params.TORGVW13PublicKeyParameters;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GVW13KEMEngine extends AbstractKeyEncapsulationMechanism {

    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof EncryptionParameters))
                throw new IllegalArgumentException("GVW13EncryptionParameters are required for encryption.");

            EncryptionParameters<GVW13PublicKeyParameters, Boolean> encKey = (EncryptionParameters<GVW13PublicKeyParameters, Boolean>) key;
            GVW13PublicKeyParameters publicKey = encKey.getMpk();

            this.keyBytes = publicKey.getParameters().getKeyLengthInBytes();
            // TODO: adjust outBytes
            this.outBytes = (encKey.getAssignment().getLength() + 1) * ((TORGVW13PublicKeyParameters)publicKey.getCipherParametersOut()).getOwfOutputField().getLengthInBytes();
        } else {
            if (!(key instanceof GVW13SecretKeyParameters))
                throw new IllegalArgumentException("GVW13SecretKeyParameters are required for decryption.");
        }
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof GVW13SecretKeyParameters) {
            // Decrypt
            GVW13SecretKeyParameters sk = (GVW13SecretKeyParameters) key;
            ElementCipher<Element, Element, ElementCipherParameters> tor = sk.getParameters().getTor();

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
                        tor.init(
                                sk.getCipherParametersAt(
                                        index,
                                        gate.getInputAt(0).get() ? 1 : 0,
                                        gate.getInputAt(1).get() ? 1 : 0
                                )
                        );

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

            return tor.init(
                    sk.getCipherParametersOut(),
                    evaluations.get(circuit.getOutputGate().getIndex())
            ).processElementsToBytes(e);
        } else {
            // Encrypt
            EncryptionParameters<GVW13PublicKeyParameters, Boolean> encKey = (EncryptionParameters<GVW13PublicKeyParameters, Boolean>) key;
            GVW13PublicKeyParameters publicKey = encKey.getMpk();

            ElementCipher<Element, Element, ElementCipherParameters> tor = publicKey.getParameters().getTor();
            Assignment<Boolean> assignment = encKey.getAssignment();

            ElementStreamWriter writer = new ElementStreamWriter(getOutputBlockSize());
            try {
                Element s = publicKey.getParameters().getRandomnessField().newRandomElement();

                // choose random bit string
                byte[] bytes = new byte[publicKey.getParameters().getKeyLengthInBytes()];
                publicKey.getParameters().getRandom().nextBytes(bytes);
                writer.write(bytes);

                // encrypt bytes
                Element key = tor.init(publicKey.getCipherParametersOut()).processElements(s);
                Element e = tor.init(key).processBytes(bytes);

                // write out
                writer.write(assignment.toString());
                writer.write(e);
                for (int i = 0, n = assignment.getLength(); i < n; i++)
                    writer.write(tor.init(publicKey.getCipherParametersAt(i, assignment.getAt(i))).processElements(s));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return writer.toBytes();
        }
    }


}