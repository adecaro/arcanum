package org.arcanum;

/**
 * Common interface for all pre-processing interfaces.
 *
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 * @see ElementPowPreProcessing
 * @see PairingPreProcessing
 */
public interface PreProcessing {

    /**
     * Converts the object to bytes.
     *
     * @return the bytes written.
     * @since 1.0.0
     */
    byte[] toBytes();

}
