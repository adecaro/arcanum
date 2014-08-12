package org.arcanum.program.pbp.permutation;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class DefaultPermutation implements Permutation {

    private int[] perm;
    private int[] invPerm;


    public DefaultPermutation(int... perm) {
        this.perm = perm;
    }

    public DefaultPermutation(int size) {
        this.perm = new int[size];

        for (int i = 0; i < size; i++)
            perm[i] = i;
    }

    public DefaultPermutation(Permutation... permutations) {
        this.perm = new int[permutations[0].getSize()];

        for (int i = 0; i < perm.length; i++) {
            perm[i] = i;
            for (Permutation permutation : permutations) {
                perm[i] = permutation.permute(perm[i]);
            }
        }
    }


    public int getSize() {
        return perm.length;
    }

    public boolean isCyclic() {
        return false;
    }

    public int permute(int index) {
        return perm[index];
    }

    @Override
    public int permuteInverse(int index) {
        if (invPerm == null) {
            // compute inverse
            this.invPerm = new int[perm.length];
            for (int i = 0; i < invPerm.length; i++) {
                this.invPerm[perm[i]] = i;
            }
        }

        return invPerm[index];
    }

    public Permutation getInverse() {
        int[] reversed = new int[perm.length];

        for (int i = 0; i < reversed.length; i++) {
            reversed[perm[i]] = i;
        }

        return new DefaultPermutation(reversed);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < perm.length; i++) {
            sb.append(perm[i]).append(",");
        }
        sb.append(")");

        return sb.toString();
    }
}
