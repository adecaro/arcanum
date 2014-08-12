package org.arcanum.fe.abe.gghvv13.params;

import org.arcanum.Element;
import org.arcanum.common.fe.params.KeyParameters;
import org.arcanum.field.util.ElementUtils;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GGHVV13PublicKeyParameters extends KeyParameters<GGHVV13Parameters> {

    private Element H;
    private Element[] hs;

    public GGHVV13PublicKeyParameters(GGHVV13Parameters parameters, Element H, Element[] hs) {
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
