package org.arcanum.field.vector;

import org.arcanum.Element;
import org.arcanum.ElementPowPreProcessing;
import org.arcanum.Field;
import org.arcanum.Vector;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class VectorElementPowPreProcessing implements ElementPowPreProcessing {
    protected final Field field;
    protected final ElementPowPreProcessing[] processings;


    public VectorElementPowPreProcessing(Vector vector) {
        this.field = vector.getField();
        this.processings = new ElementPowPreProcessing[vector.getSize()];
        for (int i = 0; i < processings.length; i++) {
            processings[i] = vector.getAt(i).getElementPowPreProcessing();
        }
    }


    public Element pow(BigInteger n) {
        List<Element> coeff = new ArrayList<Element>(processings.length);
        for (ElementPowPreProcessing processing : processings) {
            coeff.add(processing.pow(n));
        }
        return field.newElement(coeff);
    }

    public Element powZn(Element n) {
        List<Element> coeff = new ArrayList<Element>(processings.length);
        for (ElementPowPreProcessing processing : processings) {
            coeff.add(processing.powZn(n));
        }
        return field.newElement(coeff);
    }

    public byte[] toBytes() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Field getField() {
        return field;
    }
}
