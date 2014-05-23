package org.arcanum.field.poly;

import org.arcanum.Element;
import org.arcanum.Polynomial;
import org.arcanum.Vector;
import org.arcanum.field.base.AbstractElement;
import org.arcanum.field.base.AbstractFieldOver;

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


    public int getSize() {
        return coefficients.size();
    }

    public E getAt(int index) {
        return coefficients.get(index);
    }

    public List<E> getCoefficients() {
        return coefficients;
    }

    public E getCoefficient(int index) {
        return coefficients.get(index);
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

}