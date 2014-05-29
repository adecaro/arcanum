package org.arcanum.trapdoor.mp12.params;

import org.arcanum.util.cipher.params.ElementKeyPairParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2ExKeyPairGenerationParameters extends MP12PLP2KeyPairGenerationParameters {

    public MP12HLP2ExKeyPairGenerationParameters(ElementKeyPairParameters keyPair, int extraM) {
        super(null, 0, 0, extraM);
    }

}
