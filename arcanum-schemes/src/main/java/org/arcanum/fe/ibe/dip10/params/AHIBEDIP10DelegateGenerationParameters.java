package org.arcanum.fe.ibe.dip10.params;

import org.arcanum.Element;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AHIBEDIP10DelegateGenerationParameters extends KeyGenerationParameters {

    private AHIBEDIP10PublicKeyParameters publicKey;
    private AHIBEDIP10SecretKeyParameters secretKey;
    private Element id;


    public AHIBEDIP10DelegateGenerationParameters(AHIBEDIP10PublicKeyParameters publicKey, AHIBEDIP10SecretKeyParameters secretKey, Element id) {
        super(null, 12);

        this.publicKey = publicKey;
        this.secretKey = secretKey;
        this.id = id.getImmutable();
    }

    public AHIBEDIP10PublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public AHIBEDIP10SecretKeyParameters getSecretKey() {
        return secretKey;
    }

    public Element getId() {
        return id;
    }
}
