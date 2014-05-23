package org.arcanum.tor.gvw13.params;

import org.arcanum.Element;
import org.arcanum.Parameters;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class WTORGVW13Parameters implements CipherParameters {
    private Parameters parameters;
    private Element g1;
    private Element g2;

    private Element g1a;
    private Element g2a;

    public WTORGVW13Parameters(Parameters parameters, Element g1, Element g2, Element g1a, Element g2a) {
        this.parameters = parameters;
        this.g1 = g1;
        this.g2 = g2;
        this.g1a = g1a;
        this.g2a = g2a;
    }


    public Parameters getParameters() {
        return parameters;
    }

    public Element getG1() {
        return g1;
    }

    public Element getG2() {
        return g2;
    }

    public Element getG1a() {
        return g1a;
    }

    public Element getG2a() {
        return g2a;
    }
}