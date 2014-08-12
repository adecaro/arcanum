package org.arcanum.common.fe.engine;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public interface PredicateOnlyEngine extends AsymmetricBlockCipher {

    public byte[] process() throws InvalidCipherTextException;


    public boolean evaluate(byte[] in, int inOff, int len) throws InvalidCipherTextException;

    public boolean evaluate(byte[] in) throws InvalidCipherTextException;
}
