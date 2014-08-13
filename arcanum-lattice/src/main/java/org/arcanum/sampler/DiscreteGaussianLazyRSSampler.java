package org.arcanum.sampler;

import org.apfloat.Apfloat;
import org.arcanum.common.concurrent.ThreadSecureRandom;
import org.arcanum.field.util.DoubleUtils;
import org.arcanum.trapdoor.mp12.utils.MP12P2Utils;

import java.math.BigInteger;
import java.security.SecureRandom;

import static org.arcanum.field.floating.ApfloatUtils.newApfloat;


/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class DiscreteGaussianLazyRSSampler implements GaussianSampler<BigInteger> {

    protected SecureRandom random;
    protected double gaussianParameter;
    protected double sigma;
    protected double center;

    protected double h, sigmaTau, bound;
    protected long left;
    protected int interval;


    public DiscreteGaussianLazyRSSampler(SecureRandom random, double gaussianParameter, double center) {
        if (random == null)
            random = new SecureRandom();

        this.random = random;
        this.gaussianParameter = gaussianParameter;
        this.sigma = gaussianParameter / DoubleUtils.SQRT_TWO_PI;
        this.center = center;
        this.h = - (1.0d / (sigma * sigma));
        this.sigmaTau = sigma * MP12P2Utils.TAU;

        this.bound = 1 / Math.sqrt(Math.pow(1024,5));

        setCenter(newApfloat(center));
    }

    public DiscreteGaussianLazyRSSampler(SecureRandom random, double sigma) {
        this(random, sigma, 0.0d);
    }

    public DiscreteGaussianLazyRSSampler(SecureRandom random, Apfloat sigma) {
        this(random, sigma.doubleValue(), 0.0d);
    }


    public DiscreteGaussianLazyRSSampler setCenter(Apfloat center) {
        this.center = center.doubleValue();
        this.interval = (int) (Math.ceil(this.center + sigmaTau) -  Math.floor(this.center - sigmaTau)) + 1;
        // TODO: It could be that we need biginteger
        this.left = (long) Math.floor(this.center - sigmaTau);

        if (interval < 0) {
            System.out.println("BOOOOOOOOOO");
        }

        return this;
    }

    public BigInteger sample() {
        while (true) {
            long x = left + random.nextInt(interval);
            double z = x - center;

            double rhos = Math.exp(h * z * z);
            double sample = random.nextDouble();

            if (Math.abs(sample - rhos) < bound)
                System.out.println("NOOOOO");

            if (sample <= rhos)
                return BigInteger.valueOf(x);
        }
    }

    public BigInteger sample(Apfloat c) {
        SecureRandom random = ThreadSecureRandom.get();

        double center = c.doubleValue();
        int interval = (int) (Math.ceil(center + sigmaTau) -  Math.floor(center - sigmaTau)) + 1;
        long left = (long) Math.floor(center - sigmaTau);

        while (true) {
            long x = left + random.nextInt(interval);
            double z = x - center;

            double rhos = Math.exp(h * z * z);
            double sample = random.nextDouble();
//            System.out.println("rhos = " + rhos);
//            System.out.println("sample = " + sample);


            if (Math.abs(sample - rhos) < bound)
                System.out.println("NOOOOO");

            if (sample <= rhos)
                return BigInteger.valueOf(x);
        }
    }

}