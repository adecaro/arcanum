package org.arcanum.field.vector;

import org.arcanum.Element;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class IdentityMatrixElement<E extends Element> extends AbstractMatrixElement<E, MatrixField>{

    protected final E value;

    public IdentityMatrixElement(MatrixField field, E value) {
        super(field);

        this.value = value;
    }


    @Override
    public boolean isSparse() {
        return true;
    }

    @Override
    public boolean isZeroAt(int row, int col) {
        return row != col;
    }

    @Override
    public E getAt(int row, int col) {
        // TODO: remove this check
        if (isZeroAt(row, col))
            throw new IllegalStateException("Invalid Position");

        return value;
    }
}
