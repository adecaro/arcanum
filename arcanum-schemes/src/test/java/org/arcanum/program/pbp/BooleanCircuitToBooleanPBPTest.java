package org.arcanum.program.pbp;

import org.arcanum.program.assignment.BooleanAssignment;
import org.arcanum.program.assignment.BooleanAssignmentGenerator;
import org.arcanum.program.circuit.BooleanCircuit;
import org.arcanum.program.circuit.BooleanCircuitEvaluator;
import org.arcanum.program.circuit.smart.SmartBooleanCircuitLoader;
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
                {"org/arcanum/program/circuit/bool/gates/or.txt"},
                {"org/arcanum/program/circuit/bool/gates/and.txt"},
                {"org/arcanum/program/circuit/bool/gates/nand.txt"},
                {"org/arcanum/program/circuit/bool/gates/mod2.txt"},
                {"org/arcanum/program/circuit/bool/circuit.txt"},
                {"org/arcanum/program/circuit/bool/circuit2.txt"},
                {"org/arcanum/program/circuit/bool/circuit3.txt"},
                {"org/arcanum/program/circuit/bool/parity4inputs.txt"},
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