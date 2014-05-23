package org.arcanum.kem.cipher.params;

import org.bouncycastle.crypto.CipherParameters;

import java.security.AlgorithmParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class KEMCipherParameters implements CipherParameters {

    private AlgorithmParameters algorithmParameters;


    public KEMCipherParameters(AlgorithmParameters algorithmParameters) {
        this.algorithmParameters = algorithmParameters;
    }

    public AlgorithmParameters getAlgorithmParameters() {
        return algorithmParameters;
    }
}
