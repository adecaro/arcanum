package org.arcanum.fe.ip.lostw10.params;

import org.arcanum.Element;
import org.arcanum.field.util.ElementUtils;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class IPLOSTW10MasterSecretKeyParameters extends IPLOSTW10KeyParameters {
    private Element[] Bstar;


    public IPLOSTW10MasterSecretKeyParameters(IPLOSTW10Parameters parameters, Element[] Bstar) {
        super(true, parameters);

        this.Bstar = ElementUtils.cloneImmutable(Bstar);
    }

    public Element getBStarAt(int index) {
        return Bstar[index];
    }

}