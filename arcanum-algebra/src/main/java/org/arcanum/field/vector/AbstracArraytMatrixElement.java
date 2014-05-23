package org.arcanum.field.vector;

import org.arcanum.Element;
import org.arcanum.Matrix;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class AbstracArraytMatrixElement<E extends Element, F extends AbstractMatrixField> extends AbstractMatrixElement<E, F> {

    protected Element[][] matrix;


    protected AbstracArraytMatrixElement(F field) {
        super(field);
    }


    @Override
    public byte[] toBytes() {
        byte[] buffer = new byte[field.getLengthInBytes()];

        int counter = 0;
        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                byte[] bytes = matrix[i][j].toBytes();

                System.arraycopy(bytes, 0, buffer, counter, bytes.length);
                counter += bytes.length;
            }
        }

        return buffer;
    }

    public E getAt(int row, int col) {
        return (E) matrix[row][col];
    }

    public final Matrix<E> setAt(int row, int col, E e) {
        matrix[row][col].set(e);

        return this;
    }

    @Override
    public Matrix<E> setZeroAt(int row, int col) {
        matrix[row][col].setToZero();

        return this;
    }

    @Override
    public Matrix<E> setOneAt(int row, int col) {
        matrix[row][col].setToOne();

        return this;
    }
}