package org.arcanum.fe.abe.bns14.params;

import org.arcanum.Sampler;
import org.bouncycastle.crypto.CipherParameters;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BNS14Parameters implements CipherParameters {

    private SecureRandom random;
    private int ell;
    private int n, k;
    private Sampler<BigInteger> chi;
    private Sampler<BigInteger> uniformOneMinusOne;

    public BNS14Parameters(SecureRandom random, int ell, int n, int k,
                           Sampler<BigInteger> chi, Sampler<BigInteger> uniformOneMinusOne) {
        this.random = random;
        this.ell = ell;

        this.n = n;
        this.k = k;
        this.chi = chi;
        this.uniformOneMinusOne = uniformOneMinusOne;
    }

    public SecureRandom getRandom() {
        return random;
    }

    public int getEll() {
        return ell;
    }

    public int getN() {
        return n;
    }

    public int getK() {
        return k;
    }

    public Sampler<BigInteger> getChi() {
        return chi;
    }

    public Sampler<BigInteger> getUniformOneMinusOne() {
        return uniformOneMinusOne;
    }
}