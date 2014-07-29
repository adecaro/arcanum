package org.arcanum.program.pbp.permutation;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public interface Permutation {

    int getSize();

    boolean isCyclic();

    int permute(int index);

    int permuteInverse(int index);

    Permutation getInverse();

}
