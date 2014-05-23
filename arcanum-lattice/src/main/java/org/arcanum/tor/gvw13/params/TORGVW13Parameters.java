package org.arcanum.tor.gvw13.params;

import org.arcanum.util.cipher.params.ElementCipherParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class TORGVW13Parameters implements ElementCipherParameters {

    protected SecureRandom random;
    protected int n;
    protected int depth;


    public TORGVW13Parameters(SecureRandom random, int n, int depth) {
        this.random = random;
        this.n = n;
        this.depth = depth;
    }

    public SecureRandom getRandom() {
        return random;
    }

    public int getN() {
        return n;
    }

    public int getDepth() {
        return depth;
    }
}