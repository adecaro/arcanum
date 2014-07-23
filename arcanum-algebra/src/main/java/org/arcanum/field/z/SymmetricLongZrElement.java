package org.arcanum.field.z;

import org.arcanum.Element;
import org.arcanum.field.base.AbstractElement;
import org.arcanum.util.concurrent.ThreadSecureRandom;

import java.math.BigInteger;


/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class SymmetricLongZrElement<F extends SymmetricLongZrField> extends AbstractElement<F> {

    private long value;

    public SymmetricLongZrElement(F field) {
        super(field);

        this.value = 0;
    }

    public SymmetricLongZrElement(F field, long value) {
        super(field);

        this.value = value;
    }

    public Element duplicate() {
        return new SymmetricLongZrElement<F>(field, value);
    }

    public Element set(Element value) {
        // TODO: check carefully this!
//        this.value = ((SymmetricLongZrElement) value).value % field.order;
//
//        return mod();

        this.value = ((SymmetricLongZrElement) value).value;
        return this;
    }

    public Element set(int value) {
        this.value = value % field.order;

        return mod();
    }

    public Element set(long value) {
        this.value = value % field.order;

        return mod();
    }

    public Element set(BigInteger value) {
        this.value = value.longValue() % field.order;

        return mod();
    }

    public BigInteger toBigInteger() {
        return BigInteger.valueOf(value);
    }

    public BigInteger toCanonicalBigInteger() {
        long res = value;
        if (res < 0)
            res += field.order;

        return BigInteger.valueOf(res);
    }

    public Element setToRandom() {
        // TODO: fix this.
//        this.value = Math.abs(field.getRandom().nextLong()) % field.order;
        this.value = Math.abs(ThreadSecureRandom.get().nextLong()) % field.order;

        return mod();
    }

    public Element setFromHash(byte[] source, int offset, int length) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element setToZero() {
        this.value = 0;

        return this;
    }

    public boolean isZero() {
        return value == 0;
    }

    public Element setToOne() {
        this.value = 1;

        return this;
    }

    public boolean isEqual(Element value) {
        return value instanceof SymmetricLongZrElement && ((SymmetricLongZrElement) value).value == this.value;

    }

    public boolean isOne() {
        return value == 1;
    }

    public Element invert() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element negate() {
        if (isZero()) {
            value = 0;
            return this;
        }

        value = field.order - value;

        return mod();
    }

    public Element add(Element element) {
        this.value = (this.value + ((SymmetricLongZrElement) element).value) % field.order;

        return mod();
    }

    public Element addProduct(Element a, Element b) {
        this.value = (this.value +
                (((SymmetricLongZrElement) a).value * ((SymmetricLongZrElement) b).value)
        ) % field.order;

        return mod();
    }


    @Override
    public Element add(BigInteger element) {
        this.value = (this.value + element.longValue()) % field.order;

        return mod();
    }

    public Element mul(Element element) {
        long q, res;

        long a = this.value;
        long b = ((SymmetricLongZrElement) element).value;

        q = (long) ((((double) a) * ((double) b)) * field.orderInv);
        res = a * b - q * field.order;
        if (res >= field.order)
            res -= field.order;
        else if (res < 0)
            res += field.order;

        this.value = res;

        return mod();
    }

    public Element mul(BigInteger n) {
        long q, res;

        long a = this.value;
        long b = n.longValue();

        q = (long) ((((double) a) * ((double) b)) * field.orderInv);
        res = a * b - q * field.order;
        if (res >= field.order)
            res -= field.order;
        else if (res < 0)
            res += field.order;

        this.value = res;

        return mod();
    }

    public boolean isSqr() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public int sign() {
        throw new IllegalStateException("Not implemented yet!!!");
    }


    private final SymmetricLongZrElement mod() {
        if (Math.abs(this.value) > field.halfOrder) {
            if (this.value < 0)
                this.value += field.order;
            else
                this.value -= field.order;
        }

        return this;
    }

    public String toString() {
        return String.valueOf(value);
    }


}