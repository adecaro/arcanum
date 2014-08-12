package org.arcanum.signature.ps06.params;

import org.arcanum.Element;
import org.arcanum.common.parameters.Parameters;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PS06Parameters implements CipherParameters {
    private Parameters curveParams;
    private Element g;
    private int nU, nM;


    public PS06Parameters(Parameters curveParams, Element g, int nU, int nM) {
        this.curveParams = curveParams;
        this.g = g;
        this.nU = nU;
        this.nM = nM;
    }


    public Parameters getCurveParams() {
        return curveParams;
    }

    public Element getG() {
        return g;
    }

    public int getnU() {
        return nU;
    }

    public int getnM() {
        return nM;
    }

}