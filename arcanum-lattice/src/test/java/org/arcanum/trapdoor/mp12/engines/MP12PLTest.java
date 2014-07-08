package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.trapdoor.mp12.generators.MP12PLKeyPairGenerator;
import org.arcanum.trapdoor.mp12.params.MP12PLKeyPairGenerationParameters;
import org.arcanum.trapdoor.mp12.params.MP12PLPublicKeyParameters;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;

import static org.junit.Assert.assertTrue;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12PLTest {

    protected SecureRandom random;
    protected MP12PLKeyPairGenerator gen;
    protected MP12PLPublicKeyParameters pk;

    @Before
    public void setUp() throws Exception {
        this.random = SecureRandom.getInstance("SHA1PRNG");

        this.gen = new MP12PLKeyPairGenerator();
        this.gen.init(new MP12PLKeyPairGenerationParameters(
                random,
                3,  // n
                BigInteger.probablePrime(20, random)
        ));
        this.pk = (MP12PLPublicKeyParameters) gen.generateKeyPair().getPublic();
    }


    @Test
    public void testSampleD() throws Exception {
        Element u = pk.getSyndromeField().newRandomElement();
        Element x = new MP12PLSampler().init(pk).processElements(u);
        Element uPrime = new MP12PLDecoder().init(pk).processElements(x);

        System.out.println("u = " + u);
        System.out.println("x = " + x);
        System.out.println("uPrime = " + uPrime);
        assertTrue(u.equals(uPrime));
    }

}
