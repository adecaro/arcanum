package org.arcanum.fe.abe.bns14.params;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.common.fe.params.KeyParameters;
import org.arcanum.field.vector.VectorField;
import org.arcanum.trapdoor.mp12.params.MP12HLPublicKeyParameters;
import org.arcanum.trapdoor.mp12.params.MP12PLPublicKeyParameters;
import org.arcanum.trapdoor.mp12.utils.MP12EngineFactory;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BNS14PublicKeyParameters extends KeyParameters<BNS14Parameters> {

    private MP12EngineFactory factory;

    private Element D;
    private Element[] Bs;


    public BNS14PublicKeyParameters(BNS14Parameters parameters,
                                    Element D, Element[] Bs) {
        super(false, parameters);

        this.factory = parameters.getFactory();

        this.D = D;
        this.Bs = Bs;
    }


    public MP12HLPublicKeyParameters getLatticePk() {
        return factory.getLatticePk();
    }

    public MP12PLPublicKeyParameters getPrimitiveLatticePk() {
        return factory.getPrimitiveLatticePk();
    }


    public Element getD() {
        return D;
    }

    public Element getBAt(int index) {
        return Bs[index];
    }

    public MP12EngineFactory getFactory() {
        return factory;
    }


    public Field getSecretField() {
        return factory.getSecretField();
    }

    public VectorField getRandomnessField() {
        return factory.getRandomnessField();
    }

    public Element sampleError() {
        return factory.getRandomnessField().newElementFromSampler(
                getParameters().getChi()
        );
//        return randomnessField.newZeroElement();
    }

    public Element sampleUniformOneMinusOneMarix() {
        return factory.getOneMinusOneField().newElementFromSampler(getParameters().getUniformOneMinusOne());
    }

    public int getKeyLengthInBytes() {
        return factory.getKeyLengthInBytes();
    }
}
