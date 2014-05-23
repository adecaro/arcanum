package org.arcanum.tor.gvw13.params;

import org.arcanum.Element;
import org.arcanum.util.cipher.params.ElementKeyParameter;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class TORGVW13EncryptParameters extends ElementKeyParameter {

    private TORGVW13PublicKeyParameters publicKeyParameters;
    private Element key;


    public TORGVW13EncryptParameters(TORGVW13PublicKeyParameters publicKeyParameters, Element key) {
        super(true);

        this.publicKeyParameters = publicKeyParameters;
        this.key = key;
    }


    public TORGVW13PublicKeyParameters getPublicKeyParameters() {
        return publicKeyParameters;
    }

    public Element getKey() {
        return key;
    }
}
