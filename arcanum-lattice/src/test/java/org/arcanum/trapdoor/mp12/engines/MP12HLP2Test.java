package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.ElementCipher;
import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.field.vector.TwoByColumnMatrixElement;
import org.arcanum.field.vector.VectorField;
import org.arcanum.trapdoor.mp12.generators.MP12HLP2KeyPairGenerator;
import org.arcanum.trapdoor.mp12.params.*;
import org.arcanum.util.cipher.params.ElementKeyPairParameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2Test {

    int k = 32;
    private MP12HLP2KeyPairGenerator gen;
    private ElementKeyPairParameters keyPair;
    private MP12HLP2PublicKeyParameters pk;
    private MP12HLP2PrivateKeyParameters sk;
    private SecureRandom random;

    @Before
    public void setUp() throws Exception {
        long start = System.currentTimeMillis();

        this.random = SecureRandom.getInstance("SHA1PRNG");
        gen = new MP12HLP2KeyPairGenerator();
        gen.init(new MP12HLP2KeyPairGenerationParameters(
                random,
                4, // n
                32 // k
        ));
        keyPair = gen.generateKeyPair();
        pk = (MP12HLP2PublicKeyParameters) keyPair.getPublic();
        sk = (MP12HLP2PrivateKeyParameters) keyPair.getPrivate();

        long end = System.currentTimeMillis();

        System.out.println("+ (end-start) = " + (end - start));
    }

    @Test
    public void testSampleD() throws Exception {
        // Sample
        Element syndrome = pk.getSyndromeField().newRandomElement();
        System.out.println("syndrome = " + syndrome);

        MP12HLP2Sampler sampler = new MP12HLP2Sampler();
        sampler.init(new MP12HLP2SampleParameters(keyPair));

        long start = System.currentTimeMillis();
        Element x = sampler.processElements(syndrome);
        long end = System.currentTimeMillis();
        System.out.println("processElements (end-start) = " + (end - start));
        System.out.println("x = " + x);

        // Decode
        MP12HLP2Decoder decoder = new MP12HLP2Decoder();
        decoder.init(keyPair.getPublic());
        Element syndromePrime = decoder.processElements(x);
        System.out.println("syndromePrime = " + syndromePrime);

        assertEquals(syndrome, syndromePrime);
    }

    @Test
    public void testSampleDMatrix() throws Exception {
        MatrixField<Field> RField = new MatrixField<Field>(pk.getParameters().getRandom(), pk.getZq(), pk.getM());
        // Compute U
        Matrix U = (Matrix) pk.getA().getField().newRandomElement();

//        System.out.println("U = " + U);

        // Sample R0
        MP12HLP2MatrixSampler sampleD = new MP12HLP2MatrixSampler(RField);
        sampleD.init(new MP12HLP2SampleParameters(keyPair.getPublic(), keyPair.getPrivate()));

        Matrix R0 = (Matrix) sampleD.processElements(U);

//        System.out.println("R0 = " + R0);

        // Decode
        MP12HLP2Decoder decoder = new MP12HLP2Decoder();
        decoder.init(keyPair.getPublic());

        Matrix U1 = (Matrix) U.getField().newElement();
        for (int i = 0; i < pk.getM(); i++) {
            Element sample = R0.getColumnAt(i);
//            System.out.println("sample = " + sample);
            Element u = decoder.processElements(sample);
//            System.out.println("u = " + u);
            U1.setColAt(i, u);
        }
//        System.out.println("U1 = " + U1);

        Element U2 = pk.getA().mul(R0);
//        System.out.println("U2 = " + U2);

        assertEquals(U, U1);
        assertEquals(U, U2);
    }

    @Test
    public void testInverter() throws Exception {
        // b=OWF(s)
        ElementCipher owf = new MP12HLP2OneWayFunction();
        MP12HLP2OneWayFunctionParameters owfParams = new MP12HLP2OneWayFunctionParameters(pk);
        owf.init(owfParams);

        Element s = owfParams.getInputField().newRandomElement();
        Element b = owf.processElements(s);

        System.out.println("s = " + s);
        System.out.println("b = " + b);

        // s'=Invert(b)
        MP12HLP2Inverter inverter = new MP12HLP2Inverter();
        inverter.init(new MP12HLP2InverterParameters(
                (MP12HLP2PublicKeyParameters) keyPair.getPublic(),
                (MP12HLP2PrivateKeyParameters) keyPair.getPrivate()
        ));
        Element sPrime = inverter.processElements(b);
        System.out.println("sPrime = " + sPrime);

        // Check for equality
        Assert.assertEquals(s, sPrime);
    }

    @Test
    public void testErrorTolerantOneTimePad() throws Exception {
        // Init OWF
        ElementCipher owf = new MP12HLP2OneWayFunction();
        MP12HLP2OneWayFunctionParameters owfParams = new MP12HLP2OneWayFunctionParameters(pk);
        owf.init(owfParams);
        Element key = owf.processElements(owfParams.getInputField().newRandomElement());

        // Init OTP
        ElementCipher otp = new MP12HLP2ErrorTolerantOneTimePad();
        otp.init(key);

        byte[] bytes = new byte[gen.getMInBytes()];
        random.nextBytes(bytes);
        System.out.println("bytes = " + Arrays.toString(bytes));

        Element c = otp.processBytes(bytes);

        byte[] bytesPrime = otp.processElementsToBytes(c);
        System.out.println("bytesPrime = " + Arrays.toString(bytesPrime));

        Assert.assertArrayEquals(bytes, bytesPrime);
    }

    @Test
    public void testLeftSampler() throws Exception {
        MP12HLP2LeftSampler sampler = new MP12HLP2LeftSampler();
        sampler.init(new MP12HLP2SampleLeftParameters(keyPair, pk.getM()));

        Element A1 = pk.getA().getField().newRandomElement();
        Element u = VectorField.newRandomElement(pk.getZq(), pk.getParameters().getN());

        Element F = new TwoByColumnMatrixElement((Matrix) pk.getA(), (Matrix) A1);

        Element rOut = sampler.processElements(A1, u);
        Element uPrime = F.mul(rOut);

        assertTrue(u.equals(uPrime));
    }

    @Test
    public void testMatrixLeftSampler() throws Exception {
        MP12HLP2MatrixLeftSampler sampler = new MP12HLP2MatrixLeftSampler();
        sampler.init(new MP12HLP2SampleLeftParameters(keyPair, pk.getM()));

        Element A1 = pk.getA().getField().newRandomElement();
        Element U = new MatrixField<Field>(pk.getParameters().getRandom(), pk.getZq(), pk.getParameters().getN(), pk.getM()).newRandomElement();
        Element R = sampler.processElements(A1, U);

        Element F = new TwoByColumnMatrixElement((Matrix) pk.getA(), (Matrix) A1);
        Element UPrime = F.mul(R);

        assertTrue(U.equals(UPrime));
    }


}
