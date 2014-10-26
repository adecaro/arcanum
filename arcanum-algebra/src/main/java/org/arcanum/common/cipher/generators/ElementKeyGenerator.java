package org.arcanum.common.cipher.generators;

import org.arcanum.common.cipher.params.ElementCipherParameters;
import org.arcanum.common.cipher.params.ElementKeyGenerationParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public interface ElementKeyGenerator<P extends ElementKeyGenerationParameters, K extends ElementCipherParameters> {

    public ElementKeyGenerator<P, K> init(P keyGenerationParameters);

    public K generateKey();

}
