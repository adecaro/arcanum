package org.arcanum.trapdoor.mp12.params;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.Vector;
import org.arcanum.field.vector.VectorField;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2PublicKeyParameters extends MP12PLPublicKeyParameters {

    protected Element A;
    protected int m, mInBytes;


    public MP12HLP2PublicKeyParameters(MP12Parameters parameters,
                                       int k, int m,
                                       Vector g, Matrix G,
                                       Field syndromeField, Field Zq,
                                       VectorField<Field> preimageField,
                                       Element A) {
        super(parameters, k, g, G, syndromeField, Zq, preimageField);

        this.A = A;
        this.m = m;
        this.mInBytes = (m + 7) / 8;
    }

    public Element getA() {
        return A;
    }

    public int getM() {
        return m;
    }

    public int getmInBytes() {
        return mInBytes;
    }
}
