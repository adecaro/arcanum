package org.arcanum.program.pbp;

import org.arcanum.circuit.BooleanCircuit;
import org.arcanum.circuit.BooleanGate;
import org.arcanum.permutation.DefaultPermutation;
import org.arcanum.permutation.Permutation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BooleanCircuitToBooleanPBP {

    protected Permutation alpha, gamma, commutator, identity;
    protected Permutation commToAlpha, commToGamma;
    protected Permutation commToAlphaInverse, commToGammaInverse;
    protected Permutation commInvToComm;


    public BooleanCircuitToBooleanPBP() {
        this.alpha = new DefaultPermutation(1, 2, 3, 4, 0);
        this.gamma = new DefaultPermutation(2, 0, 4, 1, 3);
        this.commutator = new DefaultPermutation(2, 4, 1, 0, 3);
        this.identity = new DefaultPermutation(5);

        // commutator to alpha
        this.commToAlpha = new DefaultPermutation(1, 4, 3, 0, 2);
        // commutator to gamma
        this.commToGamma = new DefaultPermutation(1, 2, 4, 0, 3);
        // commutator to alphaInverse
        this.commToAlphaInverse = new DefaultPermutation(1, 2, 0, 3, 4);
        // commutator to gammaInverse
        this.commToGammaInverse = new DefaultPermutation(1, 4, 2, 3, 0);
        // commutatorInv to commutator
        this.commInvToComm = new DefaultPermutation(1, 0, 2, 4, 3);

    }

    public PermutationBranchingProgram convert(BooleanCircuit circuit) {
        Map<Integer, BooleanPermutationBranchingProgram> pbps = new HashMap<Integer, BooleanPermutationBranchingProgram>();

        // Assume that all the gates have fan-in 2.
        for (BooleanGate gate : circuit) {
            int index = gate.getIndex();
//            System.out.println("index = " + index);

            switch (gate.getType()) {
                case INPUT:
                    BooleanPermutationBranchingProgram inputPBP = new BooleanPermutationBranchingProgram();
                    inputPBP.addInstruction(index, identity, commutator);

                    pbps.put(index, inputPBP);
                    break;

                case AND:
                    pbps.put(index, and(pbps.get(gate.getInputIndexAt(0)), pbps.get(gate.getInputIndexAt(1))));
                    break;

                case OR:
                    pbps.put(index, or(pbps.get(gate.getInputIndexAt(0)), pbps.get(gate.getInputIndexAt(1))));
                    break;

                case NAND:
                    pbps.put(index, nand(pbps.get(gate.getInputIndexAt(0)), pbps.get(gate.getInputIndexAt(1))));
                    break;

                case MOD2:
                    pbps.put(index, mod2(pbps.get(gate.getInputIndexAt(0)), pbps.get(gate.getInputIndexAt(1))));
                    break;

                case NOT:
                case INV:
                    pbps.put(index, not(pbps.get(gate.getInputIndexAt(0))));
                    break;


                default:
                    throw new IllegalArgumentException("Gate not recognized!!!");
            }
        }

        return pbps.get(circuit.getOutputGate().getIndex());
    }


    protected BooleanPermutationBranchingProgram and(BooleanPermutationBranchingProgram a, BooleanPermutationBranchingProgram b) {
        BooleanPermutationBranchingProgram andPBP = new BooleanPermutationBranchingProgram();
        // alpha accept
        andPBP.addProgram(a.applyPerm(commToAlpha));
        // gamma accept
        andPBP.addProgram(b.applyPerm(commToGamma));
        // alpha^-1 accept
        andPBP.addProgram(a.applyPerm(commToAlphaInverse));
        // gamma^-1 accept
        andPBP.addProgram(b.applyPerm(commToGammaInverse));

        return andPBP;
    }

    protected BooleanPermutationBranchingProgram not(BooleanPermutationBranchingProgram pbp) {
        return pbp.negate(commutator.getInverse()).applyPerm(commInvToComm);
    }

    protected BooleanPermutationBranchingProgram nand(BooleanPermutationBranchingProgram a, BooleanPermutationBranchingProgram b) {
        return not(and(a, b));
    }

    protected BooleanPermutationBranchingProgram or(BooleanPermutationBranchingProgram a, BooleanPermutationBranchingProgram b) {
        return nand(nand(a, a), nand(b, b));
    }

    protected BooleanPermutationBranchingProgram mod2(BooleanPermutationBranchingProgram a, BooleanPermutationBranchingProgram b) {
        return or(and(a, not(b)), and(not(a), b));
    }

}
