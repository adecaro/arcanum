package org.arcanum.sampler;

import org.apfloat.Apfloat;
import org.arcanum.Sampler;
import org.arcanum.field.floating.ApfloatUtils;
import org.arcanum.trapdoor.mp12.utils.MP12P2Utils;

import java.math.BigInteger;
import java.security.SecureRandom;

import static org.apfloat.ApfloatMath.exp;
import static org.arcanum.field.floating.ApfloatUtils.*;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class DiscreteGaussianRSSampler implements GaussianSampler<BigInteger> {

    protected SecureRandom random;
    protected Apfloat sigma;
    protected Apfloat center;

    protected Apfloat h, sigmaTau;
    protected int left, interval;

    protected Sampler<Apfloat> uniform;


    public DiscreteGaussianRSSampler(SecureRandom random, Apfloat gaussianParameter, Apfloat center) {
        if (random == null)
            random = new SecureRandom();

        this.random = random;
        this.sigma = gaussianParameter.divide(SQRT2PI);
        this.center = center;

        this.h = ONE.divide(TWO.multiply(square(sigma))).negate();
        this.sigmaTau = sigma.multiply(MP12P2Utils.iTAU);
        this.uniform = new UniformSampler(random, ApfloatUtils.precision);

        setCenter(center);
    }

    public DiscreteGaussianRSSampler(SecureRandom random, Apfloat gaussianParameter) {
        this(random, gaussianParameter, IZERO);
    }


    public DiscreteGaussianRSSampler setCenter(Apfloat center) {
        this.center = center;
        this.interval = center.add(sigmaTau).ceil().subtract(center.subtract(sigmaTau).floor()).intValue() + 1;
        this.left = center.subtract(sigmaTau).floor().intValue();

        return this;
    }

    public BigInteger sample() {
        while (true) {
            int x = left + random.nextInt(interval);
            Apfloat z = newApint(x).subtract(center);

            Apfloat rhos = exp(h.multiply(square(z)));
            Apfloat sample = uniform.sample();

            if (sample.compareTo(rhos) <= 0) {
                return BigInteger.valueOf(x);
            }
        }
    }

    public BigInteger sample(Apfloat center) {
        throw new IllegalStateException("Not implemented yet!!!");
    }
}
