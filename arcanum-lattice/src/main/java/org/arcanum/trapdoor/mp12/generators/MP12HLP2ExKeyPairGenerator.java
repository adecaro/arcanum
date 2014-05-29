package org.arcanum.trapdoor.mp12.generators;

import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.Sampler;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.field.vector.VectorField;
import org.arcanum.sampler.SamplerFactory;
import org.arcanum.trapdoor.mp12.params.MP12HLP2KeyPairGenerationParameters;
import org.arcanum.trapdoor.mp12.params.MP12HLP2PrivateKeyParameters;
import org.arcanum.trapdoor.mp12.params.MP12HLP2PublicKeyParameters;
import org.arcanum.trapdoor.mp12.utils.MP12P2Utils;
import org.arcanum.util.cipher.params.ElementKeyGenerationParameters;
import org.arcanum.util.cipher.params.ElementKeyPairParameters;

import java.math.BigInteger;

/**
 * Computational instantiation.
 *
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2ExKeyPairGenerator extends MP12PLP2KeyPairGenerator {
    private MP12HLP2KeyPairGenerationParameters params;

    private int barM, w, m;
    private Field inputField, outputField;
    private Sampler<BigInteger> hlZSampler;

    public void init(ElementKeyGenerationParameters keyGenerationParameters) {
        this.params = (MP12HLP2KeyPairGenerationParameters) keyGenerationParameters;

        super.init(params);

        this.barM = 2 * n;
        this.w = n * k;
        this.m = barM + w;

        this.inputField = new VectorField<Field>(params.getRandom(), Zq, n);
        this.outputField = new VectorField<Field>(params.getRandom(), Zq, m);

        this.hlZSampler = SamplerFactory.getInstance().getDiscreteGaussianSampler(
                random,
                MP12P2Utils.getLWENoiseParameter(n, params.getParameters().getRandomizedRoundingParameter())
        );
    }

    public ElementKeyPairParameters generateKeyPair() {
        super.generateKeyPair();

        MatrixField<Field> hatAField = new MatrixField<Field>(random, Zq, n);
        Matrix R = MatrixField.newElementFromSampler(hatAField, barM, w, hlZSampler);

        return new ElementKeyPairParameters(
                new MP12HLP2PublicKeyParameters(
                        params.getParameters(),
                        k, m,
                        G, syndromeField, Zq, preimageField,
                        null
                ),
                new MP12HLP2PrivateKeyParameters(params.getParameters(), R)
        );
    }

}
