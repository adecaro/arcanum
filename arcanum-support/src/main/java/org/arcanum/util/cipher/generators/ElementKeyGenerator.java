package org.arcanum.util.cipher.generators;

import org.arcanum.util.cipher.params.ElementCipherParameters;
import org.arcanum.util.cipher.params.ElementKeyGenerationParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public interface ElementKeyGenerator {

    public ElementKeyGenerator init(ElementKeyGenerationParameters keyGenerationParameters);

    public ElementCipherParameters generateKey();

}
