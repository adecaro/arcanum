package org.arcanum.field.vector;


import org.arcanum.*;

import java.math.BigInteger;
import java.security.SecureRandom;

import static org.arcanum.Matrix.Transformer;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class MatrixField<F extends Field> extends AbstractMatrixField<F, AbstractMatrixElement> {


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

    public int getLengthInBytes() {
        return lenInBytes;
    }


    public Matrix newElementIdentity() {
        // TODO: use DiagonalMatrixElement
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

    public Matrix newTwoByTwoElement(Matrix A, Matrix B, Matrix C, Matrix D) {
        // TODO: check field dimension
        return new TwoByTwoMatrixElement(this,
                A, B,
                C, D
        );
    }

}
