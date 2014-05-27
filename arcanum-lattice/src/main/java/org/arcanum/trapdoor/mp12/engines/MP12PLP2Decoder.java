package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.ElementCipher;
import org.arcanum.ElementCipherParameters;
import org.arcanum.trapdoor.mp12.params.MP12PLP2PublicKeyParameters;
import org.arcanum.util.cipher.engine.AbstractElementCipher;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12PLP2Decoder extends AbstractElementCipher {

    protected MP12PLP2PublicKeyParameters parameters;

    public ElementCipher init(ElementCipherParameters param) {
        this.parameters = (MP12PLP2PublicKeyParameters) param;

        return this;
    }

    public Element processElements(Element... input) {
        return parameters.getG().mul(input[0]);
    }

}
