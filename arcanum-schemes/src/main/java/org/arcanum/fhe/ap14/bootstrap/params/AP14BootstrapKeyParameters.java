package org.arcanum.fhe.ap14.bootstrap.params;

import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.Vector;
import org.arcanum.common.cipher.params.ElementCipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AP14BootstrapKeyParameters implements ElementCipherParameters {

    private Matrix P;
    private int k;
    private Field<Vector> decompositionField;


    public AP14BootstrapKeyParameters(Matrix p, int k, Field<Vector> decompositionField) {
        P = p;
        this.k = k;
        this.decompositionField = decompositionField;
    }


    public Matrix getP() {
        return P;
    }

    public int getK() {
        return k;
    }

    public Field<Vector> getDecompositionField() {
        return decompositionField;
    }
}
