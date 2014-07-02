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
public class VectorElement<E extends Element> extends AbstractVectorElement<E, AbstractVectorField> {


    protected List<E> coeff;

    public VectorElement(AbstractVectorField field) {
        super(field);

        coeff = new ArrayList<E>(field.getN());
        for (int i = 0, size = field.getN(); i < size; i++)
            coeff.add((E) field.getTargetField().newElement());
    }

    public VectorElement(AbstractVectorElement element) {
        super(element.getField());

        coeff = new ArrayList<E>(field.getN());
        for (int i = 0, size = field.getN(); i < size; i++)
            coeff.add((E) element.getAt(i).duplicate());
    }

    public VectorElement(AbstractVectorField field, List<E> coeff) {
        super(field);

        this.coeff = coeff;
    }

    public VectorElement(AbstractVectorField field, Sampler<BigInteger> sampler) {
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
        coeff.get(index).set(element);

        return this;
    }

    public int getSize() {
        return coeff.size();
    }

    public VectorElement<E> duplicate() {
        return new VectorElement<E>(this);
    }

    public VectorElement<E> getImmutable() {
        return new ImmutableVectorElement<E>(this);
    }

}