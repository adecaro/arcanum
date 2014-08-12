package org.arcanum.pairing.accumulator;

import org.arcanum.Element;
import org.arcanum.PairingPreProcessing;
import org.arcanum.common.concurrent.accumultor.Accumulator;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public interface PairingAccumulator extends Accumulator<Element> {

    public PairingAccumulator addPairing(Element e1, Element e2);

    public PairingAccumulator addPairingInverse(Element e1, Element e2);

    public PairingAccumulator addPairing(PairingPreProcessing pairingPreProcessing, Element e2);

}
