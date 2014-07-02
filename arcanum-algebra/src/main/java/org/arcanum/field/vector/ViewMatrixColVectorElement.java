package org.arcanum.field.vector;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.Vector;
import org.arcanum.field.base.AbstractVectorElement;
import org.arcanum.field.base.AbstractVectorField;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ViewMatrixColVectorElement<E extends Element> extends AbstractVectorElement<E, AbstractVectorField> {

    protected final Matrix base;
    protected final int col;

    public ViewMatrixColVectorElement(AbstractMatrixField field, Matrix base, int col) {
        super(new VectorField<Field>(field.getRandom(), field.getTargetField(), field.getN()));

        this.base = base;
        this.col = col;
    }

    public int getSize() {
        return base.getN();
    }

    public E getAt(int index) {
        return (E) base.getAt(index, col);
    }

    public boolean isZeroAt(int index) {
        return base.isZeroAt(index, col);
    }

    public Vector<E> setAt(int index, E element) {
        base.setAt(index, col, element);

        return this;
    }
}
