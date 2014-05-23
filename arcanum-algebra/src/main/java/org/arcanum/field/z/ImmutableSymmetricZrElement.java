package org.arcanum.field.z;

import org.arcanum.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ImmutableSymmetricZrElement extends SymmetricZrElement {

    public ImmutableSymmetricZrElement(SymmetricZrElement zrElement) {
        super(zrElement);
        this.immutable = true;
    }

    @Override
    public Element getImmutable() {
        return this;
    }

    @Override
    public SymmetricZrElement duplicate() {
        return super.duplicate();
    }

    @Override
    public SymmetricZrElement set(Element value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public SymmetricZrElement set(int value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public SymmetricZrElement set(BigInteger value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public SymmetricZrElement setToZero() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public SymmetricZrElement setToOne() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public SymmetricZrElement setToRandom() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public SymmetricZrElement setFromHash(byte[] source, int offset, int length) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytes(byte[] source) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public SymmetricZrElement twice() {
        return (SymmetricZrElement) super.duplicate().twice().getImmutable();
    }

    @Override
    public SymmetricZrElement mul(int z) {
        return (SymmetricZrElement) super.duplicate().mul(z).getImmutable();
    }

    @Override
    public SymmetricZrElement square() {
        return (SymmetricZrElement) super.duplicate().square().getImmutable();
    }

    @Override
    public SymmetricZrElement invert() {
        return (SymmetricZrElement) super.duplicate().invert().getImmutable();
    }

    @Override
    public SymmetricZrElement halve() {
        return (SymmetricZrElement) super.duplicate().halve().getImmutable();
    }

    @Override
    public SymmetricZrElement negate() {
        return (SymmetricZrElement) super.duplicate().negate().getImmutable();
    }

    @Override
    public SymmetricZrElement add(Element element) {
        return (SymmetricZrElement) super.duplicate().add(element).getImmutable();
    }

    @Override
    public SymmetricZrElement sub(Element element) {
        return (SymmetricZrElement) super.duplicate().sub(element).getImmutable();
    }

    @Override
    public SymmetricZrElement div(Element element) {
        return (SymmetricZrElement) super.duplicate().div(element).getImmutable();
    }

    @Override
    public SymmetricZrElement mul(Element element) {
        return (SymmetricZrElement) super.duplicate().mul(element).getImmutable();
    }

    @Override
    public SymmetricZrElement mul(BigInteger n) {
        return (SymmetricZrElement) super.duplicate().mul(n).getImmutable();
    }

    @Override
    public SymmetricZrElement mulZn(Element z) {
        return (SymmetricZrElement) super.duplicate().mulZn(z).getImmutable();
    }

    @Override
    public SymmetricZrElement sqrt() {
        return (SymmetricZrElement) super.duplicate().sqrt().getImmutable();
    }

    @Override
    public SymmetricZrElement pow(BigInteger n) {
        return (SymmetricZrElement) super.duplicate().pow(n).getImmutable();
    }

    @Override
    public SymmetricZrElement powZn(Element n) {
        return (SymmetricZrElement) super.duplicate().powZn(n).getImmutable();
    }

}
