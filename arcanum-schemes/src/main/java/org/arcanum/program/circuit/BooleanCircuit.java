package org.arcanum.program.circuit;

import java.util.*;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public class BooleanCircuit implements Circuit<BooleanGate> {

    private int numInputs, numGates, numWires, numOutputs;
    private List<BooleanGate> gatesList;
    private Map<Integer, BooleanGate> gateMap;


    public BooleanCircuit(int numInputs, int numGates, int numWires, int numOutputs) {
        this.numInputs = numInputs;
        this.numGates = numGates;
        this.numWires = numWires;
        this.numOutputs = numOutputs;

        this.gatesList = new ArrayList<BooleanGate>();
        this.gateMap = new HashMap<Integer, BooleanGate>();
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

    public Iterator<BooleanGate> iterator() {
        return gatesList.iterator();
    }

    public BooleanGate getGateAt(int index) {
        return gateMap.get(index);
    }

    public BooleanGate getOutputGate() {
        return gateMap.get(gatesList.size() - 1);
    }

    public Gate getOutputGateAt(int index) {
        return gateMap.get(gatesList.size() - numOutputs + index);
    }

    public BooleanCircuit computeDepths() {
        for (BooleanGate gate : this) {
            BooleanCircuitGate bcGate = (BooleanCircuitGate) gate;

            switch (gate.getType()) {
                case INPUT:
                    bcGate.depth = 0;
                    break;

                default:
                    int max = 0;
                    for (int i = 0; i <gate.getNumInputs(); i++) {
                        Gate temp = getGateAt(gate.getInputIndexAt(i));
                        max = Math.max(temp.getDepth(), max);
                    }
                    bcGate.depth = max + 1;
                    break;
            }
        }
        return this;
    }

    public BooleanCircuit addGate(BooleanCircuitGate gate) {
        gatesList.add(gate);
        gate.setCircuit(this);

        for (int i = 0, n = gate.getNumOutputs(); i < n; i++) {
            gateMap.put(gate.getOutputIndexAt(i), gate);
        }

        return this;
    }


    public static class BooleanCircuitGate implements BooleanGate {

        private BooleanCircuit circuit;

        private Type type;
        private int index;
        private int depth;
        private int[] inputs;
        private int[] outputs;

        private boolean value;


        public BooleanCircuitGate(Type type, int index) {
            this.type = type;
            this.index = index;
            this.depth = 0;
        }

        public BooleanCircuitGate(Type type, int[] ins, int[] outs) {
            this.type = type;
            this.index = outs[0];
            this.depth = 0;

            if (outs.length > 1)
                throw new IllegalArgumentException("Too many outputs");

            this.inputs = Arrays.copyOf(ins, ins.length);
            this.outputs = Arrays.copyOf(outs, outs.length);
        }


        public int getNumInputs() {
            return (inputs == null) ? 0 : inputs.length;
        }

        public int getNumOutputs() {
            return (outputs == null) ? 1 : outputs.length;
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

                case NOT:
                case INV:
                    this.value = !getInputAt(0).get();
                    break;

                case NAND:
                    this.value = !(getInputAt(0).get() && getInputAt(1).get());
                    break;

                case MOD2:
                    this.value = (getInputAt(0).get() && !getInputAt(1).get()) ||
                            (!getInputAt(0).get() && getInputAt(1).get());

                    break;

                default:
                    throw new IllegalStateException("Invalid type");
            }

            return this;
        }

        public String toString() {
            return String.format("BooleanGate{type=%s, index=%d, depth=%d, inputs=%s, value=%s}", type, index, depth, Arrays.toString(inputs), value);
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
