package org.arcanum.circuit;

import org.arcanum.program.ProgramEvaluator;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BooleanCircuitEvaluator implements ProgramEvaluator<BooleanCircuit, Boolean, Boolean> {

    public Boolean evaluate(BooleanCircuit circuit, Boolean... inputs) {

        for (BooleanGate gate : circuit) {
            int index = gate.getIndex();

            switch (gate.getType()) {
                case INPUT:
                    gate.set(inputs[index]);
                    break;

                case OR:
                case AND:
                case NOT:
                    gate.evaluate();
                    break;
            }
        }

        return circuit.getOutputGate().get();
    }
}
