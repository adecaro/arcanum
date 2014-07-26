package org.arcanum.fhe.gsw14.field;

import org.arcanum.Element;
import org.arcanum.circuit.BooleanCircuit;
import org.arcanum.circuit.BooleanCircuitEvaluator;
import org.arcanum.circuit.smart.SmartBooleanCircuitLoader;
import org.arcanum.program.pbp.BooleanCircuitToBooleanPBP;
import org.arcanum.program.pbp.PermutationBranchingProgram;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;

import static org.junit.Assert.assertSame;

public class AP14GSWPBPNSBooleanCircuitLoaderTest {

    private SecureRandom random;
    private AP14GSW14Field field;

    @Before
    public void setUp() throws Exception {
        random = SecureRandom.getInstance("SHA1PRNG");
        field = new AP14GSW14Field(random, 4, 30);
    }

    @Test
    public void testLoad() throws Exception {
        SmartBooleanCircuitLoader loader = new SmartBooleanCircuitLoader();
        BooleanCircuit circuit = loader.load("org/arcanum/circuits/comparator_32bit_signed_lteq.txt");

        // Convert it to a PBP
        PermutationBranchingProgram pbp = new BooleanCircuitToBooleanPBP().convert(circuit);
        System.out.println("pbp = " + pbp);

        // Verify that they evaluates to the same value.

        AP14GSWPBPEvaluator pbpEvaluator = new AP14GSWPBPEvaluator();
        BooleanCircuitEvaluator circuitEvaluator = new BooleanCircuitEvaluator();


        Boolean[] x = new Boolean[64];
        Element[] xEs = new Element[64];

        for (int i = 0; i < 32; i++) {
            x[i] = false;
            xEs[i] = field.newElement(x[i] ? 1 : 0);

            x[32 + i] = true;
            xEs[32 + i] = field.newElement(x[32 + i] ? 1 : 0);
        }


        assertSame(
                circuitEvaluator.evaluate(circuit, x),
                !BigInteger.ONE.equals(pbpEvaluator.evaluate(pbp, xEs))
        );
    }
}