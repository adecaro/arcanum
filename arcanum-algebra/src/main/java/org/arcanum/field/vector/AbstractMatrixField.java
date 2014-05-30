package org.arcanum.field.vector;

import org.arcanum.Field;
import org.arcanum.field.base.AbstractFieldOver;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class AbstractMatrixField<F extends Field, E extends AbstractMatrixElement> extends AbstractFieldOver<F, E> {

    protected final int n, m;

    protected AbstractMatrixField(SecureRandom random, F targetField, int n, int m) {
        super(random, targetField);

        this.n = n;
        this.m = m;
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public BigInteger getOrder() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public E getNqr() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

}
