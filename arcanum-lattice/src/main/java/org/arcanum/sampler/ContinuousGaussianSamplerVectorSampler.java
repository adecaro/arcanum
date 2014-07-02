package org.arcanum.sampler;

import org.apfloat.Apfloat;
import org.arcanum.Sampler;
import org.arcanum.Vector;
import org.arcanum.field.floating.FloatingElement;
import org.arcanum.field.floating.FloatingField;
import org.arcanum.field.vector.VectorField;
import org.arcanum.util.concurrent.ThreadSecureRandom;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * TODO: fix precision usage
 */
public class ContinuousGaussianSamplerVectorSampler implements Sampler<Vector> {

    protected SecureRandom random;
    protected int precision, n;
    protected VectorField<FloatingField> vf;
    protected FloatingField ff;


    public ContinuousGaussianSamplerVectorSampler(SecureRandom random, int precision, int n) {
        if (random == null)
            random = new SecureRandom();

        this.random = random;
        this.precision = precision;
        this.n = n;
        this.vf = new VectorField<FloatingField>(
                random,
                (ff = new FloatingField(random, precision, 2)),
                n
        );
    }


    public Vector sample() {
        SecureRandom random = ThreadSecureRandom.get();

        Vector<FloatingElement> v = vf.newElement();

        for (int i = 0; i < n; i++)
            v.getAt(i).setFromObject(new Apfloat(random.nextGaussian(), precision, 2));

        return v;
    }

}
