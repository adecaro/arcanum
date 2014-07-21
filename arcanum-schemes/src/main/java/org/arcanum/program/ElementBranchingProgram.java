package org.arcanum.program;

import org.arcanum.Element;
import org.arcanum.permutation.CyclicPermutation;
import org.arcanum.permutation.DefaultPermutation;
import org.arcanum.permutation.Permutation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class ElementBranchingProgram implements Program<Element, Element> {

    protected List<Integer> vars;
    protected List<Permutation> leftPerms;
    protected List<Permutation> rightPerms;


    public ElementBranchingProgram() {
        this.vars = new ArrayList<Integer>();
        this.leftPerms = new ArrayList<Permutation>();
        this.rightPerms = new ArrayList<Permutation>();
    }


    public ElementBranchingProgram addStep(int varIndex, Permutation leftPerm, Permutation rightPerm) {
        vars.add(varIndex);
        leftPerms.add(leftPerm);
        rightPerms.add(rightPerm);

        return this;
    }

    public boolean evaluate(boolean... inputs) {
        int state = 0;
        System.out.println("state = " + state);
        for (int i = 0, len = vars.size(); i < len; i++) {
            state = (inputs[vars.get(i)]) ? rightPerms.get(i).permute(state)
                    : leftPerms.get(i).permute(state);
            System.out.println("state = " + state);
        }

        System.out.println("state = " + state);

        return state != 0;
    }


    public static void main(String[] args) {
        ElementBranchingProgram program = new ElementBranchingProgram() {
            public Element evaluate(Element... inputs) {
                return null;
            }
        };

        Permutation alpha = new DefaultPermutation(1, 2, 3, 4, 0);
        Permutation gamma = new DefaultPermutation(2, 0, 4, 1, 3);
        Permutation identity = new CyclicPermutation(5);

        program.addStep(0, identity, alpha);
        program.addStep(1, identity, gamma);
        program.addStep(0, identity, alpha.reverse());
        program.addStep(1, identity, gamma.reverse());

        program.evaluate(true, true);
    }

}
