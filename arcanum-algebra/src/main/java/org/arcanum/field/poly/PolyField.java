package org.arcanum.field.poly;

import org.arcanum.Field;
import org.arcanum.field.base.AbstractFieldOver;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PolyField<F extends Field> extends AbstractFieldOver<F, PolyElement> {


    public PolyField(SecureRandom random, F targetField) {
        super(random, targetField);
    }

    public PolyField(F targetField) {
        super(new SecureRandom(), targetField);
    }


    public PolyElement newElement() {
        return new PolyElement(this);
    }

    public BigInteger getOrder() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public PolyElement getNqr() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public int getLengthInBytes() {
        throw new IllegalStateException("Not Implemented yet!");
    }

}
