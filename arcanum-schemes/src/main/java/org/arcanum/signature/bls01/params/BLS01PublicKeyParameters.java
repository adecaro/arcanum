package org.arcanum.signature.bls01.params;

import org.arcanum.Element;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BLS01PublicKeyParameters extends BLS01KeyParameters {
    private Element pk;


    public BLS01PublicKeyParameters(BLS01Parameters parameters, Element pk) {
        super(false, parameters);
        this.pk = pk;
    }


    public Element getPk() {
        return pk;
    }
}
