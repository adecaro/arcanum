package org.arcanum.sampler;

import org.apfloat.Apfloat;
import org.arcanum.field.util.DoubleUtils;
import org.arcanum.trapdoor.mp12.utils.MP12P2Utils;

import java.math.BigInteger;
import java.security.SecureRandom;

import static org.arcanum.field.floating.ApfloatUtils.newApfloat;


/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class DiscreteGaussianRSDoubleSampler implements GaussianSampler<BigInteger> {

    protected SecureRandom random;
    protected double gaussianParameter;
    protected double sigma;
    protected double center;

    protected double h, sigmaTau;
    protected int left, interval;


    public DiscreteGaussianRSDoubleSampler(SecureRandom random, double gaussianParameter, double center) {
        if (random == null)
            random = new SecureRandom();

        this.random = random;
        this.gaussianParameter = gaussianParameter;
        this.sigma = gaussianParameter / DoubleUtils.SQRT_TWO_PI;
        this.center = center;
        this.h = - (1.0d / (sigma * sigma));
        this.sigmaTau = sigma * MP12P2Utils.TAU;

        setCenter(newApfloat(center));
    }

    public DiscreteGaussianRSDoubleSampler(SecureRandom random, double sigma) {
        this(random, sigma, 0.0d);
    }

    public DiscreteGaussianRSDoubleSampler(SecureRandom random, Apfloat sigma) {
        this(random, sigma.doubleValue(), 0.0d);
    }


    public DiscreteGaussianRSDoubleSampler setCenter(Apfloat center) {
        this.center = center.doubleValue();
        this.interval = (int) (Math.ceil(this.center + sigmaTau) -  Math.floor(this.center - sigmaTau)) + 1;
        this.left = (int) Math.floor(this.center - sigmaTau);

        return this;
    }

    public BigInteger sample(Apfloat c) {
        double center = c.doubleValue();
        int interval = (int) (Math.ceil(center + sigmaTau) -  Math.floor(center - sigmaTau)) + 1;
        int left = (int) Math.floor(center - sigmaTau);

        while (true) {
            int x = left + random.nextInt(interval);
            double z = x - center;

            double rhos = Math.exp(h * z * z);
            double sample = random.nextDouble();

            if (sample <= rhos)
                return BigInteger.valueOf(x);
        }
    }

    public BigInteger sample() {
        while (true) {
            int x = left + random.nextInt(interval);
            double z = x - center;

            double rhos = Math.exp(h * z * z);
            double sample = random.nextDouble();

            if (sample <= rhos)
                return BigInteger.valueOf(x);
        }
    }
}