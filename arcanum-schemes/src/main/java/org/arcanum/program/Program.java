package org.arcanum.program;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public interface Program<I, O> {

    O evaluate(I... inputs);

}
