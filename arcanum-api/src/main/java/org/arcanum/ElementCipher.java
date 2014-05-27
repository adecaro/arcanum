package org.arcanum;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public interface ElementCipher<E extends Element, P extends ElementCipherParameters> {


    public ElementCipher init(P param);

    public ElementCipher init(E key);


    public E processBytes(byte[] buffer);

    public E processElements(E... input);

    public byte[] processElementsToBytes(E... input);

}
