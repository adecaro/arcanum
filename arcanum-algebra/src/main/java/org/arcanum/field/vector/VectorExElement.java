package org.arcanum.field.vector;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Vector;
import org.arcanum.field.base.AbstractVectorElement;
import org.arcanum.field.base.AbstractVectorField;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class VectorExElement<E extends Element> extends AbstractVectorElement<E, AbstractVectorField> {

    protected final Vector<E> base;
    protected final E e;

    public VectorExElement(AbstractVectorField field, Vector<E> base, E e) {
        super(new VectorField<Field>(field.getRandom(), field.getTargetField(), field.getN() + 1));

        this.base = base;
        this.e = e;
    }

    public int getSize() {
        return base.getSize() + 1;
    }

    public E getAt(int index) {
        if (index < base.getSize())
            return base.getAt(index);

        return e;
    }

    public boolean isZeroAt(int index) {
        if (index < base.getSize())
            return base.isZeroAt(index);

        return e.isZero();

    }

    public Vector<E> setAt(int index, E element) {
        if (index < base.getSize())
            return base.setAt(index, element);

        e.set(element);

        return this;
    }
}
