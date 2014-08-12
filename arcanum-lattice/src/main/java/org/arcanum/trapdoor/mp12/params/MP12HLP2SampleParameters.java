package org.arcanum.trapdoor.mp12.params;

import org.arcanum.common.cipher.params.ElementCipherParameters;
import org.arcanum.common.cipher.params.ElementKeyPairParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2SampleParameters extends MP12KeyParameters {

    protected ElementKeyPairParameters keyPair;

    public MP12HLP2SampleParameters(ElementKeyPairParameters keyPair) {
        super(true, null);

        this.keyPair = keyPair;
    }

    public MP12HLP2SampleParameters(ElementCipherParameters pk, ElementCipherParameters sk) {
        super(true, null);

        this.keyPair = new ElementKeyPairParameters(pk, sk);
    }


    public ElementKeyPairParameters getKeyPair() {
        return keyPair;
    }
}
