package org.arcanum.fe.abe.gghsw13.params;

import org.arcanum.Element;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GGHSW13MasterSecretKeyParameters extends GGHSW13KeyParameters {

    private Element alpha;


    public GGHSW13MasterSecretKeyParameters(GGHSW13Parameters parameters, Element alpha) {
        super(true, parameters);

        this.alpha = alpha.getImmutable();
    }


    public Element getAlpha() {
        return alpha;
    }
}
