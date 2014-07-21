package org.arcanum.field.vector;

import org.arcanum.Element;
import org.arcanum.Matrix;
import org.arcanum.util.io.disk.MatrixSector;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class DiskMatrixElement<E extends Element, F extends AbstractMatrixField> extends AbstractMatrixElement<E, F> {

    protected MatrixSector<E> matrix;


    public DiskMatrixElement(F field) {
        super(field);

    }



    public E getAt(int row, int col) {
        return matrix.getAt(row, col);
    }

    public final Matrix<E> setAt(int row, int col, E e) {
        matrix.getAt(row, col).set(e);

        return this;
    }

    public Matrix<E> setZeroAt(int row, int col) {
        matrix.getAt(row, col).setToZero();

        return this;
    }

    public Matrix<E> setOneAt(int row, int col) {
        matrix.getAt(row, col).setToOne();

        return this;
    }

    public boolean isZeroAt(int row, int col) {
        if (row >= field.n || col >= field.m)
            return true;

        return matrix.getAt(row, col).isZero();
    }

    public F getField() {
        return super.getField();
    }

    public boolean equals(Object obj) {
        if (obj instanceof DiskMatrixElement)
            return isEqual((Element) obj);
        return super.equals(obj);
    }

}