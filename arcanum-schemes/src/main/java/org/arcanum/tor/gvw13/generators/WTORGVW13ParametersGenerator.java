package org.arcanum.tor.gvw13.generators;

import org.arcanum.Element;
import org.arcanum.Pairing;
import org.arcanum.Parameters;
import org.arcanum.pairing.PairingFactory;
import org.arcanum.tor.gvw13.params.WTORGVW13Parameters;


/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class WTORGVW13ParametersGenerator {
    private Parameters parameters;

    private Pairing pairing;


    public WTORGVW13ParametersGenerator init(Parameters parameters) {
        this.parameters = parameters;
        this.pairing = PairingFactory.getPairing(this.parameters);

        return this;
    }


    public WTORGVW13Parameters generateParameters() {
        Element a = pairing.getZr().newElement().setToRandom();

        Element g1 = pairing.getG1().newElement().setToRandom().getImmutable();
        Element g2 = pairing.getG2().newElement().setToRandom().getImmutable();

        Element g1a = g1.powZn(a).getImmutable();
        Element g2a = g2.powZn(a).getImmutable();

        return new WTORGVW13Parameters(parameters, g1, g2, g1a, g2a);
    }

}