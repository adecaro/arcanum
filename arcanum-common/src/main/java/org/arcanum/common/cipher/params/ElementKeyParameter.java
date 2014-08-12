package org.arcanum.common.cipher.params;


import org.arcanum.ElementCipherParameters;

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
