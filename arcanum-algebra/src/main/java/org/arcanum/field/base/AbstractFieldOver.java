package org.arcanum.field.base;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.FieldOver;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class AbstractFieldOver<F extends Field, E extends Element> extends AbstractField<E> implements FieldOver<F, E> {

    protected final F targetField;


    protected AbstractFieldOver(SecureRandom random, F targetField) {
        super(random);
        this.targetField = targetField;
    }


    public F getTargetField() {
        return targetField;
    }

}
