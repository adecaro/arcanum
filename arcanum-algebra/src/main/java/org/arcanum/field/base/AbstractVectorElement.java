package org.arcanum.field.base;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Vector;
import org.arcanum.field.vector.VectorField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class AbstractVectorElement<E extends Element, F extends AbstractFieldOver> extends AbstractElement<F> implements Vector<E> {

    protected List<E> coeff;


    protected AbstractVectorElement(F field, int size) {
        super(field);

        if (size > 0)
            this.coeff = new ArrayList<E>(size);
    }


    public E getAt(int index) {
        return coeff.get(index);
    }

    public Vector<E> subVectorFrom(int start) {
        Vector v = new VectorField<Field>(field.getRandom(), field.getTargetField(), getSize() - start).newElement();

        for (int i = 0, n = v.getSize(); i < n; i++)
            v.getAt(i).set(getAt(start + i));

        return v;
    }

    public Vector<E> subVectorTo(int end) {
        Vector v = new VectorField<Field>(field.getRandom(), field.getTargetField(), end).newElement();

        for (int i = 0; i < end; i++)
            v.getAt(i).set(getAt(i));

        return v;
    }

    public int getSize() {
        return coeff.size();
    }

}