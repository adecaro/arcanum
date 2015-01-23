package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.common.cipher.engine.AbstractElementCipher;
import org.arcanum.trapdoor.mp12.params.MP12HLP2PublicKeyParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2Decoder extends AbstractElementCipher<Element, Element, MP12HLP2PublicKeyParameters> {

    protected MP12HLP2PublicKeyParameters parameters;

    public MP12HLP2Decoder init(MP12HLP2PublicKeyParameters param) {
        this.parameters = param;

        return this;
    }

    public Element processElements(Element... input) {
        return parameters.getA().mul(input[0]);
    }

}
