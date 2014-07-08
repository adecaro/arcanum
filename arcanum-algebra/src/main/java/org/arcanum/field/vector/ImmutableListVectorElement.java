package org.arcanum.field.vector;

import org.arcanum.Element;
import org.arcanum.field.base.AbstractVectorField;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ImmutableListVectorElement<E extends Element, F extends AbstractVectorField> extends ListVectorElement<E, F> {

    public ImmutableListVectorElement(ListVectorElement<E, F> element) {
        super(element.getField());

        // TODO: remove to clear
        this.coeff.clear();
        for (int i = 0, size = field.getN(); i < size; i++)
            coeff.add((E) element.getAt(i).getImmutable());

        this.immutable = true;
    }

    @Override
    public ListVectorElement duplicate() {
        return super.duplicate();
    }

    @Override
    public ListVectorElement getImmutable() {
        return this;
    }

    @Override
    public ListVectorElement set(Element e) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public ListVectorElement set(int value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public ListVectorElement set(BigInteger value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public ListVectorElement twice() {
        return (ListVectorElement) duplicate().twice().getImmutable();
    }

    @Override
    public ListVectorElement setToZero() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public ListVectorElement setToOne() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public ListVectorElement setToRandom() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public ListVectorElement square() {
        return (ListVectorElement) duplicate().square().getImmutable();
    }

    @Override
    public ListVectorElement invert() {
        return (ListVectorElement) duplicate().invert().getImmutable();
    }

    @Override
    public ListVectorElement negate() {
        return (ListVectorElement) duplicate().negate().getImmutable();
    }

    @Override
    public ListVectorElement add(Element e) {
        return (ListVectorElement) duplicate().add(e).getImmutable();
    }

    @Override
    public ListVectorElement mul(Element e) {
        return (ListVectorElement) duplicate().mul(e).getImmutable();
    }

    @Override
    public ListVectorElement mul(BigInteger n) {
        return (ListVectorElement) duplicate().mul(n).getImmutable();
    }

    @Override
    public ListVectorElement mulZn(Element e) {
        return (ListVectorElement) duplicate().mulZn(e).getImmutable();
    }

    @Override
    public ListVectorElement powZn(Element e) {
        return (ListVectorElement) duplicate().powZn(e).getImmutable();
    }

    @Override
    public ListVectorElement setFromHash(byte[] source, int offset, int length) {
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
    public ListVectorElement sub(Element element) {
        return (ListVectorElement) duplicate().sub(element).getImmutable();
    }

    @Override
    public Element div(Element element) {
        return duplicate().div(element).getImmutable();
    }

    @Override
    public ListVectorElement mul(int z) {
        return (ListVectorElement) duplicate().mul(z).getImmutable();
    }

    @Override
    public ListVectorElement sqrt() {
        return (ListVectorElement) duplicate().sqrt().getImmutable();
    }

}
