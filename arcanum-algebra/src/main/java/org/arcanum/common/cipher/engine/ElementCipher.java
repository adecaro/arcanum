package org.arcanum.common.cipher.engine;

import org.arcanum.Element;
import org.arcanum.common.cipher.params.ElementCipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public interface ElementCipher<E extends Element, P extends ElementCipherParameters> {


    public ElementCipher<E, P> init(P param);

    public ElementCipher<E, P> init(E key);

    public ElementCipher<E, P> init(P param, E key);


    public E processBytes(byte[] buffer);

    public E processElements(E... input);

    public byte[] processElementsToBytes(E... input);

}
