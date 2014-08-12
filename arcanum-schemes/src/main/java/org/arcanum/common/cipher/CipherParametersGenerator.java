package org.arcanum.common.cipher;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public interface CipherParametersGenerator {

    public CipherParametersGenerator init(KeyGenerationParameters keyGenerationParameters);

    public CipherParameters generateKey();

}
