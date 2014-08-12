package org.arcanum.program.circuit;

import org.arcanum.Element;
import org.arcanum.field.util.ElementUtils;

import java.util.*;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public class ArithmeticCircuit implements Circuit<ArithmeticGate> {

    private int numInputs, numGates, numWires, numOutputs;
    private List<ArithmeticGate> gatesList;
    private Map<Integer, ArithmeticGate> gateMap;


    public ArithmeticCircuit(int numInputs, int numGates, int numWires, int numOutputs) {
        this.numInputs = numInputs;
        this.numGates = numGates;
        this.numWires = numWires;
        this.numOutputs = numOutputs;

        this.gatesList = new ArrayList<ArithmeticGate>();
        this.gateMap = new HashMap<Integer, ArithmeticGate>();
    }


    public int getNumInputs() {
        return numInputs;
    }

    public int getNumGates() {
        return numGates;
    }

    public int getNumWires() {
        return numWires;
    }

    public int getDepth() {
        return getOutputGate().getDepth();
    }

    public int getNumOutputs() {
        return numOutputs;
    }

    public Iterator<ArithmeticGate> iterator() {
        return gatesList.iterator();
    }

    public ArithmeticGate getGateAt(int index) {
        return gateMap.get(index);
    }

    public ArithmeticGate getOutputGate() {
        return gateMap.get(gatesList.size() - 1);
    }

    public Gate getOutputGateAt(int index) {
        return gateMap.get(gatesList.size() - numOutputs + index);
    }

    public ArithmeticCircuit computeDepths() {
        for (ArithmeticGate gate : this) {
            ArithmeticCircuitGate arithmeticCircuitGate = (ArithmeticCircuitGate) gate;

            switch (gate.getType()) {
                case INPUT:
                    arithmeticCircuitGate.depth = 0;
                    break;

                default:
                    int max = 0;
                    for (int i = 0; i < gate.getNumInputs(); i++) {
                        Gate temp = getGateAt(gate.getInputIndexAt(i));
                        max = Math.max(temp.getDepth(), max);
                    }
                    arithmeticCircuitGate.depth = max + 1;
                    break;
            }
        }
        return this;
    }

    public ArithmeticCircuit addGate(ArithmeticCircuitGate gate) {
        gatesList.add(gate);
        gate.setCircuit(this);

        for (int i = 0, n = gate.getNumOutputs(); i < n; i++) {
            gateMap.put(gate.getOutputIndexAt(i), gate);
        }

        return this;
    }


    public Element evaluate(Element... inputs) {
        for (ArithmeticGate gate : gatesList) {
            switch (gate.getType()) {
                case INPUT:
                    gate.set(inputs[gate.getIndex()]);
                    break;
                case OR:
                case AND:
                    gate.evaluate();
                    break;
            }
        }

        return getOutputGate().get();
    }

    public static class ArithmeticCircuitGate implements ArithmeticGate {

        private ArithmeticCircuit circuit;

        private Type type;
        private int index;
        private int depth;
        private int[] inputs, outputs;

        private Element value;
        private Element[] alphas;
        private Map<Integer, Element> values;


        public ArithmeticCircuitGate(Type type, int index) {
            this.type = type;
            this.index = index;
            this.depth = 0;

            this.values = new HashMap<Integer, Element>();
        }

        public ArithmeticCircuitGate(Type type, int[] ins, int[] outs, Element... alphas) {
            this.type = type;
            this.index = outs[0];
            this.depth = 0;

            this.inputs = Arrays.copyOf(ins, ins.length);
            this.outputs = Arrays.copyOf(outs, outs.length);

            this.alphas = ElementUtils.cloneImmutable(alphas);
            this.values = new HashMap<Integer, Element>();
        }


        public int getNumInputs() {
            return (inputs == null) ? 0 : inputs.length;
        }

        public int getNumOutputs() {
            return (outputs == null) ? 1 : outputs.length;
        }

        public Element get() {
            return value;
        }

        public Element getAlphaAt(int index) {
            return alphas[index];
        }

        public Type getType() {
            return type;
        }

        public int getIndex() {
            return index;
        }

        public int getDepth() {
            return depth;
        }

        public int getInputIndexAt(int index) {
            return inputs[index];
        }

        public int getOutputIndexAt(int index) {
            return (outputs == null) ? this.index : outputs[index];
        }

        public ArithmeticGate getInputAt(int index) {
            return circuit.getGateAt(getInputIndexAt(index));
        }

        public ArithmeticGate set(Element value) {
            this.value = value;
            return this;
        }

        public ArithmeticGate evaluate() {
            switch (type) {
                case AND:
                    value = getInputAt(0).get().duplicate().mul(getAlphaAt(0));
                    for (int i = 1; i < inputs.length; i++) {
                        value.mul(getInputAt(i).get());
                    }
                    break;

                case OR:
                    value = getInputAt(0).get().duplicate().mul(getAlphaAt(0));
                    for (int i = 1; i < inputs.length; i++) {
                        value.add(getInputAt(i).get().duplicate().mul(getAlphaAt(i)));
                    }
                    break;

                default:
                    throw new IllegalStateException("Invalid type");
            }

            return this;
        }

        public String toString() {
            return "ArithmeticGate{" +
                    "type=" + type +
                    ", index=" + index +
                    ", depth=" + depth +
                    ", inputs=" + Arrays.toString(inputs) +
                    ", value=" + value +
                    '}';
        }

        public Gate<Element> putAt(int index, Element value) {
            values.put(index, value);
            return this;
        }

        public Element getAt(int index) {
            return values.get(index);
        }


        protected void setCircuit(ArithmeticCircuit circuit) {
            this.circuit = circuit;
        }

    }

}
