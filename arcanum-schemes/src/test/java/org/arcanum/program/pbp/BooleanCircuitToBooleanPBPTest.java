package org.arcanum.program.pbp;

import junit.framework.TestCase;
import org.arcanum.circuit.BooleanCircuit;
import org.arcanum.circuit.BooleanCircuitEvaluator;
import org.junit.Before;
import org.junit.Test;

import static org.arcanum.circuit.BooleanCircuit.BooleanCircuitGate;
import static org.arcanum.circuit.Gate.Type.*;

public class BooleanCircuitToBooleanPBPTest extends TestCase {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testEvaluation() {
        // Define a circuit
        int ell = 4;
        int depth = 4;
        int q = 4;
        BooleanCircuit circuit = new BooleanCircuit(ell, q, depth, new BooleanCircuitGate[]{
                new BooleanCircuitGate(INPUT, 0, 1),
                new BooleanCircuitGate(INPUT, 1, 1),
                new BooleanCircuitGate(INPUT, 2, 1),
                new BooleanCircuitGate(INPUT, 3, 1),

                new BooleanCircuitGate(AND, 4, 2, new int[]{0, 1}),
                new BooleanCircuitGate(AND, 5, 2, new int[]{2, 3}),

                new BooleanCircuitGate(AND, 6, 3 , new int[]{4, 5}),

                new BooleanCircuitGate(NOT, 7, 4, new int[]{6})
        });

        // Convert it to a PBP
        PermutationBranchingProgram pbp = new BooleanCircuitToBooleanPBP().convert(circuit);
        System.out.println("pbp = " + pbp);

        // Verify that they evaluates to the same value.

        BooleanPBPEvaluator pbpEvaluator = new BooleanPBPEvaluator();
        BooleanCircuitEvaluator circuitEvaluator = new BooleanCircuitEvaluator();

        // Generate the assignment randomly
        boolean x0 = false;
        boolean x1 = true;
        boolean x2 = false;
        boolean x3 = true;

        assertSame(
                circuitEvaluator.evaluate(circuit, x0, x1, x2, x3),
                pbpEvaluator.evaluate(pbp, x0, x1, x2, x3)
        );

    }


}