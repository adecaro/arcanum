package org.arcanum.program.pbp;

import junit.framework.TestCase;
import org.arcanum.circuit.BooleanCircuit;
import org.arcanum.circuit.BooleanCircuitEvaluator;
import org.arcanum.circuit.smart.SmartBooleanCircuitLoader;
import org.junit.Before;
import org.junit.Test;

public class BooleanCircuitToBooleanPBPTest extends TestCase {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testEvaluation() {
        // Define a circuit
        BooleanCircuit circuit =new SmartBooleanCircuitLoader().load(
                "org/arcanum/circuits/circuit2.txt"
        );

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