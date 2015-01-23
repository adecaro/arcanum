package org.arcanum.fe.abe.bns14.params;

import org.arcanum.Sampler;
import org.arcanum.trapdoor.mp12.utils.MP12EngineFactory;
import org.bouncycastle.crypto.CipherParameters;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BNS14Parameters implements CipherParameters {

    private SecureRandom random;
    private int ell;
    private int n;
    private MP12EngineFactory factory;
    private Sampler<BigInteger> chi;
    private Sampler<BigInteger> uniformOneMinusOne;



    public BNS14Parameters(SecureRandom random, int ell, int n, MP12EngineFactory factory,
                           Sampler<BigInteger> chi, Sampler<BigInteger> uniformOneMinusOne) {
        this.random = random;
        this.ell = ell;

        this.n = n;
        this.factory = factory;
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

    public MP12EngineFactory getFactory() {
        return factory;
    }

    public Sampler<BigInteger> getChi() {
        return chi;
    }

    public Sampler<BigInteger> getUniformOneMinusOne() {
        return uniformOneMinusOne;
    }
}