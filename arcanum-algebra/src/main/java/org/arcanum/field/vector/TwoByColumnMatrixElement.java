package org.arcanum.field.vector;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Matrix;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class TwoByColumnMatrixElement<E extends Element> extends AbstractMatrixElement<E, MatrixField> {

    protected Matrix A, B;
    protected int Am;


    public TwoByColumnMatrixElement(MatrixField field, Matrix A, Matrix B) {
        super(field);

        this.A = A;
        this.B = B;

        this.Am = A.getM();
    }

    public TwoByColumnMatrixElement(Matrix A, Matrix B) {
        super(new MatrixField<Field>(
                ((AbstractMatrixField)A.getField()).getRandom(), A.getTargetField(),
                A.getN(),
                A.getM() + B.getM()));

        this.A = A;
        this.B = B;

        this.Am = A.getM();
    }


    @Override
    public ArrayMatrixElement duplicate() {
        return new ArrayMatrixElement(field, this);
    }

    @Override
    public boolean isSparse() {
        // TODO: verify this condition
        return A.isSparse() || B.isSparse();
    }

    public final E getAt(int row, int col) {
        if (col < Am)
            return (E) A.getAt(row, col);
        else
            return (E) B.getAt(row, col - Am);
    }

    public final boolean isZeroAt(int row, int col) {
        if (col < Am)
            return A.isZeroAt(row, col);
        else
            return B.isZeroAt(row, col - Am);
    }

    public boolean equals(Object obj) {
        if (obj instanceof TwoByColumnMatrixElement)
            return isEqual((Element) obj);
        return super.equals(obj);
    }

}