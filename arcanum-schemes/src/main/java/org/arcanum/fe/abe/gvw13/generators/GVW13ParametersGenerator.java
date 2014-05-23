package org.arcanum.fe.abe.gvw13.generators;

import org.arcanum.fe.abe.gvw13.params.GVW13Parameters;
import org.arcanum.tor.gvw13.engines.TORGVW13Engine;
import org.arcanum.tor.gvw13.generators.TORGVW13KeyPairGenerator;
import org.arcanum.tor.gvw13.generators.TORGVW13RecKeyGenerator;
import org.arcanum.tor.gvw13.params.TORGVW13KeyPairGenerationParameters;
import org.arcanum.tor.gvw13.params.TORGVW13Parameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GVW13ParametersGenerator {
    private SecureRandom random;
    private int ell;
    private int depth;

    public GVW13ParametersGenerator(SecureRandom random, int ell, int depth) {
        this.random = random;
        this.ell = ell;
        this.depth = depth;
    }

    public GVW13Parameters generateParameters() {
        TORGVW13Parameters parameters = new TORGVW13Parameters(
                random,
                8,          // n
                depth       // depth
        );

        TORGVW13KeyPairGenerator keyPairGenerator = new TORGVW13KeyPairGenerator();
        keyPairGenerator.init(new TORGVW13KeyPairGenerationParameters(random, 100, parameters));

        return new GVW13Parameters(
                random,
                100,
                ell,
                keyPairGenerator,
                new TORGVW13RecKeyGenerator(),
                new TORGVW13Engine(),
                keyPairGenerator.getOwfInputField(),
                keyPairGenerator.getKeyLengthInBytes()
        );
    }
}
