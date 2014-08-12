package org.arcanum.common.cipher.generators;

import org.arcanum.ElementCipherParameters;
import org.arcanum.common.cipher.params.ElementKeyGenerationParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public interface ElementKeyGenerator {

    public ElementKeyGenerator init(ElementKeyGenerationParameters keyGenerationParameters);

    public ElementCipherParameters generateKey();

}
