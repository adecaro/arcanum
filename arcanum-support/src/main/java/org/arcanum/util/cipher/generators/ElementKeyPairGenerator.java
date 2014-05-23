package org.arcanum.util.cipher.generators;

import org.arcanum.util.cipher.params.ElementKeyGenerationParameters;
import org.arcanum.util.cipher.params.ElementKeyPairParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public interface ElementKeyPairGenerator {

    /**
     * intialise the key pair generator.
     *
     * @param param the parameters the key pair is to be initialised with.
     */
    public void init(ElementKeyGenerationParameters param);

    public ElementKeyPairParameters generateKeyPair();

}
