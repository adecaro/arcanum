package org.arcanum.trapdoor.mp12.params;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.Sampler;
import org.arcanum.field.vector.VectorField;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2PublicKeyParameters extends MP12PLP2PublicKeyParameters {

    protected Element A;
    protected int m, mInBytes, barM, w;


    public MP12HLP2PublicKeyParameters(MP12Parameters parameters,
                                       int k, int m, int barM, int w,
                                       Sampler<BigInteger> sampler,
                                       Matrix G,
                                       Field syndromeField, Field Zq,
                                       VectorField<Field> preimageField,
                                       Element A) {
        super(parameters, k, G, syndromeField, Zq, preimageField);

        this.A = A;
        this.m = m;
        this.barM = barM;
        this.w = w;
        this.mInBytes = (m + 7) / 8;
    }

    public Element getA() {
        return A;
    }

    public int getM() {
        return m;
    }

    public int getBarM() {
        return barM;
    }

    public int getW() {
        return w;
    }

    public int getmInBytes() {
        return mInBytes;
    }
}
