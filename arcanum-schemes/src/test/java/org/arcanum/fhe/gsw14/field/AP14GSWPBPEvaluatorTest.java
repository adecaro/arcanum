package org.arcanum.fhe.gsw14.field;

import junit.framework.TestCase;
import org.arcanum.circuit.BooleanCircuit;
import org.arcanum.circuit.BooleanCircuitEvaluator;
import org.arcanum.permutation.DefaultPermutation;
import org.arcanum.permutation.Permutation;
import org.arcanum.program.pbp.BooleanCircuitToBooleanPBP;
import org.arcanum.program.pbp.BooleanPermutationBranchingProgram;
import org.arcanum.program.pbp.PermutationBranchingProgram;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;

import static org.arcanum.circuit.Gate.Type.*;

public class AP14GSWPBPEvaluatorTest extends TestCase {

    private SecureRandom random;
    private AP14GSW14Field field;

    @Before
    public void setUp() throws Exception {
        random = SecureRandom.getInstance("SHA1PRNG");
        field = new AP14GSW14Field(random, 4, 30);
    }

    @Test
    public void testEvaluation() {
        PermutationBranchingProgram pbp = new BooleanPermutationBranchingProgram();
        Permutation alpha = new DefaultPermutation(1, 2, 3, 4, 0);
        Permutation gamma = new DefaultPermutation(2, 0, 4, 1, 3);
        Permutation identity = new DefaultPermutation(5);

        pbp.addInstruction(0, identity, alpha);
        pbp.addInstruction(1, identity, gamma);
        pbp.addInstruction(0, identity, alpha.getInverse());
        pbp.addInstruction(1, identity, gamma.getInverse());

        AP14GSWPBPEvaluator evaluator = new AP14GSWPBPEvaluator();

        assertEquals(true, !BigInteger.ONE.equals(evaluator.evaluate(pbp, field.newElement(1), field.newElement(1)).toBigInteger()));
    }


    @Test
    public void testCircuitEvaluation() {
        int ell = 4;
        int depth = 4;
        int q = 4;
        BooleanCircuit circuit = new BooleanCircuit(ell, q, depth, new BooleanCircuit.BooleanCircuitGate[]{
                new BooleanCircuit.BooleanCircuitGate(INPUT, 0, 1),
                new BooleanCircuit.BooleanCircuitGate(INPUT, 1, 1),
                new BooleanCircuit.BooleanCircuitGate(INPUT, 2, 1),
                new BooleanCircuit.BooleanCircuitGate(INPUT, 3, 1),

                new BooleanCircuit.BooleanCircuitGate(AND, 4, 2, new int[]{0, 1}),
                new BooleanCircuit.BooleanCircuitGate(AND, 5, 2, new int[]{2, 3}),

                new BooleanCircuit.BooleanCircuitGate(AND, 6, 3 , new int[]{4, 5}),

                new BooleanCircuit.BooleanCircuitGate(NOT, 7, 4, new int[]{6})
        });

        // Convert it to a PBP
        PermutationBranchingProgram pbp = new BooleanCircuitToBooleanPBP().convert(circuit);
        System.out.println("pbp = " + pbp);

        // Verify that they evaluates to the same value.

        AP14GSWPBPEvaluator pbpEvaluator = new AP14GSWPBPEvaluator();
        BooleanCircuitEvaluator circuitEvaluator = new BooleanCircuitEvaluator();

        // Generate the assignment randomly
        boolean x0 = false;
        boolean x1 = true;
        boolean x2 = false;
        boolean x3 = true;

        assertSame(
                circuitEvaluator.evaluate(
                        circuit,
                        x0, x1, x2, x3),
                !BigInteger.ONE.equals(pbpEvaluator.evaluate(
                        pbp,
                        field.newElement(x0 ? 1 : 0),
                        field.newElement(x1 ? 1 : 0),
                        field.newElement(x2 ? 1 : 0),
                        field.newElement(x3 ? 1 : 0)))
        );
    }

}