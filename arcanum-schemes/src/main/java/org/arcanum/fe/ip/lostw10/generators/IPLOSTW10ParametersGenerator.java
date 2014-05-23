package org.arcanum.fe.ip.lostw10.generators;

import org.arcanum.Element;
import org.arcanum.Pairing;
import org.arcanum.Parameters;
import org.arcanum.fe.ip.lostw10.params.IPLOSTW10Parameters;
import org.arcanum.pairing.PairingFactory;


/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class IPLOSTW10ParametersGenerator {
    private Parameters parameters;
    private int n;

    private Pairing pairing;


    public IPLOSTW10ParametersGenerator init(Parameters parameters, int n) {
        this.parameters = parameters;
        this.n = n;
        this.pairing = PairingFactory.getPairing(this.parameters);

        return this;
    }


    public IPLOSTW10Parameters generateParameters() {
        Element g = pairing.getG1().newElement().setToRandom();

        return new IPLOSTW10Parameters(parameters, g.getImmutable(), n);
    }

}