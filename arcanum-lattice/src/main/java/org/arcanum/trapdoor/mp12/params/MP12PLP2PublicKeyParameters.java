package org.arcanum.trapdoor.mp12.params;

import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.Sampler;
import org.arcanum.field.vector.VectorField;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12PLP2PublicKeyParameters extends MP12KeyParameters {

    protected int k;
    protected Sampler<BigInteger> discreteGaussianSampler;

    protected Matrix G; // parity-check matrix

    protected Field syndromeField;

    protected Field Zq;
    protected VectorField<Field> preimageField;


    public MP12PLP2PublicKeyParameters(MP12Parameters parameters,
                                       int k,
                                       Sampler<BigInteger> discreteGaussianSampler,
                                       Matrix G,
                                       Field syndromeField,
                                       Field Zq,
                                       VectorField<Field> preimageField) {
        super(false, parameters);

        this.k = k;
        this.discreteGaussianSampler = discreteGaussianSampler;
        this.G = G;
        this.syndromeField = syndromeField;
        this.Zq = Zq;
        this.preimageField = preimageField;
    }

    public int getK() {
        return k;
    }

    public Sampler<BigInteger> getDiscreteGaussianSampler() {
        return discreteGaussianSampler;
    }

    public Matrix getG() {
        return G;
    }

    public VectorField<Field> getPreimageField() {
        return preimageField;
    }

    public Field getSyndromeField() {
        return syndromeField;
    }

    public Field getZq() {
        return Zq;
    }
}
