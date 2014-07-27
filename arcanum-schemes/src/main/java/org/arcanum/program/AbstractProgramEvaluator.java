package org.arcanum.program;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class AbstractProgramEvaluator<P extends Program, I, O> implements ProgramEvaluator<P, I, O> {

    public O evaluate(P program, I... inputs) {
        return null;
    }

    public O evaluate(P program, Assignment<I> assignment) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

}
