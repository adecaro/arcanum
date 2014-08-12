package org.arcanum.fe.ibe.lw11.params;

import org.arcanum.Element;
import org.arcanum.common.parameters.Parameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class UHIBELW11PublicKeyParameters extends UHIBELW11KeyParameters {

    private Element g;
    private Element u;
    private Element h;
    private Element v;
    private Element w;
    private Element omega;


    public UHIBELW11PublicKeyParameters(Parameters parameters,
                                        Element g, Element u, Element h,
                                        Element v, Element w,
                                        Element omega) {
        super(false, parameters);

        this.g = g.getImmutable();
        this.u = u.getImmutable();
        this.h = h.getImmutable();
        this.v = v.getImmutable();
        this.w = w.getImmutable();
        this.omega = omega.getImmutable();
    }

    
    public Element getG() {
        return g;
    }

    public Element getU() {
        return u;
    }

    public Element getH() {
        return h;
    }

    public Element getV() {
        return v;
    }

    public Element getW() {
        return w;
    }

    public Element getOmega() {
        return omega;
    }

}
