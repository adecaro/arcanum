package org.arcanum.field.vector;

import org.arcanum.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ZeroMatrixElement<E extends Element> extends AbstractMatrixElement<E, AbstractMatrixField> {


    public ZeroMatrixElement(AbstractMatrixField field) {
        super(field);
    }


    @Override
    public boolean isSparse() {
        return true;
    }

    @Override
    public Element duplicate() {
        return new ZeroMatrixElement<E>(field);
    }

    @Override
    public final boolean isZeroAt(int row, int col) {
        return true;
    }

    @Override
    public final E getAt(int row, int col) {
        throw new IllegalStateException("Invalid Position");
    }

    @Override
    public Element mul(BigInteger n) {
        return this;
    }
}
