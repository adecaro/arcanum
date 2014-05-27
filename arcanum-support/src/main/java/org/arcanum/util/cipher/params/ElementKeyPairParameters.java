package org.arcanum.util.cipher.params;

import org.arcanum.ElementCipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ElementKeyPairParameters {

    private ElementCipherParameters publicParam;
    private ElementCipherParameters    privateParam;

    /**
     * basic constructor.
     *
     * @param publicParam a public key parameters object.
     * @param privateParam the corresponding private key parameters.
     */
    public ElementKeyPairParameters(
            ElementCipherParameters    publicParam,
            ElementCipherParameters    privateParam)
    {
        this.publicParam = publicParam;
        this.privateParam = privateParam;
    }

    /**
     * return the public key parameters.
     *
     * @return the public key parameters.
     */
    public ElementCipherParameters getPublic()
    {
        return publicParam;
    }

    /**
     * return the private key parameters.
     *
     * @return the private key parameters.
     */
    public ElementCipherParameters getPrivate()
    {
        return privateParam;
    }

}
