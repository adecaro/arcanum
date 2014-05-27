package org.arcanum.trapdoor.mp12.params;

import org.arcanum.Parameters;
import org.arcanum.util.cipher.params.ElementKeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12PLP2KeyPairGenerationParameters extends ElementKeyGenerationParameters {

    private MP12Parameters params;
    private int k;
    private int extraM;


    public MP12PLP2KeyPairGenerationParameters(SecureRandom random, Parameters parameters) {
        super(random);

        this.params = new MP12Parameters(random, parameters.getInt("n"));
        this.k = parameters.getInt("k");
        this.extraM = 0;
    }

    public MP12PLP2KeyPairGenerationParameters(SecureRandom random, int n, int k) {
        super(random);

        this.params = new MP12Parameters(random, n);
        this.k = k;
        this.extraM = 0;
    }

    public MP12PLP2KeyPairGenerationParameters(SecureRandom random, int n, int k, int extraM) {
        super(random);

        this.params = new MP12Parameters(random, n);
        this.k = k;
        this.extraM = extraM;
    }


    public MP12Parameters getParameters() {
        return params;
    }

    public int getK() {
        return k;
    }

    public int getExtraM() {
        return extraM;
    }

}
