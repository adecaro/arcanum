package org.arcanum.program.assignment;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.program.Assignment;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ElementAssignment implements Assignment<Element> {

    private BooleanAssignment assignment;
    private Element[] elements;

    public ElementAssignment(Element[] elements) {
        this.elements = elements;
    }

    public ElementAssignment(Field field, BooleanAssignment assignment) {
        this.assignment = assignment;

        this.elements = new Element[assignment.getLength()];
        for (int i = 0; i < elements.length; i++)
            elements[i] = field.newElement(assignment.getAt(i) ? 1 : 0);
    }

    public ElementAssignment(Field field, int... inputs) {
        this.assignment = null;

        this.elements = new Element[inputs.length];
        for (int i = 0; i < elements.length; i++)
            elements[i] = field.newElement(inputs[i]);
    }


    public int getLength() {
        return elements.length;
    }

    public Element getAt(int index) {
        return elements[index];
    }

}
