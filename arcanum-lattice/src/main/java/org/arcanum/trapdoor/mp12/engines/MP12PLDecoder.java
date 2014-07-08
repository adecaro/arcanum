package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.ElementCipher;
import org.arcanum.ElementCipherParameters;
import org.arcanum.trapdoor.mp12.params.MP12PLPublicKeyParameters;
import org.arcanum.util.cipher.engine.AbstractElementCipher;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12PLDecoder extends AbstractElementCipher {

    protected MP12PLPublicKeyParameters parameters;

    public ElementCipher init(ElementCipherParameters param) {
        this.parameters = (MP12PLPublicKeyParameters) param;

        return this;
    }

    public Element processElements(Element... input) {
        return parameters.getG().mul(input[0]);
    }

}
