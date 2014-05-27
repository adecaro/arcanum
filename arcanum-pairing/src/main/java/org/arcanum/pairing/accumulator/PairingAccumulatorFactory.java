package org.arcanum.pairing.accumulator;

import org.arcanum.Element;
import org.arcanum.Pairing;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public class PairingAccumulatorFactory {

    private static final PairingAccumulatorFactory INSTANCE = new PairingAccumulatorFactory();

    public static PairingAccumulatorFactory getInstance() {
        return INSTANCE;
    }

    private boolean multiThreadingEnabled;


    private PairingAccumulatorFactory() {
        this.multiThreadingEnabled = false; // Runtime.getRuntime().availableProcessors() > 1;
    }


    public PairingAccumulator getPairingMultiplier(Pairing pairing) {
        return isMultiThreadingEnabled() ? new MultiThreadedMulPairingAccumulator(pairing)
                : new SequentialMulPairingAccumulator(pairing);
    }

    public PairingAccumulator getPairingMultiplier(Pairing pairing, Element element) {
        return isMultiThreadingEnabled() ? new MultiThreadedMulPairingAccumulator(pairing, element)
                : new SequentialMulPairingAccumulator(pairing, element);
    }

    public boolean isMultiThreadingEnabled() {
        return multiThreadingEnabled;
    }

    public void setMultiThreadingEnabled(boolean multiThreadingEnabled) {
        this.multiThreadingEnabled = multiThreadingEnabled;
    }

}
