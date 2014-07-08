package org.arcanum.trapdoor.mp12.params;

import org.arcanum.Parameters;
import org.arcanum.field.floating.ApfloatUtils;
import org.arcanum.util.cipher.params.ElementKeyGenerationParameters;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12PLKeyPairGenerationParameters extends ElementKeyGenerationParameters {

    private MP12Parameters params;
    private BigInteger q;
    private int k;
    private int extraM;


    public MP12PLKeyPairGenerationParameters(SecureRandom random, Parameters parameters) {
        super(random);

        this.params = new MP12Parameters(random, parameters.getInt("n"));
        this.k = parameters.getInt("k");
        this.extraM = 0;
    }

    public MP12PLKeyPairGenerationParameters(SecureRandom random, int n, int k) {
        super(random);

        this.params = new MP12Parameters(random, n);
        this.k = k;
        this.extraM = 0;
    }

    public MP12PLKeyPairGenerationParameters(SecureRandom random, int n, int k, int extraM) {
        super(random);

        this.params = new MP12Parameters(random, n);
        this.k = k;
        this.extraM = extraM;
    }

    public MP12PLKeyPairGenerationParameters(SecureRandom random, int n, BigInteger q) {
        super(random);

        this.params = new MP12Parameters(random, n);
        this.q = q;
        this.k = ApfloatUtils.log(q);
    }

    public MP12Parameters getParameters() {
        return params;
    }

    public BigInteger getQ() {
        return q;
    }

    public int getK() {
        return k;
    }

    public int getExtraM() {
        return extraM;
    }

}
