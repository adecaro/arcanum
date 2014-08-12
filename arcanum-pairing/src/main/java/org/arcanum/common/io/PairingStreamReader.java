package org.arcanum.common.io;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.pairing.Pairing;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public class PairingStreamReader extends ElementStreamReader {

    private Pairing pairing;


    public PairingStreamReader(Pairing pairing, byte[] buffer, int offset) {
        super(buffer, offset);

        this.pairing = pairing;
    }


    public Element[] readElements(int... ids) {
        Element[] elements = new Element[ids.length];

        for (int i = 0; i < ids.length; i++) {
            Field field = pairing.getFieldAt(ids[i]);
            elements[i] = field.newElementFromBytes(buffer, cursor);
            jump(field.getLengthInBytes(elements[i]));
        }

        return elements;
    }

    public Element[] readElements(int id, int count) {
        Element[] elements = new Element[count];

        Field field = pairing.getFieldAt(id);
        for (int i = 0; i < count; i++) {
            elements[i] = field.newElementFromBytes(buffer, cursor);
            jump(field.getLengthInBytes(elements[i]));
        }

        return elements;
    }

    public Element[] readG1Elements(int count) {
        return readElements(1, count);
    }


    public Element readG1Element() {
        Element element = pairing.getG1().newElementFromBytes(buffer, cursor);
        jump(pairing.getG1().getLengthInBytes(element));

        return element;
    }

    public Element readGTElement() {
        Element element = pairing.getGT().newElementFromBytes(buffer, cursor);
        jump(pairing.getGT().getLengthInBytes(element));
        return element;
    }

    public Element readFieldElement(Field field) {
        Element element = field.newElementFromBytes(buffer, cursor);
        jump(field.getLengthInBytes(element));
        return element;
    }

}
