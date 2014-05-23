package org.arcanum.util.cipher.engine;

import org.arcanum.Element;
import org.arcanum.util.cipher.params.ElementCipherParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public interface ElementCipher {

    public ElementCipher init(ElementCipherParameters param);

    public ElementCipher init(Element key);


    public Element processBytes(byte[] buffer);

    public Element processElements(Element... input);

    public byte[] processElementsToBytes(Element... input);

}
