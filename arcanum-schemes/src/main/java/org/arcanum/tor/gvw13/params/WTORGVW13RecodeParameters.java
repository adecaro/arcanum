package org.arcanum.tor.gvw13.params;

import org.arcanum.Element;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class WTORGVW13RecodeParameters extends WTORGVW13KeyParameters {

    private Element rk;

    public WTORGVW13RecodeParameters(WTORGVW13Parameters parameters, Element rk) {
        super(true, parameters);
        this.rk = rk;
    }

    public Element getRk() {
        return rk;
    }
}
