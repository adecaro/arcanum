package org.arcanum.fe.abe.gghvv13.params;

import org.arcanum.Element;
import org.arcanum.common.fe.params.KeyParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GGHVV13MasterSecretKeyParameters extends KeyParameters<GGHVV13Parameters> {

    private Element alpha;


    public GGHVV13MasterSecretKeyParameters(GGHVV13Parameters parameters, Element alpha) {
        super(true, parameters);

        this.alpha = alpha.getImmutable();
    }


    public Element getAlpha() {
        return alpha;
    }
}
