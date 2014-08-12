package org.arcanum.field.z;

import org.arcanum.Element;
import org.arcanum.common.collection.Arrays;
import org.arcanum.common.math.BigIntegerUtils;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class SymmetricZrElement<F extends SymmetricZrField> extends AbstractBigIntegerZElement<F> {

    public SymmetricZrElement(F field) {
        super(field);

        this.value = BigInteger.ZERO;
    }

    public SymmetricZrElement(F field, BigInteger value) {
        super(field);

        set(value);
    }

    public SymmetricZrElement(SymmetricZrElement<F> zrElement) {
        super(zrElement.getField());

        // TODO: try to save this mod!
        this.value = zrElement.value/**/;
    }


    public F getField() {
        return field;
    }

    @Override
    public Element getImmutable() {
        return new ImmutableSymmetricZrElement(this);
    }

    public SymmetricZrElement duplicate() {
        return new SymmetricZrElement(this);
    }

    public SymmetricZrElement set(Element value) {
//        TODO: find a good solution
//        this.value = ((AbstractBigIntegerZElement) value).value;

//        return mod();
        this.value = ((AbstractBigIntegerZElement) value).value;

        return this;
    }

    public SymmetricZrElement set(int value) {
        this.value = BigInteger.valueOf(value);

        return mod();
    }

    public SymmetricZrElement set(BigInteger value) {
        this.value = value;

        return mod();
    }

    public boolean isZero() {
        return BigInteger.ZERO.equals(value);
    }

    public boolean isOne() {
        return BigInteger.ONE.equals(value);
    }

    public SymmetricZrElement twice() {
//        this.value = value.multiply(BigIntegerUtils.TWO);
        this.value = value.add(value);

        return mod();
    }

    public SymmetricZrElement mul(int z) {
        this.value = this.value.multiply(BigInteger.valueOf(z));

        return mod();
    }

    public SymmetricZrElement setToZero() {
        this.value = BigInteger.ZERO;

        return this;
    }

    public SymmetricZrElement setToOne() {
        this.value = BigInteger.ONE;

        return this;
    }

    public SymmetricZrElement setToRandom() {
        this.value = BigIntegerUtils.getRandom(field.order, field.getRandom());

        return mod();
    }

    public SymmetricZrElement setFromHash(byte[] source, int offset, int length) {
        int i = 0, n, count = (field.order.bitLength() + 7) / 8;
        byte[] buf = new byte[count];

        byte counter = 0;
        boolean done = false;

        for (; ; ) {
            if (length >= count - i) {
                n = count - i;
                done = true;
            } else n = length;

            System.arraycopy(source, offset, buf, i, n);
            i += n;

            if (done)
                break;

            buf[i] = counter;
            counter++;
            i++;

            if (i == count) break;
        }
        assert (i == count);

        //mpz_import(z, count, 1, 1, 1, 0, buf);
        BigInteger z = new BigInteger(1, buf);

        while (z.compareTo(field.order) > 0) {
            z = z.divide(BigIntegerUtils.TWO);
        }

        this.value = z;

        return this;
    }

    public int setFromBytes(byte[] source) {
        return setFromBytes(source, 0);
    }

    public int setFromBytes(byte[] source, int offset) {
        byte[] buffer = Arrays.copyOf(source, offset, field.getLengthInBytes());
        value = new BigInteger(1, buffer);
        mod();

        return buffer.length;
    }

    public SymmetricZrElement square() {
//        value = value.modPow(BigIntegerUtils.TWO, field.order);
        value = value.multiply(value);

        return mod();
    }

    public SymmetricZrElement invert() {
        value = value.modInverse(field.order);

        return mod();
    }

    public SymmetricZrElement halve() {
        if (field.twoInverse == null)
            throw new IllegalStateException("Cannot halve. Check field.order!");

        value = value.multiply(field.twoInverse);

        return mod();
    }

    public SymmetricZrElement negate() {
        if (isZero()) {
            value = BigInteger.ZERO;
            return this;
        }

        value = field.order.subtract(value);

        return mod();
    }

    @Override
    public SymmetricZrElement add(BigInteger element) {
        // TODO: should run mod?
        value = value.add(element);

        return mod();
//        return this;
    }

    public SymmetricZrElement add(Element element) {
        // TODO: should run mod?
        value = value.add(((AbstractBigIntegerZElement) element).value);

        return mod();
//        return this;
    }

    public SymmetricZrElement sub(Element element) {
        value = value.subtract(((SymmetricZrElement) element).value);

        return mod();
    }

    public SymmetricZrElement div(Element element) {
        value = value.multiply(((SymmetricZrElement) element).value.modInverse(field.order));

        return mod();
    }

    public SymmetricZrElement mul(Element element) {
        value = value.multiply(((AbstractBigIntegerZElement) element).value);

        return mod();
    }

    public SymmetricZrElement mul(BigInteger n) {
        this.value = this.value.multiply(n);

        return mod();
    }

    public SymmetricZrElement mulZn(Element z) {
        this.value = this.value.multiply(z.toBigInteger());

        return mod();
    }

    public boolean isSqr() {
        return BigInteger.ZERO.equals(value) || BigIntegerUtils.legendre(value, field.order) == 1;
    }

    public SymmetricZrElement sqrt() {
        // Apply the Tonelli-Shanks Algorithm

        Element e0 = field.newElement();
        Element nqr = field.getNqr();
        Element gInv = nqr.duplicate().invert();

        // let q be the field.order of the field
        // q - 1 = 2^s t, for some t odd
        BigInteger t = field.order.subtract(BigInteger.ONE);
        int s = BigIntegerUtils.scanOne(t, 0);
        t = t.divide(BigInteger.valueOf(2 << (s - 1)));

        BigInteger e = BigInteger.ZERO;
        BigInteger orderMinusOne = field.order.subtract(BigInteger.ONE);

        for (int i = 2; i <= s; i++) {
            e0.set(gInv).pow(e);
            e0.mul(this).pow(orderMinusOne.divide(BigInteger.valueOf(2 << (i - 1))));

            if (!e0.isOne())
                e = e.setBit(i - 1);
        }
        e0.set(gInv).pow(e);
        e0.mul(this);
        t = t.add(BigInteger.ONE);
        t = t.divide(BigIntegerUtils.TWO);
        e = e.divide(BigIntegerUtils.TWO);

        // TODO(-):
        // (suggested by Hovav Shacham) replace next three lines with
        //  element_pow2_mpz(x, e0, t, nqr, e);
        // once sliding windows are implemented for pow2

        e0.pow(t);
        set(nqr).pow(e).mul(e0);

        return this;
    }


    public SymmetricZrElement pow(BigInteger n) {
        this.value = this.value.modPow(n, field.order);

        return mod();
    }

    public SymmetricZrElement powZn(Element n) {
        return pow(n.toBigInteger());
    }

    public boolean isEqual(Element e) {
        mod();
        ((SymmetricZrElement) e).mod();

        return this == e || (e instanceof SymmetricZrElement && value.compareTo(((SymmetricZrElement) e).value) == 0);
    }

    @Override
    public byte[] toBytes() {
        mod();
        byte[] bytes = (value.signum() == -1)
                ? value.add(field.order).toByteArray()
                : value.toByteArray();

        if (bytes.length > field.getLengthInBytes()) {
            // strip the zero prefix
            if (bytes[0] == 0 && bytes.length == field.getLengthInBytes() + 1) {
                // Remove it
                bytes = Arrays.copyOfRange(bytes, 1, bytes.length);
            } else
                throw new IllegalStateException("result has more than FixedLengthInBytes.");
        } else if (bytes.length < field.getLengthInBytes()) {
            byte[] result = new byte[field.getLengthInBytes()];
            System.arraycopy(bytes, 0, result, field.getLengthInBytes() - bytes.length, bytes.length);
            return result;
        }

        return bytes;
    }

    public int sign() {
        if (isZero())
            return 0;

        if (field.isOrderOdd()) {
            return BigIntegerUtils.isOdd(value) ? 1 : -1;
        } else {
            return value.add(value).compareTo(field.order);
        }
    }

    @Override
    public BigInteger toCanonicalBigInteger() {
        BigInteger res = value;
        if (res.signum() == -1)
            res = res.add(field.order);

        return res;
    }

    public String toString() {
        mod();
        return value.toString();
    }

    private final SymmetricZrElement mod() {
        this.value = this.value.mod(field.order);

        if (this.value.compareTo(field.halfOrder) > 0)
            this.value = this.value.subtract(field.order);

        return this;
    }

}
