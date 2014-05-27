package org.arcanum.trapdoor.mp12.params;


import org.arcanum.ElementCipherParameters;
import org.arcanum.util.cipher.params.ElementKeyPairParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2SampleLeftParameters extends MP12HLP2SampleParameters {

    public MP12HLP2SampleLeftParameters(ElementKeyPairParameters keyPair) {
        super(keyPair);
    }

    public MP12HLP2SampleLeftParameters(ElementCipherParameters pk, ElementCipherParameters sk) {
        super(pk, sk);
    }
}
