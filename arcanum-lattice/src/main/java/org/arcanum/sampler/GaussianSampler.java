package org.arcanum.sampler;

import org.apfloat.Apfloat;
import org.arcanum.Sampler;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public interface GaussianSampler<E> extends Sampler<E> {

    GaussianSampler<E> setCenter(Apfloat center);

    E sample(Apfloat center);

}
