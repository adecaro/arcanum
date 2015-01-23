package org.arcanum.common.cipher.engine;

import org.arcanum.Element;
import org.arcanum.common.cipher.params.ElementCipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AbstractElementCipher<E extends Element, O extends Element, P extends ElementCipherParameters>
        implements ElementCipher<E, O, P> {

    public ElementCipher<E, O, P> init(P param) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public ElementCipher<E, O, P> init(E key) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public ElementCipher<E, O, P> init(P param, E key) {
        throw new IllegalStateException("Not implemented yet!!!");
    }


    public O processBytes(byte[] buffer) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public O processElements(E... input) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public O processElementsTo(E to, E... input) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public byte[] processElementsToBytes(E... input) {
        throw new IllegalStateException("Not implemented yet!!!");
    }
}
