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

        for (BooleanGate gate : circuit) {
            int index = gate.getIndex();
            System.out.println("index = " + index);

            switch (gate.getType()) {
                case INPUT:
                    BooleanPermutationBranchingProgram inputPBP = new BooleanPermutationBranchingProgram();
                    inputPBP.addInstruction(index, identity, commutator);

                    pbps.put(index, inputPBP);
                    break;

                case AND:
                    BooleanPermutationBranchingProgram leftPBP = pbps.get(gate.getInputIndexAt(0));
                    BooleanPermutationBranchingProgram rightPBP = pbps.get(gate.getInputIndexAt(1));

                    System.out.println("leftPBP  = " + leftPBP.getLength());
                    System.out.println("rightPBP = " + rightPBP.getLength());

                    BooleanPermutationBranchingProgram andPBP = new BooleanPermutationBranchingProgram();
                    // alpha accept
                    andPBP.addProgram(leftPBP.applyPerm(commToAlpha));
                    // gamma accept
                    andPBP.addProgram(rightPBP.applyPerm(commToGamma));
                    // alpha^-1 accept
                    andPBP.addProgram(leftPBP.applyPerm(commToAlphaInverse));
                    // gamma^-1 accept
                    andPBP.addProgram(rightPBP.applyPerm(commToGammaInverse));

//                    System.out.println("andPBP = " + andPBP);
                    System.out.println("andPBP   = " + andPBP.getLength());

                    pbps.put(index, andPBP);
                    break;

                case NOT:
                case INV:
                    BooleanPermutationBranchingProgram pbp = pbps.get(gate.getInputIndexAt(0));
                    pbp = pbp.negate(commutator.getInverse()).applyPerm(commInvToComm);

                    pbps.put(index, pbp);
                    break;
            }
        }

        return pbps.get(circuit.getOutputGate().getIndex());
    }

}
