package org.arcanum.pairing.accumulator;


import org.arcanum.AbstractArcanumTest;
import org.arcanum.Element;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public class MulPairingAccumulatorTest extends AbstractArcanumTest {

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {false, "it/unisa/dia/gas/plaf/arcanum/pairing/a/a_181_603.properties"},
        };

        return Arrays.asList(data);
    }


    public MulPairingAccumulatorTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }


    @Test
    public void testMultiplier() {
        int n = 1000;
        Element in1s[] = new Element[n];
        Element in2s[] = new Element[n];

        for (int i = 0; i < n; i++) {
            in1s[i] = pairing.getG1().newRandomElement();
            in2s[i] = pairing.getG2().newRandomElement();
        }

        // Test default
        PairingAccumulator multiplier = new SequentialMulPairingAccumulator(pairing);
        for (int i = 0; i < n; i++) {
            multiplier.addPairing(in1s[i], in2s[i]);
        }
        Element result1 = multiplier.awaitResult();

        // Test multi thread
        multiplier = new MultiThreadedMulPairingAccumulator(pairing);
        for (int i = 0; i < n; i++) {
            multiplier.addPairing(in1s[i], in2s[i]);
        }
        Element result2 = multiplier.awaitResult();

        assertEquals(true, result1.isEqual(result2));
    }

}
