package org.arcanum.pairing.accumulator;

import org.arcanum.Element;
import org.arcanum.Pairing;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public class MultiThreadedMulPairingAccumulator extends AbstractPairingAccumulator {


    public MultiThreadedMulPairingAccumulator(Pairing pairing) {
        super(pairing);
    }

    public MultiThreadedMulPairingAccumulator(Pairing pairing, Element value) {
        super(pairing, value);
    }


    protected void reduce(Element value) {
        this.result.mul(value);
    }
}
