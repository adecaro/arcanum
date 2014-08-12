package org.arcanum.fe.abe.gghsw13.generators;

import org.arcanum.Pairing;
import org.arcanum.Parameters;
import org.arcanum.fe.abe.gghsw13.params.GGHSW13Parameters;
import org.arcanum.pairing.PairingFactory;


/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GGHSW13ParametersGenerator {

    private Pairing pairing;
    private int n;


    public GGHSW13ParametersGenerator init(Parameters parameters, int n) {
        this.pairing = PairingFactory.getPairing(parameters);
        this.n = n;

        return this;
    }


    public GGHSW13Parameters generateParameters() {
        return new GGHSW13Parameters(pairing, n);
    }

}