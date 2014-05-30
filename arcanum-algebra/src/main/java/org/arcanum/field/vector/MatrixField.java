package org.arcanum.field.vector;


import org.arcanum.*;

import java.math.BigInteger;
import java.security.SecureRandom;

import static org.arcanum.Matrix.Transformer;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class MatrixField<F extends Field> extends AbstractMatrixField<F, AbstractMatrixElement> {


    public static Matrix newElementFromSampler(MatrixField field, int n, int m, Sampler<BigInteger> sampler) {
        return new MatrixField<Field>(field.getRandom(), field.getTargetField(), n, m).newElementFromSampler(sampler);
    }

    public static Matrix unionByCol(Element a, Element b) {
        AbstractMatrixElement a1 = (AbstractMatrixElement) a;
        AbstractMatrixElement b1 = (AbstractMatrixElement) b;

        MatrixField f = new MatrixField<Field>(
                a1.getField().getRandom(), a1.getTargetField(),
                a1.getField().n,
                a1.getField().m + b1.getField().m);

        Matrix c = f.newElement();
        for (int i = 0; i < f.n; i++) {
            for (int j = 0; j < a1.getField().m; j++) {
                if (a1.isZeroAt(i, j))
                    c.setZeroAt(i, j);
                else
                    c.getAt(i, j).set(a1.getAt(i, j));
            }

            for (int j = 0; j < b1.getField().m; j++) {
                if (b1.isZeroAt(i, j))
                    c.setZeroAt(i, a1.getField().m + j);
                else
                    c.getAt(i, a1.getField().m + j).set(b1.getAt(i, j));
            }
        }


        return c;

    }

    public static Matrix unionByRow(Element a, Element b) {
        AbstractMatrixElement a1 = (AbstractMatrixElement) a;
        AbstractMatrixElement b1 = (AbstractMatrixElement) b;

        MatrixField f = new MatrixField<Field>(
                a1.getField().getRandom(), a1.getTargetField(),
                a1.getField().n + b1.getField().n,
                a1.getField().m
        );

        Matrix c = f.newElement();
        for (int i = 0; i < f.m; i++) {

            for (int j = 0; j < a1.getField().n; j++) {
                if (a1.isZeroAt(j, i))
                    c.setZeroAt(j, i);
                else
                    c.getAt(j, i).set(a1.getAt(j, i));
            }

            for (int j = 0; j < b1.getField().n; j++) {
                if (b1.isZeroAt(j, i))
                    c.setZeroAt(a1.getField().n + j, i);
                else
                    c.getAt(a1.getField().n + j, i).set(b1.getAt(j, i));
            }
        }


        return c;

    }


    protected final int lenInBytes;


    public MatrixField(SecureRandom random, F targetField, int n, int m) {
        super(random, targetField, n, m);

        this.lenInBytes = n * m * targetField.getLengthInBytes();
    }

    public MatrixField(SecureRandom random, F targetField, int n) {
        super(random, targetField, n, n);

        this.lenInBytes = n * n * targetField.getLengthInBytes();
    }


    public AbstractMatrixElement newElement() {
        return new ArrayMatrixElement(this);
    }

    public AbstractMatrixElement newElementFromSampler(Sampler<BigInteger> sampler) {
        return new ArrayMatrixElement(this, sampler);
    }

    public Matrix newElementIdentity() {
        Matrix m = newElement();
        for (int i = 0; i < n; i++)
            m.getAt(i, i).setToOne();
        return m;
    }

    public Matrix newElementIdentity(Element g) {
        if (g instanceof Vector) {
            return new DiagonalMatrixElement(this, (Vector) g);

            /*VectorElement vg = (VectorElement) g;

            int col = 0;
            for (int row = 0; row < n; row++) {

                for (int k = 0; k < vg.getSize(); k++) {
                    r.getAt(row, col).set(vg.getAt(k));
                    col += 1;
                }
            }*/
        }

        throw new IllegalArgumentException("Not implemented yet!!!");
    }


    public Matrix newIdentityMatrix(int n, Element element) {
        return new IdentityMatrixElement<Element>(new MatrixField<F>(random, targetField, n), element);
    }

    public Matrix newNullMatrix(int n, int m) {
        return new ZeroMatrixElement<Element>(new MatrixField<F>(random, targetField, n, m));
    }

    public Matrix newSquareMatrix(int n) {
        return new ArrayMatrixElement(new MatrixField<F>(random, targetField, n));
    }

    public Matrix newSquareMatrix(int n, Matrix matrix, Transformer transformer) {
        return new ArrayMatrixElement(
                new MatrixField<F>(random, targetField, n),
                matrix,
                transformer
        );
    }

    public Matrix newMatrix(int n, int m, Matrix matrix, Transformer transformer) {
        return new ArrayMatrixElement(
                new MatrixField<F>(random, targetField, n, m),
                matrix,
                transformer
        );
    }

    public Matrix newMatrix(Matrix matrix, Transformer transformer) {
        return new ArrayMatrixElement(
                new MatrixField<F>(random, targetField, matrix.getN(), matrix.getM()),
                matrix,
                transformer);
    }


    public Matrix newTwoByColMatrix(Element A, Element B) {
        // TODO: check field dimension
        return new TwoByColumnMatrixElement(
                this,
                (AbstractMatrixElement) A, (AbstractMatrixElement) B
        );
    }

    public Matrix newTwoByRowMatrix(Matrix A, Matrix B) {
        // TODO: check field dimension
        return new TwoByRowMatrixElement(
                new MatrixField<F>(random, targetField, A.getN() + B.getN(), A.getM()),
                (AbstractMatrixElement) A,
                (AbstractMatrixElement) B);
    }

    public Matrix newTwoByTwoElement(Element A, Element B, Element C, Element D) {
        // TODO: check field dimension
        return new TwoByTwoMatrixElement(this,
                (AbstractMatrixElement) A, (AbstractMatrixElement) B,
                (AbstractMatrixElement) C, (AbstractMatrixElement) D
        );
    }


    public int getLengthInBytes() {
        return lenInBytes;
    }

}
