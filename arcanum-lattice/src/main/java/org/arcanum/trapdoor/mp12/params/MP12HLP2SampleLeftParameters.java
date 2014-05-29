package org.arcanum.trapdoor.mp12.params;

import org.arcanum.ElementCipherParameters;
import org.arcanum.util.cipher.params.ElementKeyPairParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2SampleLeftParameters extends MP12HLP2SampleParameters {

    private int matrixExtensionLength;

    public MP12HLP2SampleLeftParameters(ElementKeyPairParameters keyPair, int matrixExtensionLength) {
        super(keyPair);

        this.matrixExtensionLength = matrixExtensionLength;
    }

    public MP12HLP2SampleLeftParameters(ElementCipherParameters pk, ElementCipherParameters sk, int matrixExtensionLength) {
        super(pk, sk);

        this.matrixExtensionLength = matrixExtensionLength;
    }


    public int getMatrixExtensionLength() {
        return matrixExtensionLength;
    }
}
