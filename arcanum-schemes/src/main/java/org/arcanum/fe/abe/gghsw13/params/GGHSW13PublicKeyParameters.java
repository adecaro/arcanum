package org.arcanum.fe.abe.gghsw13.params;

import org.arcanum.Element;
import org.arcanum.field.util.ElementUtils;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GGHSW13PublicKeyParameters extends GGHSW13KeyParameters {

    private Element H;
    private Element[] hs;

    public GGHSW13PublicKeyParameters(GGHSW13Parameters parameters, Element H, Element[] hs) {
        super(false, parameters);

        this.H = H.getImmutable();
        this.hs = ElementUtils.cloneImmutable(hs);
    }

    public Element getH() {
        return H;
    }

    public Element getHAt(int index) {
        return hs[index];
    }
}
