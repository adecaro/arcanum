package org.arcanum.trapdoor.mp12.params;

import org.arcanum.Field;
import org.arcanum.Sampler;
import org.arcanum.common.cipher.params.ElementKeyParameter;
import org.arcanum.field.vector.VectorField;
import org.arcanum.sampler.SamplerFactory;
import org.arcanum.trapdoor.mp12.utils.MP12P2Utils;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2OneWayFunctionParameters extends ElementKeyParameter {

    protected MP12HLP2PublicKeyParameters pk;
    protected Sampler<BigInteger> sampler;
    protected Field inputField, outputField;


    public MP12HLP2OneWayFunctionParameters(MP12HLP2PublicKeyParameters pk) {
        super(false);

        this.pk = pk;
        this.sampler = SamplerFactory.getInstance().getDiscreteGaussianSampler(
                pk.getParameters().getRandom(),
                MP12P2Utils.getLWENoiseParameter(
                        pk.getParameters().getN(),
                        pk.getPrimitiveLatticPk().getRandomizedRoundingParameter()
                )
        );

        this.inputField = new VectorField<Field>(
                pk.getParameters().getRandom(),
                pk.getPrimitiveLatticPk().getZq(),
                pk.getParameters().getN()
        );
        this.outputField = new VectorField<Field>(
                pk.getParameters().getRandom(),
                pk.getPrimitiveLatticPk().getZq(),
                pk.getM()
        );
    }


    public MP12HLP2PublicKeyParameters getPk() {
        return pk;
    }

    public Sampler<BigInteger> getSampler() {
        return sampler;
    }

    public Field getInputField() {
        return inputField;
    }

    public Field getOutputField() {
        return outputField;
    }
}
