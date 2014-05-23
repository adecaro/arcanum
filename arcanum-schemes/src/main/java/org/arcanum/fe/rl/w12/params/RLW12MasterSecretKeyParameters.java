package org.arcanum.fe.rl.w12.params;

import org.arcanum.Element;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class RLW12MasterSecretKeyParameters extends RLW12KeyParameters {

    private Element alpha;


    public RLW12MasterSecretKeyParameters(RLW12Parameters parameters, Element alpha) {
        super(true, parameters);

        this.alpha = alpha.getImmutable();
    }


    public Element getAlpha() {
        return alpha;
    }
}
