package org.arcanum.signature.bls01.params;

import org.arcanum.Element;
import org.arcanum.Parameters;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BLS01Parameters implements CipherParameters {
    private Parameters parameters;
    private Element g;


    public BLS01Parameters(Parameters parameters, Element g) {
        this.parameters = parameters;
        this.g = g.getImmutable();
    }


    public Parameters getParameters() {
        return parameters;
    }

    public Element getG() {
        return g;
    }

}