package org.arcanum.field.vector;

import org.arcanum.*;
import org.arcanum.field.base.AbstractElement;
import org.arcanum.util.concurrent.PoolExecutor;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class AbstractMatrixElement<E extends Element, F extends AbstractMatrixField> extends AbstractElement<F> implements Matrix<E> {


    protected AbstractMatrixElement(F field) {
        super(field);
    }


    public F getField() {
        return field;
    }

    public Matrix<E> mulByTransposeTo(final Matrix matrix, final int offsetRow, final int offsetCol, final Transformer transformer) {
        PoolExecutor executor = new PoolExecutor();

        for (int i = 0; i < field.n; i++) {
            final int finalI = i;
            executor.submit(new Runnable() {
                public void run() {
                    for (int j = 0; j < field.n; j++) {
                        Element temp = field.getTargetField().newZeroElement();

                        for (int k = 0; k < field.m; k++)
                            temp.add(getAt(finalI, k).duplicate().mul(getAt(j, k)));

                        Element target = matrix.getAt(offsetRow + finalI, offsetCol + j);
                        target.set(temp);
                        transformer.transform(offsetRow + finalI, offsetCol + j, target);
                    }
                }
            });
        }

        executor.awaitTermination();

        return this;
    }

    public Element mulFromTranspose(Element e) {
        if (e instanceof Vector) {
            final Vector ve = (Vector) e;
            // Consider transpose

            VectorField f = new VectorField<Field>(field.getRandom(), field.getTargetField(), field.m);
            final VectorElement r = f.newElement();

            PoolExecutor executor = new PoolExecutor();

            for (int i = 0; i < f.n; i++) {

                final int finalI = i;
                executor.submit(new Runnable() {
                    public void run() {
                        // column \times row
                        Element temp = field.getTargetField().newElement();
                        for (int k = 0; k < field.n; k++) {
                            if (isZeroAt(k, finalI))
                                continue;

                            temp.add(getAt(k, finalI).duplicate().mul(ve.getAt(k)));
                        }
                        r.getAt(finalI).set(temp);
                    }
                });
            }
            executor.awaitTermination();

            return r;
        }  else if (e instanceof AbstractMatrixElement) {
/*            final AbstractMatrixElement me = (AbstractMatrixElement) e;

            if (field.getTargetField().equals(me.getField().getTargetField())) {
                final MatrixField f = new MatrixField<Field>(field.getRandom(), field.getTargetField(), field.m, me.getField().n);
                final MatrixElement r = f.newElement();

                PoolExecutor executor = new PoolExecutor();
                for (int i = 0; i < field.m; i++) {

                    final int finalI = i;
                    executor.submit(new Runnable() {
                        public void run() {
                            // row \times column
                            for (int j = 0; j < field.n; j++) {
                                Element temp = field.getTargetField().newElement();

                                for (int k = 0; k < field.m; k++) {
                                    if (isZeroAt(finalI, k))
                                        continue;
                                    temp.add(getAt(finalI, k).duplicate().mul(me.getAt(k, j)));
                                }

                                r.getAt(finalI, j).set(temp);
                            }
                        }
                    });
                }
                executor.awaitTermination();

                return r;
            }*/
        }

        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Matrix<E> mulByTranspose() {
        MatrixField resultField = new MatrixField<Field>(field.getRandom(), field.getTargetField(), field.n);
        Matrix result = resultField.newElement();

        for (int i = 0; i < field.n; i++) {

            for (int j = 0; j < field.n; j++) {
                Element temp = field.getTargetField().newZeroElement();

                for (int k = 0; k < field.m; k++) {
                    if (!isZeroAt(i, k))
                        temp.add(getAt(i, k).duplicate().mul(getAt(j, k)));
                }

                result.getAt(i, j).set(temp);
            }
        }

        return result;
    }


    public Field getTargetField() {
        return field.getTargetField();
    }

    public int getN() {
        return field.n;
    }

    public int getM() {
        return field.m;
    }

    public int sign() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public E getAt(int row, int col) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Matrix<E> setAt(int row, int col, E e) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Matrix<E> setZeroAt(int row, int col) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Matrix<E> setOneAt(int row, int col) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Vector<E> rowAt(int row) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Vector<E> columnAt(int col) {
        VectorField<Field> f = new VectorField<Field>(field.getRandom(), field.getTargetField(), field.n);
        VectorElement r = f.newElement();

        for (int i = 0; i < f.n; i++)
            if (!isZeroAt(i, col))
                r.getAt(i).set(getAt(i, col));

        return r;
    }

    public Matrix<E> setRowAt(int row, Element rowElement) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Matrix<E> setColAt(int col, Element colElement) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Matrix<E> setSubMatrixToIdentityAt(int row, int col, int n) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Matrix<E> setSubMatrixFromMatrixAt(int row, int col, Element e) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Matrix<E> setSubMatrixFromMatrixAt(int row, int col, Element e, Transformer transformer) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Matrix<E> setSubMatrixFromMatrixTransposeAt(int row, int col, Element e) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Matrix<E> setSubMatrixToIdentityAt(int row, int col, int n, Element e) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Matrix<E> transform(Transformer transformer) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isSymmetric() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isSquare() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isZeroAt(int row, int col) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public String toStringSubMatrix(int startRow, int startCol) {
        StringBuffer sb = new StringBuffer();
        sb.append("[\n");
        for (int i = startRow; i < field.n; i++) {

            for (int j = startCol; j < field.m; j++) {
                if (isZeroAt(i, j))
                    sb.append(String.format("%10s", "0"));
                else
                    sb.append(String.format("%10s", getAt(i, j)));
                if (j != field.m - 1)
                    sb.append(",");
            }

            if (i != field.n - 1)
                sb.append(";\n");
        }
        sb.append("]\n");

        return "MatrixElement{" +
                "matrix=\n" + sb.toString() +
                '}';
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[\n");
        for (int i = 0; i < field.n; i++) {

            for (int j = 0; j < field.m; j++) {
                if (isZeroAt(i, j))
                    sb.append(String.format("%10s", "0"));
                else
                    sb.append(String.format("%10s", getAt(i, j)));
                if (j != field.m - 1)
                    sb.append(",");
            }

            if (i != field.n - 1)
                sb.append(";\n");
        }
        sb.append("]\n");

        return "DiagonalMatrixElement{" +
                "matrix=\n" + sb.toString() +
                '}';
    }

    public Element duplicate() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element set(Element value) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element set(int value) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element set(BigInteger value) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public BigInteger toBigInteger() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element setToRandom() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element setFromHash(byte[] source, int offset, int length) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element setToZero() {
        for (int i = 0; i < field.n; i++) {

            for (int j = 0; j < field.m; j++) {
                if (!isZeroAt(i, j))
                    setZeroAt(i, j);
            }
        }

        return this;
    }

    public boolean isZero() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element setToOne() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isEqual(Element e) {
        AbstractMatrixElement element = (AbstractMatrixElement) e;

        if (field.n != element.getField().n)
            return false;
        if (field.m != element.getField().m)
            return false;

        for (int i = 0; i < field.n; i++)
            for (int j = 0; j < field.m; j++)
                if (element.isZeroAt(i, j)) {
                    if (!isZeroAt(i, j))
                        return false;
                } else if (!getAt(i, j).isEqual(element.getAt(i, j)))
                    return false;

        return true;
    }

    public boolean isOne() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element invert() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element negate() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element add(Element element) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element mul(BigInteger n) {
        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                if (!isZeroAt(i, j))
                    getAt(i, j).mul(n);
            }
        }

        return this;
    }

    public boolean isSqr() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element mul(Element e) {
        if (field.getTargetField().equals(e.getField())) {
            Element result = e.duplicate();
            if (field.n == 1) {
                for (int j = 0; j < field.m; j++) {
                    result.add(getAt(0, j));
                }
            } else if (field.m == 1) {
                for (int i = 0; i < field.n; i++) {
                    result.add(getAt(i, 0));
                }
            } else {
                for (int i = 0; i < field.n; i++) {
                    for (int j = 0; j < field.m; j++) {
                        if (!isZeroAt(i, j))
                            getAt(i, j).mul(e);
                    }
                }
                return this;
            }

            return result;
        } else if (e instanceof Vector) {
            final Vector ve = (Vector) e;

            if (field.getTargetField().equals(((FieldOver) ve.getField()).getTargetField())) {
                if (ve.getSize() == 1) {
                    Element result = ve.getAt(0).duplicate();
                    if (field.n == 1) {
                        for (int j = 0; j < field.m; j++) {
                            result.add(getAt(0, j));
                        }
                    } else if (field.m == 1) {
                        for (int i = 0; i < field.n; i++) {
                            result.add(getAt(i, 0));
                        }
                    } else
                        throw new IllegalArgumentException("Cannot multiply this way.");

                    return result;
                } else {
                    // Check dimensions

                    if (ve.getSize() == field.m) {
                        VectorField f = new VectorField(field.getRandom(), field.getTargetField(), field.n);
                        final VectorElement r = f.newElement();

                        PoolExecutor executor = new PoolExecutor();

                        for (int i = 0; i < f.n; i++) {

                            // row \times column

                            final int finalI = i;
                            executor.submit(new Runnable() {
                                public void run() {
                                    for (int j = 0; j < 1; j++) {
                                        Element temp = field.getTargetField().newElement();
                                        for (int k = 0; k < field.m; k++) {
                                            if (isZeroAt(finalI, k))
                                                continue;

                                            temp.add(getAt(finalI, k).duplicate().mul(ve.getAt(k)));
                                        }
                                        r.getAt(finalI).set(temp);
                                    }
                                }
                            });
                        }
                        executor.awaitTermination();

                        return r;
                    } else if (ve.getSize() == field.n) {
                        // Consider transpose

                        VectorField f = new VectorField(field.getRandom(), field.getTargetField(), field.m);
                        final VectorElement r = f.newElement();

                        PoolExecutor executor = new PoolExecutor();

                        for (int i = 0; i < f.n; i++) {

                            final int finalI = i;
                            executor.submit(new Runnable() {
                                public void run() {
                                    // column \times row
                                    Element temp = field.getTargetField().newElement();
                                    for (int k = 0; k < field.n; k++) {
                                        if (isZeroAt(k, finalI))
                                            continue;

                                        temp.add(getAt(k, finalI).duplicate().mul(ve.getAt(k)));
                                    }
                                    r.getAt(finalI).set(temp);
                                }
                            });
                        }
                        executor.awaitTermination();

                        return r;
                    }
                }
            }
            throw new IllegalStateException("Not Implemented yet!!!");
        } else if (e instanceof MatrixElement) {
            final MatrixElement me = (MatrixElement) e;

            if (field.getTargetField().equals(me.getField().getTargetField())) {
                final MatrixField f = new MatrixField<Field>(field.getRandom(), field.getTargetField(), field.n, me.getField().m);
                final MatrixElement r = f.newElement();

                PoolExecutor executor = new PoolExecutor();
                for (int i = 0; i < f.n; i++) {

                    final int finalI = i;
                    executor.submit(new Runnable() {
                        public void run() {
                            // row \times column
                            for (int j = 0; j < f.m; j++) {
                                Element temp = field.getTargetField().newElement();

                                for (int k = 0; k < field.m; k++) {
                                    if (isZeroAt(finalI, k))
                                        continue;
                                    temp.add(getAt(finalI, k).duplicate().mul(me.getAt(k, j)));
                                }
                                r.getAt(finalI, j).set(temp);
                            }
                        }
                    });
                }
                executor.awaitTermination();

                return r;
            }
        }

        throw new IllegalStateException("Not Implemented yet!!!");
    }

}