package org.arcanum.field.vector;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Sampler;
import org.arcanum.field.base.AbstractField;
import org.arcanum.field.base.AbstractVectorField;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class VectorField<F extends Field> extends AbstractVectorField<F, ListVectorElement> {


    public static Element newRandomElement(Field targetField, int n) {
        return new VectorField<Field>(((AbstractField)targetField).getRandom(),targetField, n).newRandomElement();
    }


    public VectorField(SecureRandom random, F targetField, int n) {
        super(random, targetField, n);
    }


    public ListVectorElement newElement() {
        return new ListVectorElement(this);
    }

    public ListVectorElement newElementFromSampler(Sampler<BigInteger> sampler) {
        return new ListVectorElement(this, sampler);
    }

    public VectorField<F> newField(int n) {
        return new VectorField<F>(random, targetField, n);
    }

    public ListVectorElement newPrimitiveElement() {
        ListVectorElement g = newElement();

        BigInteger value = BigInteger.ONE;
        for (int i = 0; i < n; i++) {
            g.getAt(i).set(value);
            value = value.shiftLeft(1);
        }

        return g;
    }

}
