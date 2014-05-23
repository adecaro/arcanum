package org.arcanum.circuit;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.field.z.ZrField;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.StringTokenizer;

import static org.arcanum.circuit.ArithmeticCircuit.ArithmeticCircuitGate;
import static org.arcanum.circuit.Gate.Type.*;
import static org.junit.Assert.assertEquals;

public class ArithmeticCircuitTest {


    @Test
    public void testMultiplication() throws Exception {
        Field Zq = new ZrField(new SecureRandom(), BigInteger.valueOf(17));

        int ell = 2;
        int q = 1;
        int depth = 2;
        ArithmeticCircuit circuit = new ArithmeticCircuit(ell, q, depth, new ArithmeticCircuitGate[]{
                new ArithmeticCircuitGate(INPUT, 0, 1),
                new ArithmeticCircuitGate(INPUT, 1, 1),
                new ArithmeticCircuitGate(AND, 3, 2, new int[]{0, 1}, Zq.newOneElement()),
        });

        assertEquals(circuit.evaluate(toElement(Zq, "1 1", 2)), Zq.newOneElement());
        assertEquals(circuit.evaluate(toElement(Zq, "2 2", 2)), Zq.newElement(4));
        assertEquals(circuit.evaluate(toElement(Zq, "1 0", 2)), Zq.newZeroElement());
    }

    @Test
    public void testAddition() throws Exception {
        Field Zq = new ZrField(new SecureRandom(), BigInteger.valueOf(17));

        int ell = 2;
        int q = 1;
        int depth = 2;
        ArithmeticCircuit circuit = new ArithmeticCircuit(ell, q, depth, new ArithmeticCircuitGate[]{
                new ArithmeticCircuitGate(INPUT, 0, 1),
                new ArithmeticCircuitGate(INPUT, 1, 1),
                new ArithmeticCircuitGate(OR, 3, 2, new int[]{0, 1}, Zq.newOneElement(), Zq.newOneElement()),
        });

        assertEquals(circuit.evaluate(toElement(Zq, "1 1", 2)), Zq.newElement(2));
        assertEquals(circuit.evaluate(toElement(Zq, "1 0", 2)), Zq.newOneElement());
        assertEquals(circuit.evaluate(toElement(Zq, "1 -1", 2)), Zq.newZeroElement());
    }


    @Test
    public void testCircuit() throws Exception {
        Field Zq = new ZrField(new SecureRandom(), BigInteger.valueOf(17));

        int ell = 4;
        int depth = 3;
        int q = 3;

        ArithmeticCircuit circuit = new ArithmeticCircuit(ell, q, depth, new ArithmeticCircuitGate[]{
                new ArithmeticCircuitGate(INPUT, 0, 1),
                new ArithmeticCircuitGate(INPUT, 1, 1),
                new ArithmeticCircuitGate(INPUT, 2, 1),
                new ArithmeticCircuitGate(INPUT, 3, 1),

                new ArithmeticCircuitGate(AND, 4, 2, new int[]{0, 1}, Zq.newOneElement()),
                new ArithmeticCircuitGate(OR, 5, 2, new int[]{2, 3}, Zq.newOneElement(), Zq.newOneElement()),

                new ArithmeticCircuitGate(AND, 6, 3, new int[]{4, 5}, Zq.newOneElement()),
        });

        assertEquals(circuit.evaluate(toElement(Zq, "1 0 1 1", 4)), Zq.newZeroElement());
        assertEquals(circuit.evaluate(toElement(Zq, "1 2 1 1", 4)), Zq.newElement(4));
    }

    @Test
    public void testCircuitAllOR() throws Exception {
        Field Zq = new ZrField(new SecureRandom(), BigInteger.valueOf(17));

        int ell = 4;
        int depth = 3;
        int q = 3;

        ArithmeticCircuit circuit = new ArithmeticCircuit(ell, q, depth, new ArithmeticCircuitGate[]{
                new ArithmeticCircuitGate(INPUT, 0, 1),
                new ArithmeticCircuitGate(INPUT, 1, 1),
                new ArithmeticCircuitGate(INPUT, 2, 1),
                new ArithmeticCircuitGate(INPUT, 3, 1),

                new ArithmeticCircuitGate(OR, 5, 2, new int[]{2, 3}, Zq.newOneElement(), Zq.newOneElement()),
                new ArithmeticCircuitGate(OR, 4, 2, new int[]{0, 1}, Zq.newOneElement(), Zq.newOneElement()),

                new ArithmeticCircuitGate(OR, 6, 3, new int[]{4, 5}, Zq.newOneElement(), Zq.newOneElement()),
        });

        assertEquals(circuit.evaluate(toElement(Zq, "1 -1 1 -1", 4)), Zq.newZeroElement());
        assertEquals(circuit.evaluate(toElement(Zq, "1 2 1 1", 4)), Zq.newElement(5));
    }

    @Test
    public void testCircuitAllAND() throws Exception {
        Field Zq = new ZrField(new SecureRandom(), BigInteger.valueOf(17));

        int ell = 4;
        int depth = 3;
        int q = 3;

        ArithmeticCircuit circuit = new ArithmeticCircuit(ell, q, depth, new ArithmeticCircuitGate[]{
                new ArithmeticCircuitGate(INPUT, 0, 1),
                new ArithmeticCircuitGate(INPUT, 1, 1),
                new ArithmeticCircuitGate(INPUT, 2, 1),
                new ArithmeticCircuitGate(INPUT, 3, 1),

                new ArithmeticCircuitGate(AND, 5, 2, new int[]{2, 3}, Zq.newOneElement()),
                new ArithmeticCircuitGate(AND, 4, 2, new int[]{0, 1}, Zq.newOneElement()),

                new ArithmeticCircuitGate(AND, 6, 3, new int[]{4, 5}, Zq.newOneElement()),
        });

        assertEquals(circuit.evaluate(toElement(Zq, "1 0 1 0", 4)), Zq.newZeroElement());
        assertEquals(circuit.evaluate(toElement(Zq, "1 2 1 1", 4)), Zq.newElement(2));
    }

    protected Element[] toElement(Field Zq, String assignment, int ell) {
        Element[] elements = new Element[ell];
        StringTokenizer st = new StringTokenizer(assignment, " ");
        int i = 0;
        while (st.hasMoreTokens()) {
            elements[i++] = Zq.newElement(new BigInteger(st.nextToken()));
        }

        return elements;
    }

}