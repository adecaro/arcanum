package org.arcanum.signature.bls01.params;

import org.arcanum.Element;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BLS01PrivateKeyParameters extends BLS01KeyParameters {
    private Element sk;


    public BLS01PrivateKeyParameters(BLS01Parameters parameters, Element sk) {
        super(true, parameters);
        this.sk = sk;
    }


    public Element getSk() {
        return sk;
    }
}