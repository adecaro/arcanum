package org.arcanum.fhe.gsw14.program;

import org.arcanum.Element;
import org.arcanum.program.AbstractProgramEvaluator;
import org.arcanum.program.circuit.BooleanCircuit;
import org.arcanum.program.circuit.BooleanGate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AP14GSWCircuitEvaluator extends AbstractProgramEvaluator<BooleanCircuit, Element, Element> {

    public Element evaluate(BooleanCircuit circuit, final Element... inputs) {
        Map<Integer, Element> map = new HashMap<Integer, Element>();

        for (BooleanGate gate : circuit) {
            int index = gate.getIndex();

            switch (gate.getType()) {
                case INPUT:
                    map.put(index, inputs[index]);
                    break;

                case AND:
                    // multiplication
                    break;

                case OR:
                    // addition

                case NOT:
                    // 1-<value>
                    break;

                default:
                    throw new IllegalArgumentException("Gate not recognized!!! " + gate);
            }
        }

        return map.get(circuit.getOutputGate().getIndex());
    }

}
