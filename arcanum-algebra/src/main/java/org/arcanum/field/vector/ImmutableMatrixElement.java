package org.arcanum.field.vector;

import org.arcanum.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ImmutableMatrixElement<E extends Element> extends MatrixElement<E> {

    public ImmutableMatrixElement(MatrixElement element) {
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
    public MatrixElement<E> duplicate() {
        return super.duplicate();
    }

    @Override
    public MatrixElement<E> getImmutable() {
        return this;
    }

    @Override
    public MatrixElement set(Element e) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public MatrixElement set(int value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public MatrixElement set(BigInteger value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public MatrixElement twice() {
        return (MatrixElement) duplicate().twice().getImmutable();
    }

    @Override
    public MatrixElement setToZero() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public MatrixElement setToOne() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public MatrixElement setToRandom() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public MatrixElement square() {
        return (MatrixElement) duplicate().square().getImmutable();
    }

    @Override
    public MatrixElement invert() {
        return (MatrixElement) duplicate().invert().getImmutable();
    }

    @Override
    public MatrixElement negate() {
        return (MatrixElement) duplicate().negate().getImmutable();
    }

    @Override
    public MatrixElement add(Element e) {
        return (MatrixElement) duplicate().add(e).getImmutable();
    }

    @Override
    public MatrixElement mul(Element e) {
        return (MatrixElement) duplicate().mul(e).getImmutable();
    }

    @Override
    public MatrixElement mul(BigInteger n) {
        return (MatrixElement) duplicate().mul(n).getImmutable();
    }

    @Override
    public MatrixElement mulZn(Element e) {
        return (MatrixElement) duplicate().mulZn(e).getImmutable();
    }

    @Override
    public MatrixElement powZn(Element e) {
        return (MatrixElement) duplicate().powZn(e).getImmutable();
    }

    @Override
    public MatrixElement setFromHash(byte[] source, int offset, int length) {
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
    public MatrixElement sub(Element element) {
        return (MatrixElement) duplicate().sub(element).getImmutable();
    }

    @Override
    public Element div(Element element) {
        return duplicate().div(element).getImmutable();
    }

    @Override
    public MatrixElement mul(int z) {
        return (MatrixElement) duplicate().mul(z).getImmutable();
    }

    @Override
    public MatrixElement sqrt() {
        return (MatrixElement) duplicate().sqrt().getImmutable();
    }

}
