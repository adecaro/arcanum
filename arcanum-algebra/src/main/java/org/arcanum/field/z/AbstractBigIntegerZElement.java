package org.arcanum.field.z;

import org.arcanum.field.base.AbstractElement;
import org.arcanum.field.base.AbstractField;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class AbstractBigIntegerZElement<F extends AbstractField> extends AbstractElement<F> {

    public BigInteger value;

    protected AbstractBigIntegerZElement(F field) {
        super(field);
    }

    public BigInteger toBigInteger() {
        return value;
    }

}
