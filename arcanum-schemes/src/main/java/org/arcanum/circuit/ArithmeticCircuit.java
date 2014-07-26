package org.arcanum.circuit;

import org.arcanum.Element;
import org.arcanum.field.util.ElementUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public class ArithmeticCircuit implements Circuit<ArithmeticGate> {

    private int n, q;
    private int depth;
    private ArithmeticGate[] gates;


    public ArithmeticCircuit(int n, int q, int depth, ArithmeticCircuitGate[] gates) {
        this.n = n;
        this.q = q;
        this.depth = depth;

        this.gates = gates;
        for (ArithmeticCircuitGate gate : gates)
            gate.setCircuit(this);
    }


    public int getNumInputs() {
        return n;
    }

    public int getNumGates() {
        return q;
    }

    public int getDepth() {
        return depth;
    }

    public Iterator<ArithmeticGate> iterator() {
        return Arrays.asList(gates).iterator();
    }

    public ArithmeticGate getGateAt(int index) {
        return gates[index];
    }

    public ArithmeticGate getOutputGate() {
        return gates[n + q - 1];
    }

    public Element evaluate(Element... inputs) {
        for (ArithmeticGate gate : gates) {
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
        private int[] inputs;

        private Element value;
        private Element[] alphas;
        private Map<Integer, Element> values;

        public ArithmeticCircuitGate(Type type, int index, int depth) {
            this.type = type;
            this.index = index;
            this.depth = depth;
            this.values = new HashMap<Integer, Element>();
        }

        public ArithmeticCircuitGate(Type type, int index, int depth, int[] inputs, Element... alphas) {
            this.type = type;
            this.index = index;
            this.depth = depth;
            this.inputs = Arrays.copyOf(inputs, inputs.length);
            this.alphas = ElementUtils.cloneImmutable(alphas);
            this.values = new HashMap<Integer, Element>();
        }

        public int getNumInputs() {
            return inputs.length;
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

        @Override
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
