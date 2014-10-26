package org.arcanum.fhe.bv14.params;


import org.arcanum.Vector;
import org.arcanum.common.cipher.params.ElementKeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BV14DMRKeyGenerationParameters extends ElementKeyGenerationParameters {

    private Vector s, t;

    public BV14DMRKeyGenerationParameters(SecureRandom random, Vector s, Vector t) {
        super(random);

        this.s = s;
        this.t = t;
    }

    public Vector getS() {
        return s;
    }

    public Vector getT() {
        return t;
    }

}
