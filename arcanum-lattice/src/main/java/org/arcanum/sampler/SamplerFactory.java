package org.arcanum.sampler;

import org.apfloat.Apfloat;
import org.arcanum.Sampler;
import org.arcanum.field.floating.ApfloatUtils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class SamplerFactory {

    private static final SamplerFactory INSTANCE = new SamplerFactory();

    public static SamplerFactory getInstance() {
        return INSTANCE;
    }

    private SamplerFactory() {
    }


    public Sampler<BigInteger> getDiscreteGaussianSampler(SecureRandom random, Apfloat gaussianParameter) {
        return new DiscreteGaussianCDTSampler(random, gaussianParameter);
//        return new DiscreteGaussianRSDoubleSampler(random, gaussianParameter);
    }

    public Sampler<BigInteger> getDiscreteGaussianSampler(SecureRandom random, int gaussianParameter) {
        return new DiscreteGaussianCDTSampler(random, ApfloatUtils.newApfloat(gaussianParameter));
//        return new DiscreteGaussianRSDoubleSampler(random, ApfloatUtils.newApfloat(gaussianParameter));
    }
}
