package org.arcanum.field.vector;

import org.arcanum.Element;
import org.arcanum.Matrix;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class TwoByColumnMatrixElement<E extends Element> extends AbstractMatrixElement<E, MatrixField> {

    protected AbstractMatrixElement A, B;
    protected int Am;


    public TwoByColumnMatrixElement(MatrixField field, AbstractMatrixElement A, AbstractMatrixElement B) {
        super(field);

        this.A = A;
        this.B = B;

        this.Am = A.getM();
    }

    
    public E getAt(int row, int col) {
        if (col < Am)
            return (E) A.getAt(row, col);
        else
            return (E) B.getAt(row, col - Am);
    }

    public final Matrix<E> setAt(int row, int col, E e) {
        getAt(row, col).set(e);

        return this;
    }

    public Matrix<E> setZeroAt(int row, int col) {
        getAt(row, col).setToZero();

        return this;
    }

    public Matrix<E> setOneAt(int row, int col) {
        getAt(row, col).setToOne();

        return this;
    }

    public boolean isZeroAt(int row, int col) {
        if (col < Am)
            return A.isZeroAt(row, col);
        else
            return B.isZeroAt(row, col - Am);
    }

    public MatrixField getField() {
        return super.getField();
    }

    public boolean equals(Object obj) {
        if (obj instanceof TwoByColumnMatrixElement)
            return isEqual((Element) obj);
        return super.equals(obj);
    }

}