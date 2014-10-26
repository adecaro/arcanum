package org.arcanum.common.cipher.generators;

import org.arcanum.common.cipher.params.ElementKeyGenerationParameters;
import org.arcanum.common.cipher.params.ElementKeyPairParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public interface ElementKeyPairGenerator<P extends ElementKeyGenerationParameters> {

    /**
     * intialise the key pair generator.
     *
     * @param keyGenerationParameters the parameters the key pair is to be initialised with.
     */
    public ElementKeyPairGenerator<P> init(P keyGenerationParameters);

    public ElementKeyPairParameters generateKeyPair();

}
