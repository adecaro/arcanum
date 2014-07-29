package org.arcanum.fe.abe.bns14.engines;

import org.arcanum.Element;
import org.arcanum.Field;
import org.arcanum.circuit.ArithmeticCircuit;
import org.arcanum.circuit.smart.SmartArithmeticCircuitLoader;
import org.arcanum.fe.abe.bns14.generators.BNS14KeyPairGenerator;
import org.arcanum.fe.abe.bns14.generators.BNS14ParametersGenerator;
import org.arcanum.fe.abe.bns14.generators.BNS14SecretKeyGenerator;
import org.arcanum.fe.abe.bns14.params.*;
import org.arcanum.kem.KeyEncapsulationMechanism;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.StringTokenizer;

import static org.junit.Assert.*;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BNS14KEMEngineTest {

    private SecureRandom random;

    @Before
    public void setUp() throws Exception {
        random = SecureRandom.getInstance("SHA1PRNG");
    }

    @Test
    public void testBNS14KEMEngine() {

        AsymmetricCipherKeyPair keyPair = setup(4, 2);
        Field Zq = ((BNS14PublicKeyParameters) keyPair.getPublic()).getLatticePk().getZq();

        ArithmeticCircuit circuit = new SmartArithmeticCircuitLoader().load(
                Zq, "org/arcanum/circuits/arithmetic/circuit4.txt"
        );


        BNS14SecretKeyParameters secretKey = (BNS14SecretKeyParameters) keyGen(keyPair.getPublic(), keyPair.getPrivate(), circuit);

        // Encaps/Decaps for a satisfying assignment
        byte[][] ct = encaps(keyPair.getPublic(), toElement(Zq, "1 0 1 -1", circuit.getNumInputs()));
        byte[] key = ct[0];
        byte[] keyPrime = decaps(secretKey, ct[1]);

        System.out.println("key      = " + Arrays.toString(key));
        System.out.println("keyPrime = " + Arrays.toString(keyPrime));
        assertEquals(true, Arrays.equals(key, keyPrime));

        // Encaps/Decaps for a non-satisfying assignment
        ct = encaps(keyPair.getPublic(), toElement(Zq, "1 1 1 1", circuit.getNumInputs()));
        key = ct[0];
        keyPrime = decaps(secretKey, ct[1]);

        System.out.println("key      = " + Arrays.toString(key));
        System.out.println("keyPrime = " + Arrays.toString(keyPrime));
        assertEquals(false, Arrays.equals(key, keyPrime));
    }


    protected AsymmetricCipherKeyPair setup(int ell, int depth) {
        BNS14KeyPairGenerator gen = new BNS14KeyPairGenerator();
        gen.init(
                new BNS14KeyPairGenerationParameters(
                        random,
                        new BNS14ParametersGenerator(random, ell, depth).generateParameters()
                )
        );
        return gen.generateKeyPair();
    }

    protected Element[] toElement(Field Zq, String assignment, int ell) {
        Element[] elements = new Element[ell];
        StringTokenizer st = new StringTokenizer(assignment, " ");
        int i = 0;
        while (st.hasMoreTokens() && i < ell) {
            elements[i++] = Zq.newElement(new BigInteger(st.nextToken()));
        }

        return elements;
    }

    protected byte[][] encaps(CipherParameters publicKey, Element[] w) {
        KeyEncapsulationMechanism kem = new BNS14KEMEngine();
        kem.init(true, new BNS14EncryptionParameters((BNS14PublicKeyParameters) publicKey, w));

        byte[] ciphertext = kem.process();

        assertNotNull(ciphertext);
        assertNotSame(0, ciphertext.length);

        byte[] key = Arrays.copyOfRange(ciphertext, 0, kem.getKeyBlockSize());
        byte[] ct = Arrays.copyOfRange(ciphertext, kem.getKeyBlockSize(), ciphertext.length);

        return new byte[][]{key, ct};
    }

    protected CipherParameters keyGen(CipherParameters publicKey, CipherParameters masterSecretKey, ArithmeticCircuit circuit) {
        return new BNS14SecretKeyGenerator().init(
                new BNS14SecretKeyGenerationParameters(
                        (BNS14PublicKeyParameters) publicKey,
                        (BNS14MasterSecretKeyParameters) masterSecretKey,
                        circuit
                )
        ).generateKey();
    }

    protected byte[] decaps(CipherParameters secretKey, byte[] ciphertext) {
        return new BNS14KEMEngine().initForDecryption(secretKey).processBlock(ciphertext);
    }

}