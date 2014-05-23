package org.arcanum.tor.gvw13.params;

import org.arcanum.Element;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class WTORGVW13SecretKeyParameters extends WTORGVW13KeyParameters {

    private Element t;


    public WTORGVW13SecretKeyParameters(WTORGVW13Parameters parameters, Element t) {
        super(true, parameters);

        this.t = t.getImmutable();
    }


    public Element getT() {
        return t;
    }
}
