package org.arcanum.util.cipher.params;

import java.security.SecureRandom;

public class ElementKeyGenerationParameters {
    private SecureRandom    random;


    public ElementKeyGenerationParameters(SecureRandom random){
        this.random = random;
    }

    public SecureRandom getRandom()
    {
        return random;
    }

}
