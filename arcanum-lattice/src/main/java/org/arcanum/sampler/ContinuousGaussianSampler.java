package org.arcanum.sampler;

import org.apfloat.Apfloat;
import org.arcanum.Sampler;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ContinuousGaussianSampler implements Sampler<Apfloat> {

    protected SecureRandom random;
    protected int precision;


    public ContinuousGaussianSampler(SecureRandom random, int precision) {
        if (random == null)
            random = new SecureRandom();

        this.random = random;
        this.precision = precision;
    }


    public Apfloat sample() {
        return new Apfloat(Double.toString(random.nextGaussian()), precision, 2);
    }

}
