package org.arcanum.trapdoor.mp12.params;

import org.apfloat.Apfloat;
import org.arcanum.ElementCipherParameters;
import org.arcanum.trapdoor.mp12.utils.MP12P2Utils;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12Parameters implements ElementCipherParameters {

    protected SecureRandom random;
    protected int n;
    protected Apfloat randomizedRoundingParameter;


    public MP12Parameters(SecureRandom random, int n, Apfloat randomizedRoundingParameter) {
        this.random = random;
        this.n = n;
        this.randomizedRoundingParameter = randomizedRoundingParameter;
    }

    public MP12Parameters(SecureRandom random, int n) {
        this.random = random;
        this.n = n;
        this.randomizedRoundingParameter = MP12P2Utils.RRP;
    }


    public SecureRandom getRandom() {
        return random;
    }

    public int getN() {
        return n;
    }

    public Apfloat getRandomizedRoundingParameter() {
        return randomizedRoundingParameter;
    }

}