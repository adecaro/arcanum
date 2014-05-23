package org.arcanum.field.quadratic;

import org.arcanum.Field;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class DegreeTwoExtensionQuadraticField<F extends Field> extends QuadraticField<F, DegreeTwoExtensionQuadraticElement> {

    public DegreeTwoExtensionQuadraticField(SecureRandom random, F targetField) {
        super(random, targetField);
    }


    public DegreeTwoExtensionQuadraticElement newElement() {
        return new DegreeTwoExtensionQuadraticElement(this);
    }

}
