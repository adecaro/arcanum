package org.arcanum.tor.gvw13.engines;

import org.arcanum.Element;
import org.arcanum.ElementCipher;
import org.arcanum.ElementCipherParameters;
import org.arcanum.common.cipher.engine.AbstractElementCipher;
import org.arcanum.field.util.ElementUtils;
import org.arcanum.tor.gvw13.params.TORGVW13EncryptParameters;
import org.arcanum.tor.gvw13.params.TORGVW13PublicKeyParameters;
import org.arcanum.tor.gvw13.params.TORGVW13RecodeParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public class TORGVW13Engine extends AbstractElementCipher {

    private ElementCipherParameters param;

    public ElementCipher init(ElementCipherParameters param) {
        this.param = param;

        return this;
    }

    @Override
    public ElementCipher init(Element key) {
        return init(new TORGVW13EncryptParameters((TORGVW13PublicKeyParameters) param, key));
    }

    public Element processBytes(byte[] buffer) {
        if (param instanceof TORGVW13EncryptParameters) {
            TORGVW13EncryptParameters keyParameters = (TORGVW13EncryptParameters) param;

            return keyParameters.getPublicKeyParameters().getOtp().init(
                    keyParameters.getKey()
            ).processBytes(buffer);
        } else
            throw new IllegalArgumentException("Invalid params!!!");
    }

    @Override
    public byte[] processElementsToBytes(Element... input) {
        if (param instanceof TORGVW13EncryptParameters) {
            TORGVW13EncryptParameters keyParameters = (TORGVW13EncryptParameters) param;

            return keyParameters.getPublicKeyParameters().getOtp().init(
                    keyParameters.getKey()
            ).processElementsToBytes(input[0]);
        } else
            throw new IllegalArgumentException("Invalid params!!!");
    }

    public Element processElements(Element... input) {
        if (param instanceof TORGVW13PublicKeyParameters) {
            TORGVW13PublicKeyParameters keyParameters = (TORGVW13PublicKeyParameters) param;

            return keyParameters.getOwf().processElements(input[0]);
        } else if (param instanceof TORGVW13RecodeParameters) {
            TORGVW13RecodeParameters keyParameters = (TORGVW13RecodeParameters) param;

            return ElementUtils.union(input[0], input[1]).mul(keyParameters.getR());
        } else
            throw new IllegalArgumentException("Invalid params!!!");
    }

}
