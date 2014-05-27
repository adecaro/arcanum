package org.arcanum;

/**
 * This interface lets the user to generate all the necessary parameters
 * to initialize a pairing.
 *
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 * @see Parameters
 */
public interface ParametersGenerator<P extends Parameters> {

    /**
     * Generates the parameters.
     *
     * @return a map with all the necessary parameters.
     * @since 1.0.0
     */
    P generate();

}
