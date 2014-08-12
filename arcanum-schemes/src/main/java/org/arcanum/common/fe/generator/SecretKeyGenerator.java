package org.arcanum.common.fe.generator;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class SecretKeyGenerator<PK extends CipherParameters, SK extends CipherParameters, P> {

    protected PK publicKey;
    protected SK secretKey;


    public SecretKeyGenerator<PK, SK, P> init(AsymmetricCipherKeyPair keyPair) {
        this.publicKey = (PK) keyPair.getPublic();
        this.secretKey = (SK) keyPair.getPrivate();

        return this;
    }

    public SecretKeyGenerator<PK, SK, P> init(PK publicKey, SK secretKey) {
        this.publicKey = publicKey;
        this.secretKey = secretKey;

        return this;
    }


    public abstract CipherParameters generateKey(P program);

}
