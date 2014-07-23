package org.arcanum.field.vector;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.Vector;
import org.arcanum.field.base.AbstractVectorElement;
import org.arcanum.field.base.AbstractVectorField;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ViewMatrixRowVectorElement<E extends Element> extends AbstractVectorElement<E, AbstractVectorField> {

    protected final Matrix base;
    protected final int row;

    public ViewMatrixRowVectorElement(AbstractMatrixField field, Matrix base, int row) {
        super(new VectorField<Field>(field.getRandom(), field.getTargetField(), field.getM()));

        this.base = base;
        this.row = row;
    }

    public int getSize() {
        return base.getM();
    }

    public E getAt(int index) {
        return (E) base.getAt(row, index);
    }

    public boolean isZeroAt(int index) {
        return base.isZeroAt(row, index);
    }

    public Vector<E> setAt(int index, E element) {
        base.setAt(row, index, element);

        return this;
    }

    public Vector<E> setAt(int index, BigInteger value) {
        base.setAt(row, index, value);

        return this;
    }
}
