package org.arcanum.field.vector;

import org.arcanum.Element;
import org.arcanum.ElementPowPreProcessing;
import org.arcanum.Sampler;
import org.arcanum.field.base.AbstractVectorElement;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class VectorElement<E extends Element> extends AbstractVectorElement<E, VectorField> {


    public VectorElement(VectorField field) {
        super(field);

        for (int i = 0; i < field.n; i++)
            coeff.add((E) field.getTargetField().newElement());
    }

    public VectorElement(VectorElement element) {
        super(element.getField());

        for (int i = 0; i < field.n; i++)
            coeff.add((E) element.getAt(i).duplicate());
    }


    public VectorElement(VectorField field, List<E> coeff) {
        super(field);

        this.coeff = coeff;
    }

    public VectorElement(VectorField field, Sampler<BigInteger> sampler) {
        this(field);

        for (int i = 0; i < field.n; i++)
            coeff.get(i).set(sampler.sample());
    }


    public VectorField getField() {
        return field;
    }

    public VectorElement<E> duplicate() {
        return new VectorElement<E>(this);
    }

    public VectorElement<E> getImmutable() {
        return new ImmutableVectorElement<E>(this);
    }

    public VectorElement<E> set(Element e) {
        VectorElement<E> element = (VectorElement<E>) e;

        for (int i = 0; i < coeff.size(); i++) {
            coeff.get(i).set(element.coeff.get(i));
        }

        return this;
    }

    public VectorElement<E> set(int value) {
        coeff.get(0).set(value);

        for (int i = 1; i < field.n; i++) {
            coeff.get(i).setToZero();
        }

        return this;
    }

    public VectorElement<E> set(BigInteger value) {
        coeff.get(0).set(value);

        for (int i = 1; i < field.n; i++) {
            coeff.get(i).setToZero();
        }

        return this;
    }

    public VectorElement<E> setToRandom() {
        for (int i = 0; i < field.n; i++) {
            coeff.get(i).setToRandom();
        }

        return this;
    }

    public VectorElement<E> setFromHash(byte[] source, int offset, int length) {
        for (int i = 0; i < field.n; i++) {
            coeff.get(i).setFromHash(source, offset, length);
        }

        return this;
    }

    public VectorElement<E> setToZero() {
        for (int i = 0; i < field.n; i++) {
            coeff.get(i).setToZero();
        }

        return this;
    }

    public boolean isZero() {
        for (int i = 0; i < field.n; i++) {
            if (!coeff.get(i).isZero())
                return false;
        }
        return true;
    }

    public VectorElement<E> setToOne() {
        coeff.get(0).setToOne();

        for (int i = 1; i < field.n; i++) {
            coeff.get(i).setToZero();
        }

        return this;
    }

    public boolean isOne() {
        if (!coeff.get(0).isOne())
            return false;

        for (int i = 1; i < field.n; i++) {
            if (!coeff.get(i).isZero())
                return false;
        }

        return true;
    }

    public VectorElement<E> map(Element e) {
        coeff.get(0).set(e);
        for (int i = 1; i < field.n; i++) {
            coeff.get(i).setToZero();
        }

        return this;
    }

    public VectorElement<E> twice() {
        for (int i = 0; i < field.n; i++) {
            coeff.get(i).twice();
        }

        return this;
    }

    public VectorElement<E> square() {
        for (int i = 0; i < field.n; i++) {
            coeff.get(i).square();
        }

        return this;
    }

    public VectorElement<E> invert() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public VectorElement<E> negate() {
        for (Element e : coeff) {
            e.negate();
        }

        return this;
    }

    public VectorElement<E> add(Element e) {
        VectorElement<E> element = (VectorElement<E>) e;

        for (int i = 0; i < element.getSize(); i++) {
            coeff.get(i).add(element.getAt(i));
        }

        return this;
    }

    public VectorElement<E> add(Element... es) {
        int cursor = 0;

        for (Element e : es) {
            VectorElement<E> element = (VectorElement<E>) e;

            for (int i = 0; i < element.getSize(); i++, cursor++) {
                coeff.get(cursor).add(element.getAt(i));
            }

        }

        return this;
    }

    public VectorElement<E> sub(Element e) {
        VectorElement<E> element = (VectorElement<E>) e;

        for (int i = 0; i < field.n; i++) {
            coeff.get(i).sub(element.coeff.get(i));
        }

        return this;
    }

    public Element mul(Element e) {
        if (e instanceof MatrixElement) {
            MatrixElement me = (MatrixElement) e;

            if (field.getTargetField().equals(me.getField().getTargetField())) {
                // Check dimensions

                if (this.getSize() == me.getField().m) {
                    throw new IllegalStateException("Not Implemented yet!!!");
                } else if (this.getSize() == me.getField().n) {
                    // Consider transpose

                    VectorField f = new VectorField(field.getRandom(), field.getTargetField(), me.getField().m);
                    VectorElement r = f.newElement();

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
        } else if (e instanceof VectorElement) {
            VectorElement me = (VectorElement) e;

            if (field.getTargetField().equals(me.getField().getTargetField())) {

                if (this.getSize() == me.getField().n) {

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

    public VectorElement<E> mul(int z) {
        for (int i = 0; i < field.n; i++) {
            coeff.get(i).mul(z);
        }

        return this;
    }

    public VectorElement<E> mul(BigInteger n) {
        if (BigInteger.ONE.equals(n))
            return this;

        if (BigInteger.ZERO.equals(n))
            return setToZero();

        for (int i = 0; i < field.n; i++)
            coeff.get(i).mul(n);

        return this;
    }

    public VectorElement<E> powZn(Element e) {
        for (int i = 0; i < field.n; i++) {
            coeff.get(i).powZn(e);
        }

        return this;
    }

    public ElementPowPreProcessing getElementPowPreProcessing() {
        return new VectorElementPowPreProcessing(this);
    }

    public VectorElement<E> sqrt() {
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
        if (!(e instanceof VectorElement))
            return false;

        VectorElement<E> element = (VectorElement<E>) e;

        for (int i = 0; i < field.n; i++) {
            if (!coeff.get(i).isEqual(element.coeff.get(i)))
                return false;
        }

        return true;
    }

    public int setFromBytes(byte[] source) {
        return setFromBytes(source, 0);
    }

    public int setFromBytes(byte[] source, int offset) {
        int len = offset;
        for (int i = 0, size = coeff.size(); i < size; i++) {
            len += coeff.get(i).setFromBytes(source, len);
        }
        return len - offset;
    }

    public byte[] toBytes() {
        byte[] buffer = new byte[field.getLengthInBytes()];
        int targetLB = field.getTargetField().getLengthInBytes();

        for (int len = 0, i = 0, size = coeff.size(); i < size; i++, len += targetLB) {
            byte[] temp = coeff.get(i).toBytes();
            System.arraycopy(temp, 0, buffer, len, targetLB);

            if (!field.getTargetField().newElementFromBytes(temp).isEqual(coeff.get(i))) {
                System.out.println("FATAL!");
                byte[] temp1 = coeff.get(i).toBytes();
            }

        }
        return buffer;
    }

    public BigInteger toBigInteger() {
        return coeff.get(0).toBigInteger();
    }


    public String toString() {
        StringBuffer buffer = new StringBuffer("[");
        for (Element e : coeff) {
            buffer.append(e).append(", ");
        }
        buffer.append("]");
        return buffer.toString();
    }

}