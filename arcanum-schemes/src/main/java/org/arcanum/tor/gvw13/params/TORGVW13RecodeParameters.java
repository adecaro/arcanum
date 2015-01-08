package org.arcanum.tor.gvw13.params;

import org.arcanum.Element;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class TORGVW13RecodeParameters extends TORGVW13KeyParameters {

    private Element R;

    public TORGVW13RecodeParameters(TORGVW13Parameters parameters, Element R) {
        super(true, parameters);

        this.R = R;
    }

    public Element getR() {
        return R;
    }
}
