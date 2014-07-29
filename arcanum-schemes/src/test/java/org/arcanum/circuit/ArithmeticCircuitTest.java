package org.arcanum.circuit;

import org.arcanum.Field;
import org.arcanum.circuit.smart.SmartArithmeticCircuitLoader;
import org.arcanum.field.z.ZrField;
import org.arcanum.program.assignment.ElementAssignment;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;

public class ArithmeticCircuitTest {

    private Field Zq;
    private ArithmeticCircuitEvaluator evaluator;

    @Before
    public void setUp() {
        this.Zq = new ZrField(new SecureRandom(), BigInteger.valueOf(17));
        this.evaluator = new ArithmeticCircuitEvaluator();
    }

    @Test
    public void testMultiplication() throws Exception {
        ArithmeticCircuit circuit = new SmartArithmeticCircuitLoader().load(
                Zq, "org/arcanum/circuits/arithmetic/gates/and.txt"
        );

        assertEquals(evaluator.evaluate(circuit, new ElementAssignment(Zq, 1, 1)), Zq.newOneElement());
        assertEquals(evaluator.evaluate(circuit, new ElementAssignment(Zq, 2, 2)), Zq.newElement(4));
        assertEquals(evaluator.evaluate(circuit, new ElementAssignment(Zq, 1, 0)), Zq.newZeroElement());
    }

    @Test
    public void testAddition() throws Exception {
        ArithmeticCircuit circuit = new SmartArithmeticCircuitLoader().load(
                Zq, "org/arcanum/circuits/arithmetic/gates/or.txt"
        );

        assertEquals(evaluator.evaluate(circuit, new ElementAssignment(Zq, 1, 1)), Zq.newElement(2));
        assertEquals(evaluator.evaluate(circuit, new ElementAssignment(Zq, 1, 0)), Zq.newOneElement());
        assertEquals(evaluator.evaluate(circuit, new ElementAssignment(Zq, 1, -1)), Zq.newZeroElement());
    }


    /* TODO
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

        assertEquals(evaluator.evaluate(circuit, new ElementAssignment(Zq, 1, 0, 1, 1)), Zq.newZeroElement());
        assertEquals(evaluator.evaluate(circuit, new ElementAssignment(Zq, 1, 2, 1, 1)), Zq.newElement(4));
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

        assertEquals(evaluator.evaluate(circuit, new ElementAssignment(Zq, 1, -1, 1, -1)), Zq.newZeroElement());
        assertEquals(evaluator.evaluate(circuit, new ElementAssignment(Zq, 1, 2, 1, 1)), Zq.newElement(5));
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

        assertEquals(evaluator.evaluate(circuit, new ElementAssignment(Zq, 1, 0, 1, 0)), Zq.newZeroElement());
        assertEquals(evaluator.evaluate(circuit, new ElementAssignment(Zq, 1, 2, 1, 1)), Zq.newElement(2));
    }

    */
}