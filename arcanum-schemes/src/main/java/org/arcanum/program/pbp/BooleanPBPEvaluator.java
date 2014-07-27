package org.arcanum.program.pbp;

import org.arcanum.program.AbstractProgramEvaluator;
import org.arcanum.program.Assignment;
import org.arcanum.program.assignment.BooleanAssignment;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BooleanPBPEvaluator extends AbstractProgramEvaluator<PermutationBranchingProgram, Boolean, Boolean> {


    public Boolean evaluate(PermutationBranchingProgram pbp, Boolean... inputs) {
        return evaluate(pbp, new BooleanAssignment(inputs));
    }

    @Override
    public Boolean evaluate(PermutationBranchingProgram pbp, Assignment<Boolean> assignment) {
        int state = 0;

//        System.out.println("state = " + state);
        for (int i = 0, len = pbp.getLength(); i < len; i++) {
            state = (assignment.getAt(pbp.getVarIndexAt(i)))
                    ? pbp.permuteRightAt(i, state)
                    : pbp.permuteLeftAt(i, state);
//            System.out.println("state = " + state);
        }

//        System.out.println("state = " + state);

        return state != 0;
    }
}
