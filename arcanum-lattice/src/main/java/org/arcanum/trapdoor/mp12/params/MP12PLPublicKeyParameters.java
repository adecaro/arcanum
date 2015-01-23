package org.arcanum.trapdoor.mp12.params;

import org.apfloat.Apfloat;
import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.Vector;
import org.arcanum.field.vector.VectorField;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class MP12PLPublicKeyParameters extends MP12KeyParameters {

    protected Vector g;
    protected Matrix G; // parity-check matrix

    protected Field syndromeField;
    protected Field Zq;
    protected VectorField<Field> preimageField;


    public MP12PLPublicKeyParameters(MP12Parameters parameters,
                                     Vector g,
                                     Matrix G,
                                     Field syndromeField,
                                     Field Zq,
                                     VectorField<Field> preimageField) {
        super(false, parameters);

        this.g = g;
        this.G = G;
        this.syndromeField = syndromeField;
        this.Zq = Zq;
        this.preimageField = preimageField;
    }

    public Apfloat getRandomizedRoundingParameter() {
        return getParameters().getRandomizedRoundingParameter();
    }

    public Matrix getG() {
        return G;
    }

    public Vector getPrimitiveVector() {
        return g;
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
