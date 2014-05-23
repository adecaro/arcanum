package org.arcanum.fe.abe.gghsw13.generators;

import org.arcanum.Pairing;
import org.arcanum.fe.abe.gghsw13.params.GGHSW13Parameters;


/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GGHSW13ParametersGenerator {

    private Pairing pairing;
    private int n;


    public GGHSW13ParametersGenerator init(Pairing pairing, int n) {
        this.pairing = pairing;
        this.n = n;

        return this;
    }


    public GGHSW13Parameters generateParameters() {
        return new GGHSW13Parameters(pairing, n);
    }

}