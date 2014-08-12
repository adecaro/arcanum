package org.arcanum.pairing.map;

import org.arcanum.Point;
import org.arcanum.pairing.Pairing;
import org.arcanum.pairing.PairingPreProcessing;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public abstract class AbstractMillerPairingPreProcessing implements PairingPreProcessing {

    protected AbstractMillerPairingMap.MillerPreProcessingInfo processingInfo;


    protected AbstractMillerPairingPreProcessing() {
    }

    protected AbstractMillerPairingPreProcessing(Point in1, int processingInfoSize) {
        this.processingInfo = new AbstractMillerPairingMap.MillerPreProcessingInfo(processingInfoSize);
    }

    protected AbstractMillerPairingPreProcessing(Pairing pairing, byte[] source, int offset) {
        this.processingInfo = new AbstractMillerPairingMap.MillerPreProcessingInfo(pairing, source, offset);
    }

    public byte[] toBytes() {
        if (processingInfo != null)
            return processingInfo.toBytes();
        else
            return new byte[0];
    }
}
