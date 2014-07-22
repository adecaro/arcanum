package org.arcanum.field.vector;

import org.arcanum.Element;
import org.arcanum.Matrix;
import org.arcanum.Sampler;
import org.arcanum.Vector;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ArrayMatrixElement<E extends Element, F extends AbstractMatrixField> extends AbstractMatrixElement<E, F> {

    protected Element[][] matrix;


    public ArrayMatrixElement(F field) {
        super(field);

        this.matrix = new Element[field.n][field.m];
//        PoolExecutor pool = new PoolExecutor();
//        for (int i = 0; i < field.n; i++) {
//
//            final int finalI1 = i;
//            pool.submit(new Runnable() {
//                public void run() {
//                    for (int j = 0; j < ArrayMatrixElement.this.field.m; j++) {
//                        matrix[finalI1][j] = ArrayMatrixElement.this.getTargetField().newElement();
//                    }
//                }
//            });
//
//        }
//        pool.awaitTermination();
//        System.out.println("ArrayMatrixElement.ArrayMatrixElement");


//        this.matrix = new Element[field.n][field.m];
//        for (int i = 0; i < field.n; i++) {
//            for (int j = 0; j < field.m; j++) {
//                matrix[i][j] = field.getTargetField().newElement();
//            }
//        }
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

    public ArrayMatrixElement(F field, Vector<E> diagonal) {
        this(field);

        for (int i = 0; i < field.n; i++) {

            int offset = i * diagonal.getSize();
            for (int j = 0, s = diagonal.getSize(); j < s; j++) {
                getAt(i, offset + j).set(diagonal.getAt(j));
            }

        }
    }


    public ArrayMatrixElement duplicate() {
        return new ArrayMatrixElement(this);
    }

    public ArrayMatrixElement getImmutable() {
        return new ImmutableArrayMatrixElement(this);
    }


    public E getAt(int row, int col) {
        if (matrix[row][col] == null)
            matrix[row][col] = field.getTargetField().newElement();

        return (E) matrix[row][col];
    }

    public final Matrix<E> setAt(int row, int col, E e) {
        if (matrix[row][col] == null)
            matrix[row][col] = field.getTargetField().newElement();

        matrix[row][col].set(e);

        return this;
    }

    public Matrix<E> setZeroAt(int row, int col) {
        if (matrix[row][col] == null)
            matrix[row][col] = field.getTargetField().newElement();

        matrix[row][col].setToZero();

        return this;
    }

    public Matrix<E> setOneAt(int row, int col) {
        if (matrix[row][col] == null)
            matrix[row][col] = field.getTargetField().newElement();

        matrix[row][col].setToOne();

        return this;
    }

    public boolean isZeroAt(int row, int col) {
        if (row >= field.n || col >= field.m)
            return true;

        if (matrix[row][col] == null)
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