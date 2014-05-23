package org.arcanum.signature.ps06.generators;

import org.arcanum.Element;
import org.arcanum.Pairing;
import org.arcanum.Parameters;
import org.arcanum.pairing.PairingFactory;
import org.arcanum.signature.ps06.params.PS06Parameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PS06ParametersGenerator {
    private Parameters curveParams;
    private Pairing pairing;
    private int nU, nM;


    public PS06ParametersGenerator init(Parameters curveParams, int nU, int nM) {
        this.curveParams = curveParams;
        this.nU = nU;
        this.nM = nM;

        this.pairing = PairingFactory.getPairing(curveParams);

        return this;
    }

    public PS06Parameters generateParameters() {
        Element g = pairing.getG1().newRandomElement();

        return new PS06Parameters(curveParams, g.getImmutable(), nU, nM);
    }
}
