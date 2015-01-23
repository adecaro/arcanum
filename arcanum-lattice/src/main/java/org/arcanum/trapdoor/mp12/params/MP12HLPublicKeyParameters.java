package org.arcanum.trapdoor.mp12.params;

import org.arcanum.Element;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLPublicKeyParameters extends MP12KeyParameters {

    protected MP12PLPublicKeyParameters primitiveLatticPk;
    protected Element A;
    protected int m, mInBytes;


    public MP12HLPublicKeyParameters(MP12Parameters parameters,
                                     MP12PLPublicKeyParameters primitiveLatticPk,
                                     Element A, int m) {
        super(false, parameters);

        this.primitiveLatticPk = primitiveLatticPk;
        this.A = A;
        this.m = m;
        this.mInBytes = (m + 7) / 8;
    }


    public MP12PLPublicKeyParameters getPrimitiveLatticPk() {
        return primitiveLatticPk;
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
