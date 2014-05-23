package org.arcanum.field.floating;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;
import org.arcanum.Element;
import org.arcanum.field.base.AbstractElement;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class FloatingElement extends AbstractElement<FloatingField> {

    protected Apfloat value;


    public FloatingElement(FloatingField field) {
        super(field);

        this.value = field.zero;
    }

    public FloatingElement(FloatingField field, Apfloat value) {
        super(field);

        this.value = value;
    }

    public FloatingElement(FloatingElement element) {
        super(element.getField());

        this.value = element.value;
    }


    public FloatingField getField() {
        return field;
    }

    @Override
    public Element getImmutable() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public FloatingElement duplicate() {
        return new FloatingElement(this);
    }

    public FloatingElement set(Element value) {
        if (value instanceof FloatingElement)
            this.value = ((FloatingElement) value).value;
        else
            this.value = new Apint(value.toBigInteger(), field.radix);


        return this;
    }

    public FloatingElement set(int value) {
        this.value = new Apfloat(value, field.precision, field.radix);

        return this;
    }

    public FloatingElement set(BigInteger value) {
        this.value = new Apfloat(value, field.precision, field.radix);

        return this;
    }

    public boolean isZero() {
        return value.signum() == 0;
    }

    public boolean isOne() {
        return field.one.equals(value);
    }

    public FloatingElement twice() {
//        this.value = value.multiply(BigIntegerUtils.TWO);
        this.value = value.add(value);

        return this;
    }

    public FloatingElement mul(int z) {
        this.value = this.value.multiply(new Apfloat(z, field.precision, field.radix));

        return this;
    }

    public FloatingElement setToZero() {
        this.value = field.zero;

        return this;
    }

    public FloatingElement setToOne() {
        this.value = field.one;

        return this;
    }

    public FloatingElement setToRandom() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public FloatingElement setFromHash(byte[] source, int offset, int length) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public int setFromBytes(byte[] source) {
        return setFromBytes(source, 0);
    }

    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public FloatingElement square() {
        value = value.multiply(value);

        return this;
    }

    public FloatingElement invert() {
        value = ApfloatMath.pow(value, -1);

        return this;
    }

    public FloatingElement halve() {
        value = value.divide(field.two);

        return this;
    }

    public FloatingElement negate() {
        value = value.negate();

        return this;
    }

    public FloatingElement add(Element element) {
        value = value.add(((FloatingElement)element).value);

        return this;
    }

    public FloatingElement sub(Element element) {
        value = value.subtract(((FloatingElement)element).value);

        return this;
    }

    public FloatingElement div(Element element) {
        value = value.divide(((FloatingElement) element).value);

        return this;
    }

    public FloatingElement mul(Element element) {
        value = value.multiply(((FloatingElement)element).value);

        return this;
    }

    public FloatingElement mul(BigInteger n) {
        this.value = this.value.multiply(new Apfloat(n, field.precision, field.radix));

        return this;
    }

    public FloatingElement mulZn(Element z) {
        this.value = this.value.multiply(new Apfloat(z.toBigInteger(), field.precision, field.radix));

        return this;
    }

    public boolean isSqr() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public FloatingElement sqrt() {
        this.value = ApfloatMath.sqrt(this.value);

        return this;
    }


    public FloatingElement pow(BigInteger n) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public FloatingElement powZn(Element n) {
        return pow(n.toBigInteger());
    }

    public boolean isEqual(Element e) {
        return this == e || (e instanceof FloatingElement && value.compareTo(((FloatingElement) e).value) == 0);
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof FloatingElement) {
            return this.value.compareTo(((FloatingElement)o).value);
        } else
            throw new IllegalArgumentException("Not a FloatingElement!!!");
    }

    public BigInteger toBigInteger() {
        return value.truncate().toBigInteger();
    }

    @Override
    public byte[] toBytes() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public int sign() {
        return value.signum();
    }

    public String toString() {
        return value.toRadix(10).toString(true);
    }

    public FloatingElement setFromObject(Object value) {
        // TODO: check compatibility
        this.value = (Apfloat) value;

        return this;
    }

    public Apfloat getValue() {
        return value;
    }
}
