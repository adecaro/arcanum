package org.arcanum.field.poly;

import org.arcanum.Field;
import org.arcanum.field.base.AbstractFieldOver;

import java.security.SecureRandom;
import java.util.List;

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

    public PolyElement newElement(Object value) {
        return new PolyElement(this, (List) value);
    }

}
