package org.arcanum.common.cipher.engine;

import org.arcanum.Element;
import org.arcanum.common.cipher.params.ElementCipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public interface ElementCipher<E extends Element, O extends Element, P extends ElementCipherParameters> {


    public ElementCipher<E, O, P> init(P param);

    public ElementCipher<E, O, P> init(E key);

    public ElementCipher<E, O, P> init(P param, E key);


    public O processBytes(byte[] buffer);

    public O processElements(E... input);

    public byte[] processElementsToBytes(E... input);

}
