package org.arcanum;

/**
 * If it is known in advance that a particular element will be paired several times
 * then time can be saved in the long run by using preprocessing.
 *
 * The, this interface is used to compute the pairing function when preprocessing information has
 * been compute before on the first input which is so fixed for each instance of this interface.
 *
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 * @see Element
 * @see Pairing
 * @see Pairing#getPairingPreProcessingFromBytes(byte[])
 * @see Pairing#getPairingPreProcessingFromBytes(byte[], int)
 * @see Pairing#getPairingPreProcessingFromElement(Element)
 * @see Pairing#getPairingPreProcessingLengthInBytes()
 */
public interface PairingPreProcessing extends PreProcessing {

    /**
     * Compute the pairing where the second argument is in2. The pre-processed information
     * are used for a fast computation
     *
     * @param in2 the second pairing function argument.
     * @return an element from GT whose value is assigned by this map applied to in1 and in2.
     * @since 1.0.0
     */
    Element pairing(Element in2);

}
