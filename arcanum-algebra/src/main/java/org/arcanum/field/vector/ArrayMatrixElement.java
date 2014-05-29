package org.arcanum.field.vector;

import org.arcanum.Element;
import org.arcanum.Matrix;
import org.arcanum.Sampler;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ArrayMatrixElement<E extends Element> extends AbstractMatrixElement<E, MatrixField> {

    protected Element[][] matrix;


    public ArrayMatrixElement(MatrixField field) {
        super(field);

        this.matrix = new Element[field.n][field.m];
        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                matrix[i][j] = field.getTargetField().newElement();
            }
        }
    }

    public ArrayMatrixElement(ArrayMatrixElement e) {
        super(e.getField());

        this.matrix = new Element[field.n][field.m];
        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                matrix[i][j] = e.getAt(i, j).duplicate();
            }
        }
    }

    public ArrayMatrixElement(MatrixField field, Sampler<BigInteger> sampler) {
        super(field);

        this.matrix = new Element[field.n][field.m];
        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                matrix[i][j] = field.getTargetField().newElement(sampler.sample());
            }
        }
    }


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

    public Matrix<E> setZeroAt(int row, int col) {
        matrix[row][col].setToZero();

        return this;
    }

    public Matrix<E> setOneAt(int row, int col) {
        matrix[row][col].setToOne();

        return this;
    }

    public boolean isZeroAt(int row, int col) {
        return matrix[row][col].isZero();
    }

    public boolean isSymmetric() {
        // TODO: generalize
        int n = matrix.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (!matrix[i][j].equals(matrix[j][i]))
                    return false;
            }
        }

        return true;
    }

    public MatrixField getField() {
        return super.getField();
    }

    public ArrayMatrixElement duplicate() {
        return new ArrayMatrixElement(this);
    }

    public ArrayMatrixElement getImmutable() {
        return new ImmutableArrayMatrixElement<E>(this);
    }

    public boolean equals(Object obj) {
        if (obj instanceof ArrayMatrixElement)
            return isEqual((Element) obj);
        return super.equals(obj);
    }

}