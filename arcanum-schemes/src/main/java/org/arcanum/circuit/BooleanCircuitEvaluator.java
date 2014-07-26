package org.arcanum.circuit;

import org.arcanum.program.ProgramEvaluator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BooleanCircuitEvaluator implements ProgramEvaluator<BooleanCircuit, Boolean, Boolean> {

    public Boolean evaluate(BooleanCircuit circuit, Boolean... inputs) {

        Map<Integer, Integer> depths = new HashMap<Integer, Integer>();

        for (BooleanGate gate : circuit) {
            int index = gate.getIndex();

            switch (gate.getType()) {
                case INPUT:
                    gate.set(inputs[index]);
                   depths.put(index, 0);

                    break;

                default:
                    gate.evaluate();

                    int max = 0;
                    for (int i = 0; i <gate.getNumInputs(); i++) {
                        max = Math.max(depths.get(gate.getInputIndexAt(i)), max);
                    }

                    System.out.println("gate.getDepth() = " + gate.getDepth());
                    System.out.println("max = " + max);

                    depths.put(index, max + 1);
                    break;
            }
        }

        System.out.println(depths.get(circuit.getOutputGate().getIndex()));

        return circuit.getOutputGate().get();
    }
}
