package org.arcanum.circuit;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 2.0.0
 */
public class BooleanCircuit implements Circuit<BooleanGate> {

    private int n, q;
    private int depth;
    private BooleanGate[] gates;


    public BooleanCircuit(int n, int q, int depth, BooleanCircuitGate[] gates) {
        this.n = n;
        this.q = q;
        this.depth = depth;

        this.gates = gates;
        for (BooleanCircuitGate gate : gates)
            gate.setCircuit(this);
    }


    public int getN() {
        return n;
    }

    public int getQ() {
        return q;
    }

    public int getDepth() {
        return depth;
    }


    public Iterator<BooleanGate> iterator() {
        return Arrays.asList(gates).iterator();
    }

    public BooleanGate getGateAt(int index) {
        return gates[index];
    }

    public BooleanGate getOutputGate() {
        return gates[n + q - 1];
    }


    public static class BooleanCircuitGate implements BooleanGate {

        private BooleanCircuit circuit;

        private Type type;
        private int index;
        private int depth;
        private int[] inputs;

        private boolean value;

        public BooleanCircuitGate(Type type, int index, int depth) {
            this.type = type;
            this.index = index;
            this.depth = depth;
        }

        public BooleanCircuitGate(Type type, int index, int depth, int[] inputs) {
            this.type = type;
            this.index = index;
            this.depth = depth;
            this.inputs = Arrays.copyOf(inputs, inputs.length);
        }

        public int getInputNum() {
            return inputs.length;
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

        public BooleanGate getInputAt(int index) {
            return circuit.getGateAt(getInputIndexAt(index));
        }

        public BooleanGate set(Boolean value) {
            this.value = value;
            return this;
        }

        public Boolean get() {
            return value;
        }

        public BooleanGate evaluate() {
            switch (type) {
                case AND:
                    this.value = getInputAt(0).get() && getInputAt(1).get();
                    break;

                case OR:
                    this.value = getInputAt(0).get() || getInputAt(1).get();
                    break;

                case NAND:
                    this.value = !(getInputAt(0).get() && getInputAt(1).get());
                    break;

                default:
                    throw new IllegalStateException("Invalid type");
            }

            return this;
        }

        @Override
        public String toString() {
            return "BooleanGate{" +
                    "type=" + type +
                    ", index=" + index +
                    ", depth=" + depth +
                    ", inputs=" + Arrays.toString(inputs) +
                    ", value=" + value +
                    '}';
        }

        public Gate<Boolean> putAt(int index, Boolean value) {
            throw new IllegalStateException("Not implemented yet!!!");
        }

        public Boolean getAt(int index) {
            throw new IllegalStateException("Not implemented yet!!!");
        }

        protected void setCircuit(BooleanCircuit circuit) {
            this.circuit = circuit;
        }

    }

}
