package org.arcanum.common.fe.generator;

import org.arcanum.common.fe.params.KeyPairGenerationParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class KeyPairGenerator<P extends CipherParameters> implements AsymmetricCipherKeyPairGenerator {

    protected KeyPairGenerationParameters<P> params;


    public KeyPairGenerator<P> init(KeyPairGenerationParameters<P> param) {
        this.params = param;

        return this;
    }

    @Override
    public void init(KeyGenerationParameters param) {
        init((KeyPairGenerationParameters<P>) param);
    }

}
