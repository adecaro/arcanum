package org.arcanum.fe.ibe.dip10.params;

import org.arcanum.Element;
import org.arcanum.Parameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AHIBEDIP10MasterSecretKeyParameters extends AHIBEDIP10KeyParameters {

    private Element X1;
    private Element alpha;


    public AHIBEDIP10MasterSecretKeyParameters(Parameters parameters, Element x1, Element alpha) {
        super(true, parameters);

        this.X1 = x1.getImmutable();
        this.alpha = alpha.getImmutable();
    }

    public Element getX1() {
        return X1;
    }

    public Element getAlpha() {
        return alpha;
    }
}
