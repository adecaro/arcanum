package org.arcanum.field.vector;

import org.arcanum.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ImmutableArrayMatrixElement<E extends Element> extends ArrayMatrixElement<E> {

    public ImmutableArrayMatrixElement(ArrayMatrixElement element) {
        super(element.getField());

        this.matrix = new Element[field.n][field.m];
        for (int i = 0; i < field.n; i++) {
            for (int j = 0; j < field.m; j++) {
                matrix[i][j] = element.getAt(i, j).duplicate();
            }
        }

        this.immutable = true;
    }

    @Override
    public ArrayMatrixElement<E> duplicate() {
        return super.duplicate();
    }

    @Override
    public ArrayMatrixElement<E> getImmutable() {
        return this;
    }

    @Override
    public ArrayMatrixElement set(Element e) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public ArrayMatrixElement set(int value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public ArrayMatrixElement set(BigInteger value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public ArrayMatrixElement twice() {
        return (ArrayMatrixElement) duplicate().twice().getImmutable();
    }

    @Override
    public ArrayMatrixElement setToZero() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public ArrayMatrixElement setToOne() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public ArrayMatrixElement setToRandom() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public ArrayMatrixElement square() {
        return (ArrayMatrixElement) duplicate().square().getImmutable();
    }

    @Override
    public ArrayMatrixElement invert() {
        return (ArrayMatrixElement) duplicate().invert().getImmutable();
    }

    @Override
    public ArrayMatrixElement negate() {
        return (ArrayMatrixElement) duplicate().negate().getImmutable();
    }

    @Override
    public ArrayMatrixElement add(Element e) {
        return (ArrayMatrixElement) duplicate().add(e).getImmutable();
    }

    @Override
    public ArrayMatrixElement mul(Element e) {
        return (ArrayMatrixElement) duplicate().mul(e).getImmutable();
    }

    @Override
    public ArrayMatrixElement mul(BigInteger n) {
        return (ArrayMatrixElement) duplicate().mul(n).getImmutable();
    }

    @Override
    public ArrayMatrixElement mulZn(Element e) {
        return (ArrayMatrixElement) duplicate().mulZn(e).getImmutable();
    }

    @Override
    public ArrayMatrixElement powZn(Element e) {
        return (ArrayMatrixElement) duplicate().powZn(e).getImmutable();
    }

    @Override
    public ArrayMatrixElement setFromHash(byte[] source, int offset, int length) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytes(byte[] source) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public Element pow(BigInteger n) {
        return duplicate().pow(n).getImmutable();
    }

    @Override
    public Element halve() {
        return duplicate().halve().getImmutable();
    }

    @Override
    public ArrayMatrixElement sub(Element element) {
        return (ArrayMatrixElement) duplicate().sub(element).getImmutable();
    }

    @Override
    public Element div(Element element) {
        return duplicate().div(element).getImmutable();
    }

    @Override
    public ArrayMatrixElement mul(int z) {
        return (ArrayMatrixElement) duplicate().mul(z).getImmutable();
    }

    @Override
    public ArrayMatrixElement sqrt() {
        return (ArrayMatrixElement) duplicate().sqrt().getImmutable();
    }

}
