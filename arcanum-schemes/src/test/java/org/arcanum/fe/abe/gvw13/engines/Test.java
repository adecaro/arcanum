package org.arcanum.fe.abe.gvw13.engines;

import junit.framework.Assert;
import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.Sampler;
import org.arcanum.field.util.ElementUtils;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.field.vector.VectorField;
import org.arcanum.field.z.SymmetricZrField;
import org.arcanum.sampler.UniformOneMinusOneSampler;
import org.arcanum.trapdoor.mp12.engines.MP12HLP2LeftSampler;
import org.arcanum.trapdoor.mp12.engines.MP12HLP2MatrixSampler;
import org.arcanum.trapdoor.mp12.generators.MP12HLP2KeyPairGenerator;
import org.arcanum.trapdoor.mp12.params.MP12HLP2KeyPairGenerationParameters;
import org.arcanum.trapdoor.mp12.params.MP12HLP2PublicKeyParameters;
import org.arcanum.trapdoor.mp12.params.MP12HLP2SampleParameters;
import org.arcanum.trapdoor.mp12.utils.MP12P2Utils;
import org.arcanum.util.cipher.params.ElementKeyPairParameters;
import org.arcanum.util.math.BigIntegerUtils;

import java.math.BigInteger;
import java.security.SecureRandom;

import static org.arcanum.field.util.ElementUtils.bd;
import static org.arcanum.field.util.ElementUtils.newDiagonalPrimitiveMatrix;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class Test {

    @org.junit.Test
    public void test2() {
        SecureRandom random = new SecureRandom();
        int n = 4;
        int k = 4;
        int m = 4;
        Field Zq = new SymmetricZrField(random, BigInteger.valueOf((int) Math.pow(2, k)));

        Matrix A = (Matrix) newDiagonalPrimitiveMatrix(random, Zq, n, k);
        Matrix B = new MatrixField<Field>(random, Zq, n, m).newRandomElement();

        System.out.println("A = " + A);
        System.out.println("B = " + B);

        Element BD = bd(B, k);
        System.out.println("BD = " + BD);

        Element C = A.mul(BD);

        System.out.println("C = " + C);

        Assert.assertTrue(C.equals(B));
    }


    @org.junit.Test
    public void test1() {
        try {
            System.out.println("Setup");

            // setup
            int ell = 2;
            int n = 4;
            int k = 64;

            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

            MP12HLP2KeyPairGenerator gen = new MP12HLP2KeyPairGenerator();
            gen.init(new MP12HLP2KeyPairGenerationParameters(random, n, k));

            // Generate trapdoors
            ElementKeyPairParameters trA = gen.generateKeyPair();
            ElementKeyPairParameters trB = gen.generateKeyPair();
            MP12HLP2PublicKeyParameters pkA = (MP12HLP2PublicKeyParameters) trA.getPublic();
            MP12HLP2PublicKeyParameters pkB = (MP12HLP2PublicKeyParameters) trB.getPublic();

            Field Zq = pkA.getZq();
            BigInteger order = pkA.getZq().getOrder();
            BigInteger halfOrder = pkA.getZq().getOrder().divide(BigIntegerUtils.TWO);
            BigInteger oneFourthOrder = order.divide(BigIntegerUtils.FOUR);

            // generate public matrices
            Element[] As = new Element[ell];
            for (int i = 0; i < ell; i++) {
                As[i] = pkA.getA().getField().newRandomElement();
            }

            VectorField vfn = new VectorField(random, pkA.getZq(), n);
            // generate u
            Element u = vfn.newRandomElement();

            // Sample RB
            MP12HLP2MatrixSampler sampleD = new MP12HLP2MatrixSampler();
            sampleD.init(new MP12HLP2SampleParameters(trB));


            Element P = newDiagonalPrimitiveMatrix(random, Zq, n, k);
            Matrix RB = (Matrix) sampleD.processElements(P);
            assertTrue(pkB.getA().mul(RB).equals(P));

            // Encrypt

            System.out.println("Encrypt");

            Element s = vfn.newRandomElement();
            Sampler<BigInteger> csi = MP12P2Utils.getLWENoiseSampler(pkA.getParameters().getRandom(), pkA.getParameters().getN());
            Element e = new VectorField<Field>(random, pkA.getZq(), pkA.getM()).newElementFromSampler(csi);

            System.out.println("s = " + s);
            System.out.println("e = " + e);

            Element psi = pkA.getA().mul(s).add(e);

            Element[] psis = new Element[ell];
            int[] x = new int[]{1, 1};
            for (int i = 0; i < ell; i++) {
                Element Ri = new MatrixField<Field>(random, pkA.getZq(), pkA.getM()).newElementFromSampler(new UniformOneMinusOneSampler(random));
                Element ei = Ri.mul(e);

                if (x[i] == 0)
                    psis[i] = As[i].mul(s).add(ei);
                else
                    psis[i] = As[i].duplicate().add(pkB.getA()).mul(s).add(ei);
            }
            int bit = 1;
            Element tau = u.mul(s).add(csi.sample());
            if (bit == 1)
                tau.add(halfOrder);

            System.out.println("KeyGen");
            //Key Gen - NAND on input A0 and A1
            //          A2 = A0 \cdot RB \cdot BD(A1) - B

            Element Aout = As[1].mul(RB).mul(bd(As[0], k));//.sub(pkB.getA());

            MP12HLP2LeftSampler leftSampler = new MP12HLP2LeftSampler();
            leftSampler.init(new MP12HLP2SampleParameters(trA));

            Element APrime = Aout.sub(pkB.getA());
            Element rOut = leftSampler.processElements(APrime, u);
            assertTrue(MatrixField.unionByCol(pkA.getA(),APrime).mul(rOut).equals(u));

            // Decrypt
            System.out.println("Decrypt");

            Element psiOut = RB.mul(bd(As[0], k));
            System.out.println("Decrypt.1");

            psiOut = ((Matrix)psiOut).mulFromTranspose(psis[1]);
            System.out.println("Decrypt.2");

            if (x[1] != 0)
                psiOut = psiOut.sub(psis[0].mul(x[1]));
            System.out.println("Decrypt.3");

            Element beta = rOut.mul(ElementUtils.union(psi, psiOut));

            System.out.println("tau = " + tau);
            System.out.println("beta = " + beta);

            Element diff = tau.duplicate().sub(beta);

            System.out.println("diff = " + diff);
            System.out.println("oneFourthOrder = " + oneFourthOrder);

            int bitPrime = (diff.toBigInteger().abs().compareTo(oneFourthOrder) >= 0) ? 1 : 0;

            assertSame(bit, bitPrime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
