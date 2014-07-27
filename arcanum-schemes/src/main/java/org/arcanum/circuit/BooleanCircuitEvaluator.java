package org.arcanum.circuit;

import org.arcanum.program.AbstractProgramEvaluator;
import org.arcanum.program.Assignment;
import org.arcanum.program.assignment.BooleanAssignment;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BooleanCircuitEvaluator extends AbstractProgramEvaluator<BooleanCircuit, Boolean, Boolean> {

    public Boolean evaluate(BooleanCircuit circuit, Boolean... inputs) {
        return evaluate(circuit, new BooleanAssignment(inputs));
    }

    public Boolean evaluate(BooleanCircuit circuit, Assignment<Boolean> assignment) {
        for (BooleanGate gate : circuit) {
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
