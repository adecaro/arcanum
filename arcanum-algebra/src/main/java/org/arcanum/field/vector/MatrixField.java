package org.arcanum.field.vector;


import org.arcanum.*;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class MatrixField<F extends Field> extends AbstractMatrixField<F, MatrixElement> {


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

        MatrixElement c = f.newElement();
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

        MatrixElement c = f.newElement();
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


    public MatrixElement newElement() {
        return new MatrixElement(this);
    }

    public BigInteger getOrder() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public int getLengthInBytes() {
        return lenInBytes;
    }

    public MatrixElement getNqr() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public MatrixElement newElementFromSampler(Sampler<BigInteger> sampler) {
        return new MatrixElement(this, sampler);
    }

    public MatrixElement newIdentity() {
        MatrixElement m = newElement();
        for (int i = 0; i < n; i++)
            m.getAt(i, i).setToOne();
        return m;
    }

    public Matrix newDiagonalElement(Element g) {
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

}
