package org.arcanum.program.circuit;

import org.arcanum.program.Program;

import java.util.Iterator;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public interface Circuit<C extends Gate> extends Iterable<C>, Program {

    int getNumInputs();

    int getNumGates();

    int getDepth();

    int getNumOutputs();


    Iterator<C> iterator();

    Gate getGateAt(int index);

    Gate getOutputGate();

    Gate getOutputGateAt(int index);

}
