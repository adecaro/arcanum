package org.arcanum.field.vector;

import org.arcanum.Element;
import org.arcanum.Vector;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class DiagonalMatrixElement<E extends Element> extends AbstractMatrixElement<E, MatrixField> {

    protected final Vector base;
    protected final int baseLength;

    public DiagonalMatrixElement(MatrixField field, Vector row) {
        super(field);

        this.base = row;
        this.baseLength = base.getSize();
    }

    @Override
    public Element duplicate() {
        return new DiagonalMatrixElement<E>(field, (Vector) base.duplicate());
    }

    @Override
    public boolean isZeroAt(int row, int col) {
        int colOffset = row * base.getSize();

        if (col >= colOffset && col < colOffset + baseLength) {
            return base.getAt(col % baseLength).isZero();
        } else
            return true;
    }

    @Override
    public E getAt(int row, int col) {
        // TODO: remove this check
        if (isZeroAt(row, col))
            throw new IllegalStateException("Invalid Position");

        return (E) base.getAt(col % baseLength);
    }

    @Override
    public Element mul(BigInteger n) {
        base.mul(n);
        return this;
    }
}
