package org.arcanum.permutation;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public interface Permutation {

    int getSize();

    boolean isCyclic();

    int permute(int index);

    Permutation getInverse();

    Permutation compose(Permutation permutation);
}
