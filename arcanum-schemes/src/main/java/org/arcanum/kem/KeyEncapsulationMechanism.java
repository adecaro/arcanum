package org.arcanum.kem;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public interface KeyEncapsulationMechanism extends AsymmetricBlockCipher {

    int getKeyBlockSize();

    public byte[] processBlock(byte[] in) throws InvalidCipherTextException;

    byte[] process() throws InvalidCipherTextException;

}
