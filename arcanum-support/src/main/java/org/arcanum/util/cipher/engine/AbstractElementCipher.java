package org.arcanum.util.cipher.engine;

import org.arcanum.Element;
import org.arcanum.ElementCipher;
import org.arcanum.ElementCipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AbstractElementCipher implements ElementCipher {

    public ElementCipher init(ElementCipherParameters param) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public ElementCipher init(Element key) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element processBytes(byte[] buffer) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Element processElements(Element... input) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public byte[] processElementsToBytes(Element... input) {
        throw new IllegalStateException("Not implemented yet!!!");
    }
}
