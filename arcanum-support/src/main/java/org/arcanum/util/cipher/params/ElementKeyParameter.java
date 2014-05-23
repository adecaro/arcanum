package org.arcanum.util.cipher.params;


public class ElementKeyParameter
    implements ElementCipherParameters
{
    boolean privateKey;

    public ElementKeyParameter(
            boolean privateKey)
    {
        this.privateKey = privateKey;
    }

    public boolean isPrivate()
    {
        return privateKey;
    }
}
