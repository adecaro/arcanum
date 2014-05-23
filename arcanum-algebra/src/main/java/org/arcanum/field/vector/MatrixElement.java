package org.arcanum.field.vector;

import org.arcanum.*;
import org.arcanum.util.concurrent.PoolExecutor;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class MatrixElement<E extends Element> extends AbstracArraytMatrixElement<E, MatrixField> implements Matrix<E> {

    public MatrixElement(MatrixField field) {
        super(field);

        this.matrix = new Element[field.n][field.m];
        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                matrix[i][j] = field.getTargetField().newElement();
            }
        }
    }

    public MatrixElement(MatrixElement e) {
        super(e.getField());

        this.matrix = new Element[field.n][field.m];
        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                matrix[i][j] = e.getAt(i, j).duplicate();
            }
        }
    }

    public MatrixElement(MatrixField field, Sampler<BigInteger> sampler) {
        super(field);

        this.matrix = new Element[field.n][field.m];
        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                matrix[i][j] = field.getTargetField().newElement(sampler.sample());
            }
        }
    }


    public boolean isSqr() {
        return false;
    }

    public MatrixField getField() {
        return super.getField();
    }

    public MatrixElement<E> duplicate() {
        return new MatrixElement<E>(this);
    }

    public Element getImmutable() {
//        return new ImmutableVectorElement<E>(this);
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public MatrixElement<E> setToRandom() {
        for (int i = 0; i < field.n; i++)
            for (int j = 0; j < field.m; j++)
                matrix[i][j].setToRandom();

        return this;
    }

    public MatrixElement<E> negate() {
        for (int i = 0; i < field.n; i++)
            for (int j = 0; j < field.m; j++)
                matrix[i][j].negate();

        return this;
    }

    public MatrixElement<E> sub(Element e) {
        MatrixElement m = (MatrixElement) e;

        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                matrix[i][j].sub(m.getAt(i, j));
            }
        }

        return this;
    }

    public MatrixElement<E> add(Element e) {
        AbstractMatrixElement m = (AbstractMatrixElement) e;

        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                if (!m.isZeroAt(i, j))
                    matrix[i][j].add(m.getAt(i, j));
            }
        }

        return this;
    }

    public Element div(Element e) {
        if (field.getTargetField().equals(e.getField())) {
            for (int i = 0; i < field.n; i++) {
                for (int j = 0; j < field.m; j++) {
                    matrix[i][j].div(e);
                }
            }

            return this;
        } else
            throw new IllegalArgumentException("Not implemented yet!!!");
    }

    public MatrixElement<E> mul(int z) {
        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                matrix[i][j].mul(z);
            }
        }

        return this;
    }

    public int setFromBytes(byte[] source) {
        return setFromBytes(source, 0);
    }

    public boolean equals(Object obj) {
        if (obj instanceof MatrixElement)
            return isEqual((Element) obj);
        return super.equals(obj);
    }

    public Vector<E> rowAt(int row) {
        VectorField<Field> f = new VectorField<Field>(field.getRandom(), field.getTargetField(), field.m);
        VectorElement r = f.newElement();

        for (int i = 0; i < f.n; i++) {
            r.getAt(i).set(matrix[row][i]);
        }

        return r;
    }

    public Vector<E> columnAt(int col) {
        VectorField<Field> f = new VectorField<Field>(field.getRandom(), field.getTargetField(), field.n);
        VectorElement r = f.newElement();

        for (int i = 0; i < f.n; i++) {
            r.getAt(i).set(matrix[i][col]);
        }

        return r;
    }

    public MatrixElement<E> setRowAt(int row, Element rowElement) {
        Vector r = (Vector) rowElement;

        for (int i = 0; i < field.m; i++) {
            matrix[row][i].set(r.getAt(i));
        }

        return this;
    }

    public MatrixElement<E> setColAt(int col, Element colElement) {
        Vector r = (Vector) colElement;

        for (int i = 0; i < field.n; i++) {
            matrix[i][col].set(r.getAt(i));
        }

        return this;
    }

    public Matrix<E> setSubMatrixToIdentityAt(int row, int col, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j)
                    matrix[row + i][col + j].setToOne();
                else
                    matrix[row + i][col + j].setToZero();
            }
        }

        return this;
    }

    public Matrix<E> setSubMatrixToIdentityAt(int row, int col, int n, Element e) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j)
                    matrix[row + i][col + j].set(e);
                else
                    matrix[row + i][col + j].setToZero();
            }
        }

        return this;
    }


    public Matrix<E> setSubMatrixFromMatrixAt(int row, int col, Element e) {
        // TODO: check the lengths

        Matrix eMatrix = (Matrix) e;
        for (int i = 0, n = eMatrix.getN(); i < n; i++) {
            for (int j = 0, m = eMatrix.getM(); j < m; j++) {
                matrix[row + i][col + j].set(eMatrix.getAt(i, j));
            }
        }

        return this;
    }

    public Matrix<E> setSubMatrixFromMatrixAt(final int row, final int col, Element e, final Transformer transformer) {
        // TODO: check the lengths

        final Matrix eMatrix = (Matrix) e;

        PoolExecutor pool = new PoolExecutor();
        for (int i = 0, n = eMatrix.getN(); i < n; i++) {

            final int finalI = i;
            pool.submit(new Runnable() {
                public void run() {
                    for (int j = 0, m = eMatrix.getM(); j < m; j++) {
                        matrix[row + finalI][col + j].set(eMatrix.getAt(finalI, j));
                        transformer.transform(row + finalI, col + j, matrix[row + finalI][col + j]);
                    }
                }
            });

        }
        pool.awaitTermination();

        return this;
    }


    public Matrix<E> setSubMatrixFromMatrixTransposeAt(int row, int col, Element e) {
        // TODO: check the lengths

        Matrix eMatrix = (Matrix) e;
        for (int i = 0, n = eMatrix.getN(); i < n; i++) {
            for (int j = 0, m = eMatrix.getM(); j < m; j++) {
                matrix[row + i][col + j].set(eMatrix.getAt(j, i));
            }
        }

        return this;
    }

    public Matrix<E> transform(Transformer transformer) {
        for (int i = 0; i < field.n; i++)
            for (int j = 0; j < field.m; j++)
                transformer.transform(i, j, matrix[i][j]);

        return this;
    }

    public boolean isSymmetric() {
        int n = matrix.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (!matrix[i][j].equals(matrix[j][i]))
                    return false;
            }
        }

        return true;
    }

    public boolean isSquare() {
        return field.n ==  field.m;
    }

    public boolean isZeroAt(int row, int col) {
        return matrix[row][col].isZero();
    }


}