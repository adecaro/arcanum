package org.arcanum.signature.ps06.params;

import org.arcanum.Element;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PS06MasterSecretKeyParameters extends PS06KeyParameters {
    private Element msk;


    public PS06MasterSecretKeyParameters(PS06Parameters parameters, Element msk) {
        super(true, parameters);

        this.msk = msk.getImmutable();
    }


    public Element getMsk() {
        return msk;
    }
}