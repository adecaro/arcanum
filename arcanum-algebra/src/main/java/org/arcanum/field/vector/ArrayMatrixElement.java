package org.arcanum.field.vector;

import org.arcanum.Element;
import org.arcanum.Matrix;
import org.arcanum.Sampler;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ArrayMatrixElement<E extends Element, F extends AbstractMatrixField> extends AbstractMatrixElement<E, F> {

    protected Element[][] matrix;


    public ArrayMatrixElement(F field) {
        super(field);

        this.matrix = new Element[field.n][field.m];
        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                matrix[i][j] = field.getTargetField().newElement();
            }
        }
    }

    public ArrayMatrixElement(F field, Element[][] matrix) {
        super(field);

        this.matrix = matrix;
    }

    public ArrayMatrixElement(ArrayMatrixElement<E, F> e) {
        super(e.getField());

        this.matrix = new Element[field.n][field.m];
        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                matrix[i][j] = e.getAt(i, j).duplicate();
            }
        }
    }

    public ArrayMatrixElement(F field, Sampler<BigInteger> sampler) {
        super((F) field);

        this.matrix = new Element[field.n][field.m];
        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                matrix[i][j] = field.getTargetField().newElement(sampler.sample());
            }
        }
    }

    public ArrayMatrixElement(F field, Matrix m, Transformer transformer) {
        super((F) field);

        this.matrix = new Element[field.n][field.m];
        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {

                if (m.isZeroAt(i, j))
                    matrix[i][j] = getTargetField().newZeroElement();
                else
                    matrix[i][j] = getTargetField().newElement(m.getAt(i, j));

                transformer.transform(i, j, matrix[i][j]);
            }
        }
    }


    public ArrayMatrixElement duplicate() {
        return new ArrayMatrixElement(this);
    }

    public ArrayMatrixElement getImmutable() {
        return new ImmutableArrayMatrixElement(this);
    }


    public final E getAt(int row, int col) {
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
        if (row >= field.n || col >= field.m)
            return true;

        return matrix[row][col].isZero();
    }

    public F getField() {
        return super.getField();
    }

    public boolean equals(Object obj) {
        if (obj instanceof ArrayMatrixElement)
            return isEqual((Element) obj);
        return super.equals(obj);
    }

}