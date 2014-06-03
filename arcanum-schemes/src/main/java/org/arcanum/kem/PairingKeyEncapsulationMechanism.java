package org.arcanum.kem;

import org.arcanum.cipher.PairingAsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class PairingKeyEncapsulationMechanism extends PairingAsymmetricBlockCipher implements KeyEncapsulationMechanism {

    private static byte[] EMPTY = new byte[0];


    protected int keyBytes = 0;

    public KeyEncapsulationMechanism initForEncryption(CipherParameters param) {
        init(true, param);

        return this;
    }

    public KeyEncapsulationMechanism initForDecryption(CipherParameters param) {
        init(false, param);

        return this;
    }

    public int getKeyBlockSize() {
        return keyBytes;
    }

    public byte[] processBlock(byte[] in)  {
        try {
            return processBlock(in, 0, in.length);
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] process()  {
        try {
            return processBlock(EMPTY, 0, 0);
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }


    public int getInputBlockSize() {
        if (forEncryption)
            return 0;

        return outBytes - keyBytes;
    }

    public int getOutputBlockSize() {
        if (forEncryption)
            return outBytes;

        return keyBytes;
    }


}
