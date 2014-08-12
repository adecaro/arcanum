package org.arcanum.common.fe.params;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class KeyPairGenerationParameters<P extends CipherParameters> extends KeyGenerationParameters {

    private P params;

    public KeyPairGenerationParameters(SecureRandom random, P params) {
        super(random, 0);

        this.params = params;
    }

    public P getParameters() {
        return params;
    }

}
