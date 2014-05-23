package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.trapdoor.mp12.params.MP12HLP2PublicKeyParameters;
import org.arcanum.util.cipher.engine.AbstractElementCipher;
import org.arcanum.util.cipher.engine.ElementCipher;
import org.arcanum.util.cipher.params.ElementCipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2Decoder extends AbstractElementCipher {

    protected MP12HLP2PublicKeyParameters parameters;

    public ElementCipher init(ElementCipherParameters param) {
        this.parameters = (MP12HLP2PublicKeyParameters) param;

        return this;
    }

    public Element processElements(Element... input) {
        return parameters.getA().mul(input[0]);
    }

}
