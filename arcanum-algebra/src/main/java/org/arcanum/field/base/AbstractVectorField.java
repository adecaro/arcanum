package org.arcanum.field.base;

import org.arcanum.Field;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class AbstractVectorField<F extends Field, E extends AbstractVectorElement> extends AbstractFieldOver<F, E> {

    protected final int n, lenInBytes;

    public AbstractVectorField(SecureRandom random, F targetField, int n) {
        super(random, targetField);

        this.n = n;
        this.lenInBytes = n * targetField.getLengthInBytes();
    }


    public BigInteger getOrder() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public E getNqr() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public int getLengthInBytes() {
        return lenInBytes;
    }

    public int getN() {
        return n;
    }

}
