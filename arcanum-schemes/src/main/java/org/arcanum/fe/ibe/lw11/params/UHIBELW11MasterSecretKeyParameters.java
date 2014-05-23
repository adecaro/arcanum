package org.arcanum.fe.ibe.lw11.params;

import org.arcanum.Element;
import org.arcanum.Parameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class UHIBELW11MasterSecretKeyParameters extends UHIBELW11KeyParameters {

    private Element alpha;


    public UHIBELW11MasterSecretKeyParameters(Parameters parameters, Element alpha) {
        super(true, parameters);

        this.alpha = alpha.getImmutable();
    }


    public Element getAlpha() {
        return alpha;
    }
}
