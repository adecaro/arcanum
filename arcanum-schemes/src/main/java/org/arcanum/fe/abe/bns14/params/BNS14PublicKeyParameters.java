package org.arcanum.fe.abe.bns14.params;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.field.vector.VectorField;
import org.arcanum.trapdoor.mp12.params.MP12HLP2PublicKeyParameters;
import org.arcanum.trapdoor.mp12.params.MP12PLP2PublicKeyParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BNS14PublicKeyParameters extends BNS14KeyParameters {

    private MP12HLP2PublicKeyParameters latticePk;
    private MP12PLP2PublicKeyParameters primitiveLatticePk;
    private Element D;
    private Element[] Bs;
    private VectorField secretField, randomnessField;
    private MatrixField oneMinusOneField;
    private int keyLengthInBytes;
    private Element G;


    public BNS14PublicKeyParameters(BNS14Parameters parameters,
                                    MP12HLP2PublicKeyParameters latticePk,
                                    MP12PLP2PublicKeyParameters primitiveLatticePk,
                                    Element D, Element[] Bs) {
        super(false, parameters);

        this.latticePk = latticePk;
        this.primitiveLatticePk = primitiveLatticePk;
        this.D = D;
        this.Bs = Bs;

        this.secretField = new VectorField<Field>(latticePk.getParameters().getRandom(), latticePk.getZq(), latticePk.getParameters().getN());
        this.randomnessField = new VectorField<Field>(latticePk.getParameters().getRandom(), latticePk.getZq(), latticePk.getM());
        this.oneMinusOneField = new MatrixField<Field>(latticePk.getParameters().getRandom(), latticePk.getZq(), latticePk.getM());
    }

    public MP12HLP2PublicKeyParameters getLatticePk() {
        return latticePk;
    }

    public MP12PLP2PublicKeyParameters getPrimitiveLatticePk() {
        return primitiveLatticePk;
    }

    public Element getD() {
        return D;
    }

    public Element getBAt(int index) {
        return Bs[index];
    }

    public Field getSecretField() {
        return secretField;
    }

    public VectorField getRandomnessField() {
        return randomnessField;
    }

    public Element sampleError() {
        return randomnessField.newElementFromSampler(
                getParameters().getChi()
        );
//        return randomnessField.newZeroElement();
    }

    public Element sampleUniformOneMinusOneMarix() {
        return oneMinusOneField.newElementFromSampler(getParameters().getUniformOneMinusOne());
    }

    public int getKeyLengthInBytes() {
        return latticePk.getmInBytes();
    }

}
