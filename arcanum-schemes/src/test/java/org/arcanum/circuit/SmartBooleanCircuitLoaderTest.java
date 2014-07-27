package org.arcanum.circuit;

import org.arcanum.circuit.smart.SmartBooleanCircuitLoader;
import org.junit.Test;

public class SmartBooleanCircuitLoaderTest {

    @Test
    public void testLoad1() throws Exception {
        SmartBooleanCircuitLoader loader = new SmartBooleanCircuitLoader();
        BooleanCircuit circuit = loader.load("org/arcanum/circuits/smart/comparator_32bit_signed_lteq.txt");

        Boolean[] x = new Boolean[64];
        for (int i = 0; i < 32; i++) {
            x[i] = false;
            x[32 + i] = true;
        }

        BooleanCircuitEvaluator evaluator = new BooleanCircuitEvaluator();
        boolean out = evaluator.evaluate(circuit, x);
        System.out.println("out = " + out);
    }

    @Test
    public void testLoad2() throws Exception {
        // TODO: finish this allowing multiple output
//        SmartBooleanCircuitLoader loader = new SmartBooleanCircuitLoader();
//        BooleanCircuit circuit = loader.load("org/arcanum/circuits/AES-non-expanded.txt");
//
//        Boolean[] x = new Boolean[256];
//        for (int i = 0; i < 32; i++) {
//            x[i] = (Math.random() >= 0.5d);
//        }
//
//        BooleanCircuitEvaluator evaluator = new BooleanCircuitEvaluator();
//        boolean out = evaluator.evaluate(circuit, x);
//        System.out.println("out = " + out);
    }

}