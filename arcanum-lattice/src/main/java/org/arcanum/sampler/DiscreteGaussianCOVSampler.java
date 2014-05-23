package org.arcanum.sampler;

import org.apfloat.Apfloat;
import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.Sampler;
import org.arcanum.Vector;
import org.arcanum.field.floating.FloatingElement;
import org.arcanum.field.vector.VectorField;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class DiscreteGaussianCOVSampler implements Sampler<Vector> {

    protected SecureRandom random;
    protected Matrix cov;
    protected Sampler<Vector> sampler;
    protected Field target;
    protected GaussianSampler<BigInteger> roundingSampler;


    public DiscreteGaussianCOVSampler(SecureRandom random, Matrix cov, Field target, Apfloat roundingGaussianParameter) {
        if (random == null)
            random = new SecureRandom();

        this.random = random;
        this.cov = cov;
        this.sampler = new ContinuousGaussianSamplerVectorSampler(random, 128, cov.getN());
        this.target = new VectorField<Field>(random, target, cov.getN());

        this.roundingSampler = new DiscreteGaussianLazyRSSampler(random, roundingGaussianParameter);
    }


    public Vector sample() {
        Vector sample = (Vector) cov.mul(sampler.sample());

        Vector result = (Vector) target.newElement();
        for (int i = 0, n = result.getSize(); i < n; i++) {
            result.getAt(i).set(
                    roundingSampler.sample(((FloatingElement) sample.getAt(i)).getValue())
            );
        }

        return result;
    }
}
