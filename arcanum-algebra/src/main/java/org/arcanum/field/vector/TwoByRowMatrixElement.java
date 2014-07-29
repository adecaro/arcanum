package org.arcanum.field.vector;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Matrix;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class TwoByRowMatrixElement<E extends Element> extends AbstractMatrixElement<E, MatrixField> {

    protected Matrix A, B;
    protected int An;


    public TwoByRowMatrixElement(MatrixField field, Matrix A, Matrix B) {
        super(field);

        this.A = A;
        this.B = B;

        this.An = A.getN();
    }

    public TwoByRowMatrixElement(Matrix A, Matrix B) {
        super(new MatrixField<Field<E>>(((AbstractMatrixElement)A).getField().getRandom(), A.getTargetField(), A.getN() + B.getN(), A.getM()));

        this.A = A;
        this.B = B;

        this.An = A.getN();
    }

    public final E getAt(int row, int col) {
        if (row < An)
            return (E) A.getAt(row, col);
        else
            return (E) B.getAt(row - An, col);
    }

    public final boolean isZeroAt(int row, int col) {
        if (row < An)
            return A.isZeroAt(row, col);
        else
            return B.isZeroAt(row - An, col);
    }

}