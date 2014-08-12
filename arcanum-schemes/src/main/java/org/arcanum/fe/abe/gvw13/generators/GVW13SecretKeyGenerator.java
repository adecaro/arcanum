package org.arcanum.fe.abe.gvw13.generators;

import org.arcanum.common.cipher.generators.ElementKeyGenerator;
import org.arcanum.common.cipher.generators.ElementKeyPairGenerator;
import org.arcanum.common.cipher.params.ElementCipherParameters;
import org.arcanum.common.cipher.params.ElementKeyPairParameters;
import org.arcanum.common.fe.generator.SecretKeyGenerator;
import org.arcanum.fe.abe.gvw13.params.GVW13MasterSecretKeyParameters;
import org.arcanum.fe.abe.gvw13.params.GVW13PublicKeyParameters;
import org.arcanum.fe.abe.gvw13.params.GVW13SecretKeyParameters;
import org.arcanum.program.circuit.BooleanCircuit;
import org.arcanum.program.circuit.BooleanGate;
import org.arcanum.tor.gvw13.params.TORGVW13SecretKeyParameters;
import org.bouncycastle.crypto.CipherParameters;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GVW13SecretKeyGenerator extends SecretKeyGenerator<GVW13PublicKeyParameters, GVW13MasterSecretKeyParameters, BooleanCircuit> {

    public CipherParameters generateKey(BooleanCircuit circuit) {
        int n = circuit.getNumInputs();
        int q = circuit.getNumGates();
        int qMinus1 = q - 1;

        // Generate key pairs
        ElementKeyPairGenerator tor = publicKey.getParameters().getTorKeyPairGenerater();
        ElementKeyGenerator reKeyGen = publicKey.getParameters().getTorReKeyPairGenerater();

        ElementCipherParameters[] publicKeys = new ElementCipherParameters[q * 2];
        ElementCipherParameters[] secretKeys = new ElementCipherParameters[q * 2];

        for (int i = 0, size = 2 * qMinus1; i < size; i++) {
            ElementKeyPairParameters keyPair = tor.generateKeyPair();
            publicKeys[i] = keyPair.getPublic();
            secretKeys[i] = keyPair.getPrivate();
        }

        ElementKeyPairParameters keyPair = tor.generateKeyPair();
        publicKeys[2 * qMinus1] = keyPair.getPublic();
        publicKeys[2 * qMinus1 + 1] = publicKey.getCipherParametersOut();

        secretKeys[2 * qMinus1] = keyPair.getPrivate();

        // encode the circuit
        Map<Integer, ElementCipherParameters[]> keys = new HashMap<Integer, ElementCipherParameters[]>();

        for (BooleanGate gate : circuit) {
            int index = gate.getIndex();

            switch (gate.getType()) {
                case INPUT:
                    break;

                case OR:
                    ElementCipherParameters[] recKeys = new ElementCipherParameters[4];
                    int left = gate.getInputIndexAt(0);
                    int right = gate.getInputIndexAt(1);

                    int b0 = 0, b1 = 0;
                    for (int i = 0; i < 4; i++) {
                        ElementCipherParameters leftTorPK = (left < n)
                                ? publicKey.getCipherParametersAt(left, b0)
                                : publicKeys[2 * (left - n) + b0];
                        ElementCipherParameters leftTorSK = (left < n)
                                ? secretKey.getCipherParametersAt(left, b0)
                                : secretKeys[2 * (left - n) + b0];

                        ElementCipherParameters rightTorPK = (right < n)
                                ? publicKey.getCipherParametersAt(right, b1)
                                : publicKeys[2 * (right - n) + b1];

                        int target = b0 == 1 || b1 == 1 ? 1 : 0;
                        ElementCipherParameters targetTorPK = (index < n)
                                ? publicKey.getCipherParametersAt(index, target)
                                : publicKeys[2 * (index - n) + target];


                        recKeys[i] = reKeyGen.init(
                                secretKey.getParameters().getReKeyPairGenerationParameters(leftTorPK, leftTorSK, rightTorPK, targetTorPK)
                        ).generateKey();


                        if (b1 == 0)
                            b1++;
                        else if (b0 == 0) {
                            b0++; b1 = 0;
                        } else
                            break;
                    }

                    keys.put(index, recKeys);
                    break;

                case AND:
                    recKeys = new ElementCipherParameters[4];
                    left = gate.getInputIndexAt(0);
                    right = gate.getInputIndexAt(1);

                    b0 = 0; b1 = 0;
                    for (int i = 0; i < 4; i++) {
                        ElementCipherParameters leftTorPK = (left < n)
                                ? publicKey.getCipherParametersAt(left, b0)
                                : publicKeys[2 * (left - n) + b0];
                        ElementCipherParameters leftTorSK = (left < n)
                                ? secretKey.getCipherParametersAt(left, b0)
                                : secretKeys[2 * (left - n) + b0];

                        ElementCipherParameters rightTorPK = (right < n)
                                ? publicKey.getCipherParametersAt(right, b1)
                                : publicKeys[2 * (right - n) + b1];

                        int target = b0 == 1 && b1 == 1 ? 1 : 0;
                        ElementCipherParameters targetTorPK = (index < n)
                                ? publicKey.getCipherParametersAt(index, target)
                                : publicKeys[2 * (index - n) + target];


                        recKeys[i] = reKeyGen.init(
                                secretKey.getParameters().getReKeyPairGenerationParameters(leftTorPK, leftTorSK, rightTorPK, targetTorPK)
                        ).generateKey();


                        if (b1 == 0)
                            b1++;
                        else if (b0 == 0) {
                            b0++; b1 = 0;
                        } else
                            break;
                    }

                    keys.put(index, recKeys);
                    break;
            }
        }


        return new GVW13SecretKeyParameters(
                publicKey.getParameters(),
                circuit,
                keys,
                ((TORGVW13SecretKeyParameters)secretKeys[0]).getOwfInputField(),
                publicKey.getCipherParametersOut()
        );
    }

}