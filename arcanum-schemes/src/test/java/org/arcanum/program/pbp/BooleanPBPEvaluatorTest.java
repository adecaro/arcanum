package org.arcanum.program.pbp;

import junit.framework.TestCase;
import org.arcanum.program.pbp.permutation.DefaultPermutation;
import org.arcanum.program.pbp.permutation.Permutation;
import org.junit.Test;

public class BooleanPBPEvaluatorTest extends TestCase {

    @Test
    public void testEvaluation() throws Exception {
        PermutationBranchingProgram pbp = new BooleanPermutationBranchingProgram();
        Permutation alpha = new DefaultPermutation(1, 2, 3, 4, 0);
        Permutation gamma = new DefaultPermutation(2, 0, 4, 1, 3);
        Permutation identity = new DefaultPermutation(5);

        pbp.addInstruction(0, identity, alpha);
        pbp.addInstruction(1, identity, gamma);
        pbp.addInstruction(0, identity, alpha.getInverse());
        pbp.addInstruction(1, identity, gamma.getInverse());

        BooleanPBPEvaluator evaluator = new BooleanPBPEvaluator();

        assertEquals(Boolean.TRUE, evaluator.evaluate(pbp, true, true));
    }
}