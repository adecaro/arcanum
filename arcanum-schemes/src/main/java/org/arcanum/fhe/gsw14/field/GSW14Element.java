package org.arcanum.fhe.gsw14.field;

import org.arcanum.Element;
import org.arcanum.Matrix;
import org.arcanum.field.base.AbstractElement;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GSW14Element extends AbstractElement<GSW14Field> {

    private Matrix value;


    public GSW14Element(GSW14Field field, Matrix value) {
        super(field);

        this.value = value;
    }


    public Element duplicate() {
        return null;
    }

    public Element set(Element value) {
        return null;
    }

    public Element set(int value) {
        return null;
    }

    public Element set(BigInteger value) {
        return null;
    }

    public BigInteger toBigInteger() {
        return field.decrypt(value);
    }

    public Element setToRandom() {
        return null;
    }

    public Element setFromHash(byte[] source, int offset, int length) {
        return null;
    }

    public Element setToZero() {
        return null;
    }

    public boolean isZero() {
        return false;
    }

    public Element setToOne() {
        return null;
    }

    public boolean isEqual(Element value) {
        return false;
    }

    public boolean isOne() {
        return false;
    }

    public Element invert() {
        return null;
    }

    public Element negate() {
        return null;
    }

    public Element add(Element element) {
        this.value.add(((GSW14Element)element).value);

        return this;
    }

    public Element mul(Element element) {
        this.value = (Matrix) this.value.mul(
                field.gInvert(((GSW14Element) element).value)
        );

        return this;
    }

    public Element mul(BigInteger n) {
        return null;
    }

    public boolean isSqr() {
        return false;
    }

    public int sign() {
        return 0;
    }
}
