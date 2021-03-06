package org.arcanum.trapdoor.mp12.generators;

import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.Sampler;
import org.arcanum.Vector;
import org.arcanum.common.cipher.generators.ElementKeyPairGenerator;
import org.arcanum.common.cipher.params.ElementKeyPairParameters;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.field.vector.VectorField;
import org.arcanum.field.z.ZFieldSelector;
import org.arcanum.sampler.SamplerFactory;
import org.arcanum.trapdoor.mp12.params.MP12PLP2KeyPairGenerationParameters;
import org.arcanum.trapdoor.mp12.params.MP12PLP2PublicKeyParameters;

import java.math.BigInteger;
import java.security.SecureRandom;

import static org.arcanum.field.floating.ApfloatUtils.ITWO;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12PLP2KeyPairGenerator<E extends MP12PLP2KeyPairGenerationParameters> implements ElementKeyPairGenerator<E> {
    protected MP12PLP2KeyPairGenerationParameters params;

    protected SecureRandom random;
    protected int n, k;
    protected BigInteger q;
    protected Sampler<BigInteger> discreteGaussianSampler;

    protected Vector g; // primitive vector
    protected Matrix G; // parity-check matrix

    protected Field syndromeField;

    protected Field Zq;
    protected VectorField<Field> preimageField;

    protected ElementKeyPairParameters keyPair;

    public MP12PLP2KeyPairGenerator<E> init(E keyGenerationParameters) {
        this.params = keyGenerationParameters;

        this.n = params.getParameters().getN();
        this.k = params.getK();
        int m = n * k + params.getExtraM();
        this.discreteGaussianSampler = SamplerFactory.getInstance().getDiscreteGaussianSampler(
                random,
                params.getParameters().getRandomizedRoundingParameter().multiply(ITWO)
        );

        SecureRandom random = params.getRandom();

        this.Zq = ZFieldSelector.getInstance().getSymmetricZrFieldPowerOfTwo(random, k);
        this.syndromeField = new VectorField<Field>(random, Zq, n);
        this.preimageField = new VectorField<Field>(random, Zq, m);

        // Construct primitive G
        VectorField<Field> gField = new VectorField<Field>(random, Zq, k);
        this.g = gField.newElement();
        BigInteger value = BigInteger.ONE;
        for (int i = 0; i < k; i++) {
            g.getAt(i).set(value);
            value = value.shiftLeft(1);
        }

        this.G = new MatrixField<Field>(random, Zq, n, m).newElementIdentity(g);

        this.keyPair = new ElementKeyPairParameters(
                new MP12PLP2PublicKeyParameters(
                        params.getParameters(), k,
                        g, G, syndromeField, Zq, preimageField
                ),
                null
        );

        return this;
    }

    public ElementKeyPairParameters generateKeyPair() {
        return keyPair;
    }

}
