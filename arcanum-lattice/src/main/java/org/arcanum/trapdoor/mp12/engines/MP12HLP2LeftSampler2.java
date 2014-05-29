package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.ElementCipher;
import org.arcanum.ElementCipherParameters;
import org.arcanum.trapdoor.mp12.params.MP12HLP2SampleLeftParameters;
import org.arcanum.util.cipher.params.ElementKeyPairParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2LeftSampler2 extends MP12HLP2Sampler {


    public ElementCipher init(ElementCipherParameters param) {
        MP12HLP2SampleLeftParameters params = (MP12HLP2SampleLeftParameters) param;

        ElementKeyPairParameters keyPair = params.getKeyPair();

//        MP12HLP2ExKeyPairGenerator gen = new MP12HLP2ExKeyPairGenerator();
//        gen.init(new MP12HLP2ExKeyPairGenerationParameters(keyPair, params.getExtraM()));
//        ElementKeyPairParameters extendedKeyPair = gen.generateKeyPair();

//        return super.init(new MP12HLP2SampleParameters(extendedKeyPair));

        return this;
    }

}
