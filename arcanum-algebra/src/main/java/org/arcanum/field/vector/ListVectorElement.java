package org.arcanum.field.vector;

import org.arcanum.Element;
import org.arcanum.Sampler;
import org.arcanum.Vector;
import org.arcanum.field.base.AbstractVectorElement;
import org.arcanum.field.base.AbstractVectorField;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ListVectorElement<E extends Element, F extends AbstractVectorField> extends AbstractVectorElement<E, F> {


    protected List<E> coeff;


    public ListVectorElement(F field) {
        super(field);

        coeff = new ArrayList<E>(field.getN());
        for (int i = 0, size = field.getN(); i < size; i++)
            coeff.add((E) field.getTargetField().newElement());
    }

    public ListVectorElement(AbstractVectorElement<E, F> element) {
        super(element.getField());

        coeff = new ArrayList<E>(field.getN());
        for (int i = 0, size = field.getN(); i < size; i++)
            coeff.add((E) element.getAt(i).duplicate());
    }

    public ListVectorElement(F field, List<E> coeff) {
        super(field);

        this.coeff = coeff;
    }

    public ListVectorElement(F field, E[] coeffs) {
        super(field);

        coeff = new ArrayList<E>(field.getN());
        for (int i = 0, size = field.getN(); i < size; i++)
            coeff.add(coeffs[i]);
    }

    public ListVectorElement(F field, Sampler<BigInteger> sampler) {
        this(field);

        coeff = new ArrayList<E>(field.getN());
        for (int i = 0, size = field.getN(); i < size; i++)
            coeff.add((E) field.getTargetField().newElementFromSampler(sampler));
    }



    public E getAt(int index) {
        return coeff.get(index);
    }

    public boolean isZeroAt(int index) {
        return coeff.get(index).isZero();
    }

    public Vector<E> setAt(int index, E element) {
        if (element instanceof Vector) {
            Vector v = (Vector) element;

            for (int i=0, size = v.getSize(); i< size ; i++)
                coeff.get(index+i).set(v.getAt(i));

        } else
            coeff.get(index).set(element);

        return this;
    }

    public int getSize() {
        return coeff.size();
    }

    public ListVectorElement duplicate() {
        return new ListVectorElement(this);
    }

    public ListVectorElement getImmutable() {
        return new ImmutableListVectorElement(this);
    }

}