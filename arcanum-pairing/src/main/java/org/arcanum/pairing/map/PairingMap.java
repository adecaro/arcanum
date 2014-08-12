package org.arcanum.pairing.map;

import org.arcanum.Element;
import org.arcanum.Point;
import org.arcanum.pairing.PairingPreProcessing;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public interface PairingMap {

    Element pairing(Point in1, Point in2);

    boolean isProductPairingSupported();

    Element pairing(Element[] in1, Element[] in2);


    void finalPow(Element element);

    boolean isAlmostCoddh(Element a, Element b, Element c, Element d);


    int getPairingPreProcessingLengthInBytes();

    PairingPreProcessing pairing(Point in1);

    PairingPreProcessing pairing(byte[] source, int offset);


}
