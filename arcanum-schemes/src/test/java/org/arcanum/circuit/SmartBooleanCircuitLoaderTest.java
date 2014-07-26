package org.arcanum.circuit;

import org.arcanum.circuit.smart.SmartBooleanCircuitLoader;
import org.junit.Test;

public class SmartBooleanCircuitLoaderTest {

    @Test
    public void testLoad() throws Exception {
        SmartBooleanCircuitLoader loader = new SmartBooleanCircuitLoader();
        BooleanCircuit circuit = loader.load("org/arcanum/circuits/comparator_32bit_signed_lteq.txt");

        Boolean[] x = new Boolean[64];
        for (int i = 0; i < 32; i++) {
            x[i] = false;
            x[32 + i] = true;
        }

        BooleanCircuitEvaluator evaluator = new BooleanCircuitEvaluator();
        boolean out = evaluator.evaluate(circuit, x);
        System.out.println("out = " + out);
    }
}