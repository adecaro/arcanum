package org.arcanum.signature.bls01.generators;

import org.arcanum.Element;
import org.arcanum.common.parameters.Parameters;
import org.arcanum.pairing.Pairing;
import org.arcanum.pairing.PairingFactory;
import org.arcanum.signature.bls01.params.BLS01Parameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BLS01ParametersGenerator {
    private Parameters parameters;
    private Pairing pairing;


    public void init(Parameters parameters) {
        this.parameters = parameters;
        this.pairing = PairingFactory.getPairing(parameters);
    }

    public BLS01Parameters generateParameters() {
        Element g = pairing.getG2().newRandomElement();

        return new BLS01Parameters(parameters, g.getImmutable());
    }
}
