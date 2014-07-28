package org.arcanum.program.pbp;

import org.arcanum.program.Program;
import org.arcanum.program.pbp.permutation.Permutation;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public interface PermutationBranchingProgram extends Program {


    PermutationBranchingProgram addInstruction(int varIndex, Permutation leftPerm, Permutation rightPerm);

    PermutationBranchingProgram addInstruction(int varIndex, Permutation perm);


    int getLength();

    int getVarIndexAt(int index);

    int permuteLeftAt(int index, int value);

    int permuteLeftInverseAt(int index, int value);

    int permuteRightAt(int index, int value);

    int permuteRightInverseAt(int index, int value);
}
