package org.arcanum.fe.abe.gghvv13.generators;

import org.arcanum.Pairing;
import org.arcanum.fe.abe.gghvv13.params.GGHVV13Parameters;


/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GGHVV13ParametersGenerator {

    private Pairing pairing;
    private int n;


    public GGHVV13ParametersGenerator init(Pairing pairing, int n) {
        this.pairing = pairing;
        this.n = n;

        return this;
    }


    public GGHVV13Parameters generateParameters() {
        return new GGHVV13Parameters(pairing, n);
    }

}