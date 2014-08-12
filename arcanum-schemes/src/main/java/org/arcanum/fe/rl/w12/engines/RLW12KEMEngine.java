package org.arcanum.fe.rl.w12.engines;

import org.arcanum.Element;
import org.arcanum.common.fe.params.EncryptionParameters;
import org.arcanum.common.io.ElementStreamWriter;
import org.arcanum.common.io.PairingStreamReader;
import org.arcanum.common.kem.PairingKeyEncapsulationMechanism;
import org.arcanum.fe.rl.w12.params.RLW12Parameters;
import org.arcanum.fe.rl.w12.params.RLW12PublicKeyParameters;
import org.arcanum.fe.rl.w12.params.RLW12SecretKeyParameters;
import org.arcanum.pairing.PairingFactory;
import org.arcanum.pairing.accumulator.PairingAccumulator;
import org.arcanum.pairing.accumulator.PairingAccumulatorFactory;
import org.arcanum.program.Assignment;
import org.arcanum.program.dfa.DFA;

import java.io.IOException;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class RLW12KEMEngine extends PairingKeyEncapsulationMechanism {

    public void initialize() {
        // Check params
        RLW12Parameters keyParameters;

        if (forEncryption) {
            if (!(key instanceof EncryptionParameters))
                throw new IllegalArgumentException("RLW12EncryptionParameters are required for encryption.");

            keyParameters = ((EncryptionParameters<RLW12PublicKeyParameters, Character>) key).getMpk().getParameters();
        } else {
            if (!(key instanceof RLW12SecretKeyParameters))
                throw new IllegalArgumentException("RLW12SecretKeyParameters are required for decryption.");

            keyParameters = ((RLW12SecretKeyParameters) key).getParameters();
        }

        // Init pairing
        this.pairing = PairingFactory.getPairing(keyParameters.getParameters());

        // Init sizes
        this.keyBytes = pairing.getGT().getLengthInBytes();
//        this.outBytes = 2 * pairing.getGT().getLengthInBytes() + N * pairing.getG1().getLengthInBytes();
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof RLW12SecretKeyParameters) {
            // Decrypt
            RLW12SecretKeyParameters sk = (RLW12SecretKeyParameters) key;

            PairingStreamReader reader = new PairingStreamReader(pairing, in, inOff);
            String w = reader.readString();
            Element[] wEnc = reader.readG1Elements(((inLen - pairing.getGT().getLengthInBytes()) / pairing.getG1().getLengthInBytes()));
            Element cm = reader.readGTElement();

            // Run the decryption...
            // Init
            PairingAccumulator accumulator = PairingAccumulatorFactory.getInstance().getPairingMultiplier(pairing);

            int index = 0;
            accumulator.addPairing(wEnc[index++], sk.getkStart(0))
                    .addPairingInverse(wEnc[index++], sk.getkStart(1));

            // Run
            int currentState = sk.getDfa().getInitialState(); // Initial state

            for (int i = 0; i < w.length(); i++) {
                DFA.Transition transition = sk.getDfa().getTransition(currentState, w.charAt(i));

                accumulator.addPairing(wEnc[index - 2], sk.getkTransition(transition, 0))
                        .addPairing(wEnc[index++], sk.getkTransition(transition, 2))
                        .addPairingInverse(wEnc[index++], sk.getkTransition(transition, 1));

                currentState = transition.getTo();
            }

            // Finalize
            if (sk.getDfa().isFinalState(currentState)) {
                accumulator.addPairingInverse(wEnc[index++], sk.getkEnd(currentState, 0))
                        .addPairing(wEnc[index], sk.getkEnd(currentState, 1));

                // Recover the message...
                Element M = cm.div(accumulator.awaitResult());

                return M.toBytes();
            } else {
                return cm.toBytes();
            }
        } else {
            Element M = pairing.getGT().newRandomElement();

            // Encrypt the massage under the specified attributes
            EncryptionParameters<RLW12PublicKeyParameters, Character> encKey = (EncryptionParameters<RLW12PublicKeyParameters, Character>) key;
            RLW12PublicKeyParameters publicKey = encKey.getMpk();
            Assignment<Character> assignment = encKey.getAssignment();

            ElementStreamWriter writer = new ElementStreamWriter(getOutputBlockSize());
            try {
                // Store M
                writer.write(M);

                // Store the ciphertext

                // Store assignment
                writer.write(assignment.toString());

                // Initialize
                Element s0 = pairing.getZr().newRandomElement();
                writer.write(publicKey.getParameters().getG().powZn(s0));
                writer.write(publicKey.gethStart().powZn(s0));

                // Sequence
                Element sPrev = s0;
                for (int i = 0, l = assignment.getLength(); i < l; i++) {
                    Element sNext = pairing.getZr().newRandomElement();

                    writer.write(publicKey.getParameters().getG().powZn(sNext));
                    writer.write(publicKey.getHAt(assignment.getAt(i)).powZn(sNext).mul(publicKey.getZ().powZn(sPrev)));

                    sPrev = sNext;
                }

                // Finalize
                writer.write(publicKey.getParameters().getG().powZn(sPrev));
                writer.write(publicKey.gethEnd().powZn(sPrev));

                // Store the masked message
                writer.write(publicKey.getOmega().powZn(sPrev).mul(M));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return writer.toBytes();
        }
    }


}