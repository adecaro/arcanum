package org.arcanum.fe.abe.gvw13.params;

import org.arcanum.Field;
import org.arcanum.common.cipher.engine.ElementCipher;
import org.arcanum.common.cipher.generators.ElementKeyGenerator;
import org.arcanum.common.cipher.generators.ElementKeyPairGenerator;
import org.arcanum.common.cipher.params.ElementCipherParameters;
import org.arcanum.common.cipher.params.ElementKeyGenerationParameters;
import org.arcanum.tor.gvw13.params.TORGVW13PublicKeyParameters;
import org.arcanum.tor.gvw13.params.TORGVW13ReKeyGenerationParameters;
import org.arcanum.tor.gvw13.params.TORGVW13SecretKeyParameters;
import org.bouncycastle.crypto.CipherParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GVW13Parameters implements CipherParameters {

    private SecureRandom random;
    private int strength;
    private int ell;

    private ElementCipher tor;
    private ElementKeyPairGenerator torKeyPairGenerater;
    private ElementKeyGenerator torReKeyPairGenerater;

    private Field randomnessField;
    private int keyLengthInBytes;


    public GVW13Parameters(SecureRandom random, int strength, int ell,
                           ElementKeyPairGenerator torKeyPairGenerater,
                           ElementKeyGenerator torReKeyPairGenerater,
                           ElementCipher tor,
                           Field randomnessField,
                           int keyLengthInBytes) {
        this.random = random;
        this.strength = strength;
        this.ell = ell;

        this.torKeyPairGenerater = torKeyPairGenerater;
        this.torReKeyPairGenerater = torReKeyPairGenerater;
        this.tor = tor;
        this.randomnessField = randomnessField;
        this.keyLengthInBytes = keyLengthInBytes;
    }

    public SecureRandom getRandom() {
        return random;
    }

    public int getEll() {
        return ell;
    }

    public ElementKeyGenerationParameters getReKeyPairGenerationParameters(ElementCipherParameters leftTorPK,
                                                                    ElementCipherParameters leftTorSK,
                                                                    ElementCipherParameters rightTorPK,
                                                                    ElementCipherParameters targetTorPK) {
        return new TORGVW13ReKeyGenerationParameters(
                random, strength,
                (TORGVW13PublicKeyParameters) leftTorPK,
                (TORGVW13SecretKeyParameters) leftTorSK,
                (TORGVW13PublicKeyParameters) rightTorPK,
                (TORGVW13PublicKeyParameters) targetTorPK
        );
    }

    public ElementKeyPairGenerator getTorKeyPairGenerater() {
        return torKeyPairGenerater;
    }

    public ElementKeyGenerator getTorReKeyPairGenerater() {
        return torReKeyPairGenerater;
    }

    public Field getRandomnessField() {
        return randomnessField;
    }

    public ElementCipher getTor() {
        return tor;
    }

    public int getKeyLengthInBytes() {
        return keyLengthInBytes;
    }
}