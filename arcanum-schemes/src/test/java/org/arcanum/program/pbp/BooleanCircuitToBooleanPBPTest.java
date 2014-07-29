package org.arcanum.program.pbp;

import org.arcanum.circuit.BooleanCircuit;
import org.arcanum.circuit.BooleanCircuitEvaluator;
import org.arcanum.circuit.smart.SmartBooleanCircuitLoader;
import org.arcanum.program.assignment.BooleanAssignment;
import org.arcanum.program.assignment.BooleanAssignmentGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertSame;

@RunWith(value = Parameterized.class)
public class BooleanCircuitToBooleanPBPTest {

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {"org/arcanum/circuits/bool/gates/or.txt"},
                {"org/arcanum/circuits/bool/gates/and.txt"},
                {"org/arcanum/circuits/bool/gates/nand.txt"},
                {"org/arcanum/circuits/bool/gates/mod2.txt"},
                {"org/arcanum/circuits/bool/circuit.txt"},
                {"org/arcanum/circuits/bool/circuit2.txt"},
                {"org/arcanum/circuits/bool/circuit3.txt"},
                {"org/arcanum/circuits/bool/parity4inputs.txt"},
        };

        return Arrays.asList(data);
    }


    protected String circuitPath;

    public BooleanCircuitToBooleanPBPTest(String circuitPath) {
        this.circuitPath = circuitPath;
    }


    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testEvaluation() {
        // Define a circuit
        BooleanCircuit circuit = new SmartBooleanCircuitLoader().load(
                circuitPath
        );

        // Convert it to a PBP
        PermutationBranchingProgram pbp = new BooleanCircuitToBooleanPBP().convert(circuit);
        System.out.println("pbp = " + pbp);

        // Verify that they evaluates to the same value.

        BooleanPBPEvaluator pbpEvaluator = new BooleanPBPEvaluator();
        BooleanCircuitEvaluator circuitEvaluator = new BooleanCircuitEvaluator();

        BooleanAssignmentGenerator assignmentGenerator = new BooleanAssignmentGenerator(circuit.getNumInputs());
        for (BooleanAssignment assignment : assignmentGenerator) {
            System.out.println("assignment = " + assignment);
            assertSame(
                    circuitEvaluator.evaluate(circuit, assignment),
                    pbpEvaluator.evaluate(pbp, assignment)
            );
        }
    }

}