package org.arcanum.program.pbp;

import org.arcanum.permutation.DefaultPermutation;
import org.arcanum.permutation.Permutation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BooleanPermutationBranchingProgram implements PermutationBranchingProgram {

    protected List<Integer> vars;
    protected List<Permutation> leftPerms;
    protected List<Permutation> rightPerms;


    public BooleanPermutationBranchingProgram() {
        this.vars = new ArrayList<Integer>();
        this.leftPerms = new ArrayList<Permutation>();
        this.rightPerms = new ArrayList<Permutation>();
    }


    public BooleanPermutationBranchingProgram addInstruction(int varIndex, Permutation leftPerm, Permutation rightPerm) {
        vars.add(varIndex);
        leftPerms.add(leftPerm);
        rightPerms.add(rightPerm);

        return this;
    }

    public BooleanPermutationBranchingProgram addInstruction(int varIndex, Permutation perm) {
        vars.add(varIndex);
        leftPerms.add(perm);
        rightPerms.add(perm);

        return this;
    }


    public int getLength() {
        return vars.size();
    }

    public int getVarIndexAt(int index) {
        return vars.get(index);
    }

    public int permuteLeftAt(int index, int value) {
        return leftPerms.get(index).permute(value);
    }

    public int permuteRightAt(int index, int value) {
        return rightPerms.get(index).permute(value);
    }

    public int permuteLeftInverseAt(int index, int value) {
        // TODO: optimeze
        return leftPerms.get(index).getInverse().permute(value);
    }

    public int permuteRightInverseAt(int index, int value) {
        return rightPerms.get(index).getInverse().permute(value);
    }

    public void addProgram(BooleanPermutationBranchingProgram pbp) {
        for (int i = 0; i < pbp.getLength(); i++) {
             addInstruction(
                     pbp.vars.get(i),
                     pbp.leftPerms.get(i),
                     pbp.rightPerms.get(i)
             );
        }
    }

    public BooleanPermutationBranchingProgram applyPerm(Permutation perm) {
        BooleanPermutationBranchingProgram result = new BooleanPermutationBranchingProgram();

        Permutation permInverse = perm.getInverse();

        for (int i = 0; i < getLength(); i++) {
            result.addInstruction(
                    vars.get(i),
                    new DefaultPermutation(perm, leftPerms.get(i), permInverse),
                    new DefaultPermutation(perm, rightPerms.get(i), permInverse)
            );
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("{\n");

        for (int i = 0; i < getLength(); i++) {
            sb.append("<").append(vars.get(i))
                    .append(",").append(leftPerms.get(i))
                    .append(",").append(rightPerms.get(i))
                    .append(">\n");
        }

        return sb.append("}").toString();
    }

    public BooleanPermutationBranchingProgram negate(Permutation permutation) {
        BooleanPermutationBranchingProgram result = new BooleanPermutationBranchingProgram();
        result.addProgram(this);

        int indexLast = getLength() - 1;

        result.leftPerms.add(
                new DefaultPermutation(
                        result.leftPerms.remove(indexLast), permutation
                )
        );

        result.rightPerms.add(
                new DefaultPermutation(
                        result.rightPerms.remove(indexLast), permutation
                )
        );

        return result;


    }
}
