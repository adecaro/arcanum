package org.arcanum.pairing.immutable;

import org.arcanum.Element;
import org.arcanum.PairingPreProcessing;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public class ImmutablePairingPreProcessing implements PairingPreProcessing {

    private PairingPreProcessing pairingPreProcessing;

    public ImmutablePairingPreProcessing(PairingPreProcessing pairingPreProcessing) {
        this.pairingPreProcessing = pairingPreProcessing;
    }

    public Element pairing(Element in2) {
        return pairingPreProcessing.pairing(in2).getImmutable();
    }

    public byte[] toBytes() {
        return pairingPreProcessing.toBytes();
    }
}
