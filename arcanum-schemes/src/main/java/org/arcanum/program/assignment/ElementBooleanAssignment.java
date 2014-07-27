package org.arcanum.program.assignment;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.program.Assignment;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ElementBooleanAssignment implements Assignment<Element> {

    private BooleanAssignment assignment;
    private Element[] elements;

    public ElementBooleanAssignment(Element[] elements) {
        this.elements = elements;
    }

    public ElementBooleanAssignment(Field field, BooleanAssignment assignment) {
        this.assignment = assignment;

        this.elements = new Element[assignment.getLength()];
        for (int i = 0; i < elements.length; i++)
            elements[i] = field.newElement(assignment.getAt(i) ? 1 : 0);
    }

    public int getLength() {
        return elements.length;
    }

    public Element getAt(int index) {
        return elements[index];
    }

}
