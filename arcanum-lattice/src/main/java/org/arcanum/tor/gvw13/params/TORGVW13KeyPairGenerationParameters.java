package org.arcanum.tor.gvw13.params;

import org.arcanum.util.cipher.params.ElementKeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class TORGVW13KeyPairGenerationParameters extends ElementKeyGenerationParameters {

    private TORGVW13Parameters parameters;


    public TORGVW13KeyPairGenerationParameters(SecureRandom random, int strength, TORGVW13Parameters parameters) {
        super(random, strength);

        this.parameters = parameters;
    }

    public TORGVW13Parameters getParameters() {
        return parameters;
    }

}
