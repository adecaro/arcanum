package org.arcanum.trapdoor.mp12.params;

import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.Vector;
import org.arcanum.field.vector.VectorField;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12PLP2PublicKeyParameters extends MP12PLPublicKeyParameters {

    protected int k;


    public MP12PLP2PublicKeyParameters(MP12Parameters parameters,
                                       int k,
                                       Vector g,
                                       Matrix G,
                                       Field syndromeField,
                                       Field Zq,
                                       VectorField<Field> preimageField) {
        super(parameters, g, G, syndromeField, Zq, preimageField);

        this.k = k;
    }

    public int getK() {
        return k;
    }

}
