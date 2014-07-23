package org.arcanum.field.poly;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Polynomial;
import org.arcanum.Vector;
import org.arcanum.field.base.AbstractElement;
import org.arcanum.field.base.AbstractFieldOver;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class AbstractPolyElement<E extends Element, F extends AbstractFieldOver>
        extends AbstractElement<F> implements Polynomial<E> {

    protected List<E> coefficients;


    protected AbstractPolyElement(F field) {
        super(field);

        this.coefficients = new ArrayList<E>();
    }

    public Field getTargetField() {
        return field.getTargetField();
    }

    public int getSize() {
        return coefficients.size();
    }

    public E getAt(int index) {
        return coefficients.get(index);
    }

    public Vector setAt(int index, Element element) {
        coefficients.get(index).set(element);

        return this;
    }

    public Vector setAt(int index, BigInteger value) {
        coefficients.get(index).set(value);

        return this;
    }

    public int getDegree() {
        return coefficients.size();
    }

    public Vector add(Element... es) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Vector<E> subVectorTo(int end) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Vector<E> subVectorFrom(int start) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Vector setZeroAt(int index) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public boolean isZeroAt(int index) {
        throw new IllegalStateException("Not Implemented yet!");
    }
}