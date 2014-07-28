package org.arcanum.fhe.gsw14.field;

import org.arcanum.Element;
import org.arcanum.field.vector.ArrayMatrixElement;
import org.arcanum.program.pbp.permutation.Permutation;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AP14HESymGroupElement extends ArrayMatrixElement<AP14GSW14Element, AP14HESymGroupField> {

    public AP14HESymGroupElement(AP14HESymGroupField field, AP14GSW14Element[][] elements) {
        super(field, elements);
    }


    public Element apply(Object o) {
        if (o instanceof Permutation) {
            Permutation perm = (Permutation) o;

            Element result = getAt(0, perm.permute(0)).duplicate();
            for (int i = 1, size = perm.getSize(); i < size; i++) {
                int s = perm.permute(i);

            }
        }

        throw new IllegalStateException("Input not recognized!!!");
    }
}
