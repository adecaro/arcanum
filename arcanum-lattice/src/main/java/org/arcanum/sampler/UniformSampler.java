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
    protected Apfloat[] powers;

    public UniformSampler(SecureRandom random) {
        this.random = random;

        powers = new Apfloat[ApfloatUtils.precision];
        Apfloat power = ApfloatUtils.TWO;
        for (int i = 0; i < ApfloatUtils.precision; i++) {
            powers[i] = ApfloatUtils.ONE.divide(power);
            power = power.multiply(ApfloatUtils.TWO);
        }
    }

    public Apfloat sample() {
        Apfloat value = ApfloatUtils.ZERO;
        for (int i = 0; i < ApfloatUtils.precision; i++) {
            if (random.nextBoolean())
                value = value.add(powers[i]);
        }
        return value;
    }

}
