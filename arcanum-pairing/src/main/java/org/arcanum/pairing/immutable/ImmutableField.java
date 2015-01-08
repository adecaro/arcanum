package org.arcanum.pairing.immutable;

import org.arcanum.Element;
import org.arcanum.ElementPowPreProcessing;
import org.arcanum.Field;
import org.arcanum.Sampler;
import org.arcanum.field.util.ElementUtils;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public class ImmutableField implements Field<Element> {

    protected Field field;


    public ImmutableField(Field field) {
        this.field = field;
    }


    public Element newElement() {
        return field.newElement().getImmutable();
    }

    public Element newElement(int value) {
        return field.newElement(value).getImmutable();
    }

    public Element newElement(long value) {
        return field.newElement(value).getImmutable();
    }

    public Element newElement(BigInteger value) {
        return field.newElement(value).getImmutable();
    }

    public Element newElement(Element element) {
        return field.newElement(element).getImmutable();
    }

    public Element newZeroElement() {
        return field.newZeroElement().getImmutable();
    }

    public Element newOneElement() {
        return field.newOneElement().getImmutable();
    }

    public Element newElementFromHash(byte[] source, int offset, int length) {
        return field.newElementFromHash(source, offset, length).getImmutable();
    }

    public Element newElementFromBytes(byte[] source) {
        return field.newElementFromBytes(source).getImmutable();
    }

    public Element newElementFromBytes(byte[] source, int offset) {
        return field.newElementFromBytes(source, offset).getImmutable();
    }

    public Element newRandomElement() {
        return field.newRandomElement().getImmutable();
    }

    public Element newElementFromSampler(Sampler<BigInteger> sampler) {
        return newElement(sampler.sample());
    }

    public Element newElementFromObjectSampler(Sampler<Object> sampler) {
        return newElement(sampler.sample());
    }

    public Element newElement(Object value) {
        return field.newElement(value).getImmutable();
    }

    public BigInteger getOrder() {
        return field.getOrder();
    }

    public boolean isOrderOdd() {
        return field.isOrderOdd();
    }

    public Element getNqr() {
        return field.getNqr();
    }

    public int getLengthInBytes() {
        return field.getLengthInBytes();
    }

    public int getLengthInBytes(Element element) {
        return field.getLengthInBytes(element);
    }

    public int getCanonicalRepresentationLengthInBytes() {
        return field.getCanonicalRepresentationLengthInBytes();
    }

    public Element[] twice(Element[] elements) {
        Element[] temp = ElementUtils.duplicate(elements);
        return ElementUtils.cloneImmutable(field.twice(temp));
    }

    public Element[] add(Element[] a, Element[] b) {
        Element[] temp = ElementUtils.duplicate(a);
        return ElementUtils.cloneImmutable(field.add(temp, b));
    }

    public ElementPowPreProcessing getElementPowPreProcessingFromBytes(byte[] source) {
        return new ImmutableElementPowPreProcessing(this, field.getElementPowPreProcessingFromBytes(source));
    }

    public ElementPowPreProcessing getElementPowPreProcessingFromBytes(byte[] source, int offset) {
        return new ImmutableElementPowPreProcessing(this, field.getElementPowPreProcessingFromBytes(source, offset));
    }

    @Override
    public String toString() {
        return "ImmutableField{" +
                "field=" + field +
                '}';
    }
}
