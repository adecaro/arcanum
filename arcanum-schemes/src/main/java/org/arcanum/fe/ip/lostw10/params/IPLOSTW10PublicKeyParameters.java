package org.arcanum.fe.ip.lostw10.params;

import org.arcanum.Element;
import org.arcanum.field.util.ElementUtils;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class IPLOSTW10PublicKeyParameters extends IPLOSTW10KeyParameters {
    private Element[] B;
    private Element sigma;


    public IPLOSTW10PublicKeyParameters(IPLOSTW10Parameters parameters, Element[] B, Element sigma) {
        super(false, parameters);

        this.B = ElementUtils.cloneImmutable(B);
        this.sigma = sigma.getImmutable();
    }


    public Element getBAt(int index) {
        return B[index];
    }

    public Element getSigma() {
        return sigma;
    }
}