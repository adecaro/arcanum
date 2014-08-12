package org.arcanum;

import java.math.BigInteger;

/**
 * Common interface for the exponentiation.
 *
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 * @see Element
 * @see ElementPowPreProcessing
 */
public interface ElementPow {

    /**
     * Compute the power to n.
     *
     * @param n the exponent of the power.
     * @return the computed power.
     * @since 1.0.0
     */
    Element pow(BigInteger n);

    /**
     * Compute the power to n, where n is an element of a ring Z_N for some N.
     *
     * @param n the exponent of the power.
     * @return the computed power.
     * @since 1.0.0
     */
    Element powZn(Element n);

}
