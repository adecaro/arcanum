package org.arcanum.trapdoor.mp12.params;

import org.arcanum.util.cipher.params.ElementCipherParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12Parameters implements ElementCipherParameters {

    protected SecureRandom random;
    protected int n;

    public MP12Parameters(SecureRandom random, int n) {
        this.random = random;
        this.n = n;
    }

    public SecureRandom getRandom() {
        return random;
    }

    public int getN() {
        return n;
    }

}