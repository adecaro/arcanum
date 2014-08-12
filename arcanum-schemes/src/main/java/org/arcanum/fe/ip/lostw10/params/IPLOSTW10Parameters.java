package org.arcanum.fe.ip.lostw10.params;

import org.arcanum.Element;
import org.arcanum.common.parameters.Parameters;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class IPLOSTW10Parameters implements CipherParameters {
    private Parameters parameters;
    private Element g;
    private int n;


    public IPLOSTW10Parameters(Parameters parameters, Element g, int n) {
        this.parameters = parameters;
        this.g = g.getImmutable();
        this.n = n;
    }


    public Parameters getParameters() {
        return parameters;
    }

    public Element getG() {
        return g;
    }

    public int getN() {
        return n;
    }

}