package org.arcanum.fe.abe.gghsw13.params;

import org.arcanum.Element;
import org.arcanum.common.fe.params.KeyParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GGHSW13MasterSecretKeyParameters extends KeyParameters<GGHSW13Parameters> {

    private Element alpha;


    public GGHSW13MasterSecretKeyParameters(GGHSW13Parameters parameters, Element alpha) {
        super(true, parameters);

        this.alpha = alpha.getImmutable();
    }


    public Element getAlpha() {
        return alpha;
    }
}
