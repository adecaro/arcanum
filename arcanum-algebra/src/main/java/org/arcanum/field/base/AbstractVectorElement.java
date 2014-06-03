package org.arcanum.field.base;

import org.arcanum.*;
import org.arcanum.field.vector.VectorElementPowPreProcessing;
import org.arcanum.field.vector.VectorField;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class AbstractVectorElement<E extends Element, F extends AbstractVectorField> extends AbstractElement<F> implements Vector<E> {


    protected AbstractVectorElement(F field) {
        super(field);
    }


    @Override
    public F getField() {
        return super.getField();
    }

    public Field getTargetField() {
        return field.getTargetField();
    }

    public Element duplicate() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Vector<E> subVectorFrom(int start) {
        Vector v = new VectorField<Field>(field.getRandom(), field.getTargetField(), getSize() - start).newElement();

        for (int i = 0, n = v.getSize(); i < n; i++)
            v.getAt(i).set(getAt(start + i));

        return v;
    }

    public Vector<E> setZeroAt(int index) {
        getAt(index).setToZero();

        return this;
    }

    public Vector<E> subVectorTo(int end) {
        // TODO: create a view?
        Vector v = new VectorField<Field>(field.getRandom(), field.getTargetField(), end).newElement();

        for (int i = 0; i < end; i++)
            v.getAt(i).set(getAt(i));

        return v;
    }

    public Vector<E> set(Element e) {
        Vector element = (Vector) e;

        for (int i = 0; i < field.n; i++) {
            getAt(i).set(element.getAt(i));
        }

        return this;
    }

    public Vector<E> set(int value) {
        getAt(0).set(value);

        for (int i = 1; i < field.n; i++) {
            getAt(i).setToZero();
        }

        return this;
    }

    public Vector<E> set(BigInteger value) {
        getAt(0).set(value);

        for (int i = 1; i < field.n; i++) {
            getAt(i).setToZero();
        }

        return this;
    }

    public Vector<E> setToRandom() {
        for (int i = 0; i < field.n; i++) {
            getAt(i).setToRandom();
        }

        return this;
    }

    public Vector<E> setFromHash(byte[] source, int offset, int length) {
        for (int i = 0; i < field.n; i++) {
            getAt(i).setFromHash(source, offset, length);
        }

        return this;
    }

    public Vector<E> setToZero() {
        for (int i = 0; i < field.n; i++) {
            getAt(i).setToZero();
        }

        return this;
    }

    public boolean isZero() {
        for (int i = 0; i < field.n; i++) {
            if (!getAt(i).isZero())
                return false;
        }
        return true;
    }

    public Vector<E> setToOne() {
        getAt(0).setToOne();

        for (int i = 1; i < field.n; i++) {
            getAt(i).setToZero();
        }

        return this;
    }

    public boolean isOne() {
        if (!getAt(0).isOne())
            return false;

        for (int i = 1; i < field.n; i++) {
            if (!getAt(i).isZero())
                return false;
        }

        return true;
    }

    public Vector<E> map(Element e) {
        getAt(0).set(e);
        for (int i = 1; i < field.n; i++) {
            getAt(i).setToZero();
        }

        return this;
    }

    public Vector<E> twice() {
        for (int i = 0; i < field.n; i++) {
            getAt(i).twice();
        }

        return this;
    }

    public Vector<E> square() {
        for (int i = 0; i < field.n; i++) {
            getAt(i).square();
        }

        return this;
    }

    public Vector<E> invert() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Vector<E> negate() {
        for (int i = 0; i < field.n; i++) {
            getAt(i).negate();
        }

        return this;
    }

    public Vector<E> add(Element e) {
        Vector<E> element = (Vector<E>) e;

        for (int i = 0; i < element.getSize(); i++) {
            getAt(i).add(element.getAt(i));
        }

        return this;
    }

    public Vector<E> add(Element... es) {
        int cursor = 0;

        for (Element e : es) {
            Vector<E> element = (Vector<E>) e;

            for (int i = 0; i < element.getSize(); i++, cursor++) {
                getAt(cursor).add(element.getAt(i));
            }

        }

        return this;
    }

    public Vector<E> sub(Element e) {
        Vector<E> element = (Vector<E>) e;

        for (int i = 0; i < field.n; i++) {
            getAt(i).sub(element.getAt(i));
        }

        return this;
    }

    public Element mul(Element e) {
        if (e instanceof Matrix) {
            Matrix me = (Matrix) e;

            if (field.getTargetField().equals(me.getTargetField())) {
                // Check dimensions

                if (this.getSize() == me.getM()) {
                    throw new IllegalStateException("Not Implemented yet!!!");
                } else if (this.getSize() == me.getN()) {
                    // Consider transpose

                    VectorField f = new VectorField(field.getRandom(), field.getTargetField(), me.getM());
                    Vector r = f.newElement();

                    for (int i = 0; i < f.n; i++) {

                        // row \times column
                        Element temp = field.getTargetField().newElement();
                        for (int k = 0; k < getSize(); k++) {
                            temp.add(
                                    getAt(k).duplicate().mul(me.getAt(k, i))
                            );
                        }

                        r.getAt(i).set(temp);
                    }

                    return r;
                }
            }
        } else if (e instanceof Vector) {
            Vector me = (Vector) e;

            if (field.getTargetField().equals(me.getTargetField())) {

                if (this.getSize() == me.getSize()) {

                    Element temp = field.getTargetField().newElement();
                    for (int k = 0; k < getSize(); k++) {
                        temp.add(
                                getAt(k).duplicate().mul(me.getAt(k))
                        );
                    }

                    return temp;
                }

            }

        }

        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Vector<E> mul(int z) {
        for (int i = 0; i < field.n; i++) {
            getAt(i).mul(z);
        }

        return this;
    }

    public Vector<E> mul(BigInteger n) {
        if (BigInteger.ONE.equals(n))
            return this;

        if (BigInteger.ZERO.equals(n))
            return setToZero();

        for (int i = 0; i < field.n; i++)
            getAt(i).mul(n);

        return this;
    }

    public Vector<E> powZn(Element e) {
        for (int i = 0; i < field.n; i++) {
            getAt(i).powZn(e);
        }

        return this;
    }

    public ElementPowPreProcessing getElementPowPreProcessing() {
        return new VectorElementPowPreProcessing(this);
    }

    public Vector<E> sqrt() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isSqr() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public int sign() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isEqual(Element e) {
        if (e == this)
            return true;
        if (!(e instanceof Vector))
            return false;

        Vector<E> element = (Vector<E>) e;

        for (int i = 0; i < field.n; i++) {
            if (!getAt(i).isEqual(element.getAt(i)))
                return false;
        }

        return true;
    }

    public int setFromBytes(byte[] source) {
        return setFromBytes(source, 0);
    }

    public int setFromBytes(byte[] source, int offset) {
        int len = offset;
        for (int i = 0, size = getSize(); i < size; i++) {
            len += getAt(i).setFromBytes(source, len);
        }
        return len - offset;
    }

    public byte[] toBytes() {
        byte[] buffer = new byte[field.getLengthInBytes()];
        int targetLB = field.getTargetField().getLengthInBytes();

        for (int len = 0, i = 0, size = getSize(); i < size; i++, len += targetLB) {
            byte[] temp = getAt(i).toBytes();
            System.arraycopy(temp, 0, buffer, len, targetLB);

            if (!field.getTargetField().newElementFromBytes(temp).isEqual(getAt(i))) {
                System.out.println("FATAL!");
                byte[] temp1 = getAt(i).toBytes();
            }

        }
        return buffer;
    }

    public BigInteger toBigInteger() {
        return getAt(0).toBigInteger();
    }


    public String toString() {
        StringBuffer buffer = new StringBuffer("[");
        for (int i = 0; i < field.n; i++) {
            buffer.append(getAt(i)).append(", ");
        }
        buffer.append("]");
        return buffer.toString();
    }
    
    

}