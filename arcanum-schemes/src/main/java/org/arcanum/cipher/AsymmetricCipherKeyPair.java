package org.arcanum.cipher;

import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AsymmetricCipherKeyPair<P extends CipherParameters, S extends CipherParameters> extends
        org.bouncycastle.crypto.AsymmetricCipherKeyPair {

    public AsymmetricCipherKeyPair(P publicKey, S privateKey) {
        super(publicKey, privateKey);
    }

}
