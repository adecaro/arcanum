package org.arcanum.common.cipher.engine;

import org.arcanum.Element;
import org.arcanum.common.cipher.params.ElementCipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AbstractElementCipher<E extends Element, P extends ElementCipherParameters>
        implements ElementCipher<E, P> {

    public ElementCipher init(P param) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public ElementCipher init(E key) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public ElementCipher init(P param, E key) {
        throw new IllegalStateException("Not implemented yet!!!");
    }


    public E processBytes(byte[] buffer) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public E processElements(E... input) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public E processElementsTo(E to, E... input) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public byte[] processElementsToBytes(E... input) {
        throw new IllegalStateException("Not implemented yet!!!");
    }
}
