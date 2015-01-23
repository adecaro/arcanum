package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.Matrix;
import org.arcanum.trapdoor.mp12.generators.MP12PLP2KeyPairGenerator;
import org.arcanum.trapdoor.mp12.params.MP12PLP2KeyPairGenerationParameters;
import org.arcanum.trapdoor.mp12.params.MP12PLP2PublicKeyParameters;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;

import static org.junit.Assert.assertTrue;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12PLP2Test {

    protected SecureRandom random;
    protected MP12PLP2KeyPairGenerator gen;
    protected MP12PLP2PublicKeyParameters pk;

    @Before
    public void setUp() throws Exception {
        this.random = SecureRandom.getInstance("SHA1PRNG");

        this.gen = new MP12PLP2KeyPairGenerator();
        this.gen.init(new MP12PLP2KeyPairGenerationParameters(
                random,
                3,  // n
                24  // k
        ));
        this.pk = (MP12PLP2PublicKeyParameters) gen.generateKeyPair().getPublic();
    }


    @Test
    public void testSampleD() throws Exception {
        Element u = pk.getSyndromeField().newRandomElement();
        Element x = new MP12PLP2Sampler().init(pk).processElements(u);
        Element uPrime = new MP12PLDecoder().init(pk).processElements(x);

        System.out.println("u = " + u);
        System.out.println("x = " + x);
        System.out.println("uPrime = " + uPrime);
        assertTrue(u.equals(uPrime));
    }

    @Test
    public void testSolver() throws Exception {
        Element u = pk.getSyndromeField().newRandomElement();
        Element x = new MP12PLP2Solver().init(pk).processElements(u);
        Element uPrime = new MP12PLDecoder().init(pk).processElements(x);

        System.out.println("u = " + u);
        System.out.println("x = " + x);
        System.out.println("uPrime = " + uPrime);
        assertTrue(u.equals(uPrime));
    }


    @Test
    public void testSampleDMatrix() throws Exception {
        // Compute U
        Matrix U = pk.getG().duplicate().mulZn(pk.getZq().newRandomElement());

        // Sample
        MP12PLP2MatrixSampler sampleD = new MP12PLP2MatrixSampler();
        sampleD.init(pk);

        Matrix R0 = sampleD.processElements(U);

        // Decode
        MP12PLDecoder decoder = new MP12PLDecoder();
        decoder.init(pk);

        Matrix U1 = (Matrix) U.getField().newElement();
        for (int i = 0; i < U.getM(); i++) {
            Element sample = R0.getColumnAt(i);
            Element u = decoder.processElements(sample);
            U1.setColAt(i, u);
        }

        Element U2 = pk.getG().mul(R0);

        assertTrue(U.equals(U1));
        assertTrue(U.equals(U2));
    }


    @Test
    public void testSolverMatrix() throws Exception {
        // Compute U
        Matrix U = pk.getG().duplicate().mulZn(pk.getZq().newRandomElement());

        // Sample
        MP12PLP2MatrixSolver sampleD = new MP12PLP2MatrixSolver();
        sampleD.init(pk);

        Matrix R0 = sampleD.processElements(U);
        System.out.println("R0 = " + R0);

        // Decode
        MP12PLDecoder decoder = new MP12PLDecoder();
        decoder.init(pk);

        Matrix U1 = (Matrix) U.getField().newElement();
        for (int i = 0; i < U.getM(); i++) {
            Element sample = R0.getColumnAt(i);
            Element u = decoder.processElements(sample);
            U1.setColAt(i, u);
        }

        Element U2 = pk.getG().mul(R0);

        assertTrue(U.equals(U1));
        assertTrue(U.equals(U2));
    }

}
