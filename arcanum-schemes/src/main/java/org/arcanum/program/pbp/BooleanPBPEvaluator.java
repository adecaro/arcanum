package org.arcanum.program.pbp;

import org.arcanum.program.ProgramEvaluator;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BooleanPBPEvaluator implements ProgramEvaluator<PermutationBranchingProgram, Boolean, Boolean> {


    public Boolean evaluate(PermutationBranchingProgram pbp, Boolean... inputs) {
        int state = 0;

        System.out.println("state = " + state);
        for (int i = 0, len = pbp.getLength(); i < len; i++) {
            state = (inputs[pbp.getVarIndexAt(i)])
                    ? pbp.permuteRightAt(i, state)
                    : pbp.permuteLeftAt(i, state);
            System.out.println("state = " + state);
        }

        System.out.println("state = " + state);

        return state != 0;
    }

}
