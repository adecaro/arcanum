package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.common.cipher.engine.AbstractElementCipher;
import org.arcanum.common.cipher.engine.ElementCipher;
import org.arcanum.common.cipher.params.ElementCipherParameters;
import org.arcanum.trapdoor.mp12.params.MP12PLPublicKeyParameters;

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
