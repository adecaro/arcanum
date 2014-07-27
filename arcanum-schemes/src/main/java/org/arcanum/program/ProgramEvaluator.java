package org.arcanum.program;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public interface ProgramEvaluator<P extends Program, I, O> {

    O evaluate(P program, I... inputs);

    O evaluate(P program, Assignment<I> assignment);

}
