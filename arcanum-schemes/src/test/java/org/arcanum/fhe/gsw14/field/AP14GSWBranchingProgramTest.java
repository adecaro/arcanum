package org.arcanum.fhe.gsw14.field;

import junit.framework.TestCase;
import org.arcanum.Element;
import org.arcanum.permutation.CyclicPermutation;
import org.arcanum.permutation.DefaultPermutation;
import org.arcanum.permutation.Permutation;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;

public class AP14GSWBranchingProgramTest extends TestCase {

    private SecureRandom random;
    private AP14GSW14Field field;

    @Before
    public void setUp() throws Exception {
        random = SecureRandom.getInstance("SHA1PRNG");
        field = new AP14GSW14Field(random, 4, 30);
    }

    @Test
    public void testEvaluation() {
        AP14GSWBranchingProgram program = new AP14GSWBranchingProgram() ;

        Permutation alpha = new DefaultPermutation(2, 4, 1, 0, 3);
        Permutation gamma = new DefaultPermutation(1, 3, 0, 4, 2);
        Permutation identity = new CyclicPermutation(5);

        for (int i = 0; i < 100; i++) {
            program.addStep(0, identity, alpha);
            program.addStep(1, identity, gamma);
            program.addStep(0, identity, alpha.reverse());
            program.addStep(1, identity, gamma.reverse());
        }

        Element result = program.evaluate(
                field.newElement(1),
                field.newElement(0)
        );

        boolean eval = program.evaluate(true, false);
        System.out.println("result.toBigInteger() = " + result.toBigInteger());

        assertEquals(eval, BigInteger.ONE.equals(result.toBigInteger()));

    }

}