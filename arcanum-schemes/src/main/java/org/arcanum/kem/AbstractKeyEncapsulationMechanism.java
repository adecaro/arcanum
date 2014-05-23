package org.arcanum.kem;

import org.arcanum.cipher.AbstractAsymmetricBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class AbstractKeyEncapsulationMechanism extends AbstractAsymmetricBlockCipher implements KeyEncapsulationMechanism {

    private static byte[] EMPTY = new byte[0];


    protected int keyBytes = 0;

    public int getKeyBlockSize() {
        return keyBytes;
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


    public byte[] processBlock(byte[] in) throws InvalidCipherTextException {
        return processBlock(in, 0, in.length);
    }

    public byte[] process() throws InvalidCipherTextException {
        return processBlock(EMPTY, 0, 0);
    }

}
