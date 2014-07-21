package org.arcanum.permutation;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class DefaultPermutation implements Permutation {

    private int[] perm;


    public DefaultPermutation(int... perm) {
        this.perm = perm;
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

    public Permutation reverse() {
        int[] reversed = new int[perm.length];

        for (int i = 0; i < reversed.length; i++) {
            reversed[perm[i]] = i;
        }

        return new DefaultPermutation(reversed);
    }

}
