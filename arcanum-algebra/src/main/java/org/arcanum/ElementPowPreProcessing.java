package org.arcanum;

import java.math.BigInteger;

/**
 * If it knows in advance that a particular value will be raised several times
 * then time can be saved in the long run by using preprocessing.
 *
 * The, this interface is used to compute the power function when preprocessing information has
 * been compute before on the base element which is so fixed for each instance of this interface.
 *
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 * @see Element#getElementPowPreProcessing()
 * @see Field#getElementPowPreProcessingFromBytes(byte[])
 * @see Field#getElementPowPreProcessingFromBytes(byte[], int)
 * @see Field#getElementPowPreProcessingFromBytes(byte[], int)
 * @see Field
 * @see Element
 */
public interface ElementPowPreProcessing extends ElementPow, PreProcessing {

    /**
     * Returns the field the pre-processed element belongs to.
     *
     * @return Returns the field the pre-processed element belongs to.
     * @since 1.0.0
     */
    Field getField();

    /**
     * Compute the power to n using the pre-processed information.
     *
     * @param n the exponent of the power.
     * @return a new element whose value is the computed power.
     * @since 1.0.0
     */
    Element pow(BigInteger n);

    /**
     * Compute the power to n, where n is an element of a ring Z_N for some N,
     * using the pre-processed information, 
     *
     * @param n the exponent of the power.
     * @return a new element whose value is the computed power.
     * @since 1.0.0
     */
    Element powZn(Element n);

}
