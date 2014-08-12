package org.arcanum.program.circuit;

import org.arcanum.Element;
import org.arcanum.program.AbstractProgramEvaluator;
import org.arcanum.program.Assignment;
import org.arcanum.program.assignment.ElementAssignment;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ArithmeticCircuitEvaluator extends AbstractProgramEvaluator<ArithmeticCircuit, Element, Element> {

    public Element evaluate(ArithmeticCircuit circuit, Element... inputs) {
        return evaluate(circuit, new ElementAssignment(inputs));
    }

    public Element evaluate(ArithmeticCircuit circuit, Assignment<Element> assignment) {
        for (ArithmeticGate gate : circuit) {
            int index = gate.getIndex();

            switch (gate.getType()) {
                case INPUT:
                    gate.set(assignment.getAt(index));
                    break;

                default:
                    gate.evaluate();
                    break;
            }
        }
        return circuit.getOutputGate().get();
    }

}
