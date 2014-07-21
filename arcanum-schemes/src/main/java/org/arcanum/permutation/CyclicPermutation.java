package org.arcanum.permutation;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class CyclicPermutation implements Permutation {

    private int r, value;


    public CyclicPermutation(int r, int value) {
        this.r = r;
        this.value = value % r;
    }

    public CyclicPermutation(int r) {
        this.r = r;
        this.value = 0;
    }

    public int getSize() {
        return r;
    }

    public boolean isCyclic() {
        return true;
    }

    public int permute(int index) {
        return (index + value) % r;
    }

    public Permutation reverse() {
        return new CyclicPermutation(r, r - value);
    }

    public static void main(String[] args) {
        int r = 5;
        for (int j = 0; j < r; j++) {
            Permutation p = new CyclicPermutation(r, j);
            System.out.printf("perm(%d,%d)\n", r, j);
            for (int i = 0; i < p.getSize(); i++) {
                System.out.printf("%d -> %d\n", i, p.permute(i));
            }
        }
    }


}
