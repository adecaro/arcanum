package org.arcanum.sampler;

import org.apfloat.Apfloat;
import org.arcanum.Sampler;
import org.arcanum.field.floating.ApfloatUtils;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class UniformSampler implements Sampler<Apfloat> {

    protected SecureRandom random;
    protected int precision;
    protected Apfloat[] powers;


    public UniformSampler(SecureRandom random, int precision) {
        this.random = random;
        this.precision = precision;

        this.powers = new Apfloat[precision];
        Apfloat power = ApfloatUtils.TWO;
        for (int i = 0; i < precision; i++) {
            this.powers[i] = ApfloatUtils.ONE.divide(power);
            power = power.multiply(ApfloatUtils.TWO);
        }
    }


    public Apfloat sample() {
        Apfloat value = ApfloatUtils.ZERO;
        for (int i = 0; i < precision; i++) {
            if (random.nextBoolean())
                value = value.add(powers[i]);
        }
        return value;
    }

}
