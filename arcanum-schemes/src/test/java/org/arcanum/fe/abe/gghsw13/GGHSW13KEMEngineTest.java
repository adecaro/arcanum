package org.arcanum.fe.abe.gghsw13;

import org.arcanum.AbstractArcanumCryptoTest;
import org.arcanum.Pairing;
import org.arcanum.circuit.BooleanCircuit;
import org.arcanum.circuit.smart.SmartBooleanCircuitLoader;
import org.arcanum.fe.abe.gghsw13.engines.GGHSW13KEMEngine;
import org.arcanum.fe.abe.gghsw13.generators.GGHSW13KeyPairGenerator;
import org.arcanum.fe.abe.gghsw13.generators.GGHSW13ParametersGenerator;
import org.arcanum.fe.abe.gghsw13.generators.GGHSW13SecretKeyGenerator;
import org.arcanum.fe.abe.gghsw13.params.*;
import org.arcanum.kem.KeyEncapsulationMechanism;
import org.arcanum.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * @author Angelo De Caro
 */
public class GGHSW13KEMEngineTest extends AbstractArcanumCryptoTest {

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {false, "org/arcanum/params/ctl13_toy.properties"}
        };

        return Arrays.asList(data);
    }

    protected Pairing pairing;

    public GGHSW13KEMEngineTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }

    @Override
    public void before() throws Exception {
        super.before();

        this.pairing = PairingFactory.getPairing(parameters);
    }

    @Test
    public void testGGHSW13KEMEngine() {
        BooleanCircuit circuit =new SmartBooleanCircuitLoader().load(
                "org/arcanum/circuits/circuit3.txt"
        );

        AsymmetricCipherKeyPair keyPair = setup(createParameters(circuit.getNumInputs()));
        CipherParameters secretKey = keyGen(keyPair.getPublic(), keyPair.getPrivate(), circuit);

        String assignment = "1101";
        byte[][] ct = encaps(keyPair.getPublic(), assignment);
        assertEquals(true, Arrays.equals(ct[0], decaps(secretKey, ct[1])));

        assignment = "1001";
        ct = encaps(keyPair.getPublic(), assignment);
        assertEquals(false, Arrays.equals(ct[0], decaps(secretKey, ct[1])));
    }


    protected GGHSW13Parameters createParameters(int n) {
        return new GGHSW13ParametersGenerator().init(pairing, n).generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(GGHSW13Parameters parameters) {
        GGHSW13KeyPairGenerator setup = new GGHSW13KeyPairGenerator();
        setup.init(new GGHSW13KeyPairGenerationParameters(
                new SecureRandom(),
                parameters
        ));

        return setup.generateKeyPair();
    }

    protected byte[][] encaps(CipherParameters publicKey, String w) {
        try {
            KeyEncapsulationMechanism kem = new GGHSW13KEMEngine();
            kem.init(true, new GGHSW13EncryptionParameters((GGHSW13PublicKeyParameters) publicKey, w));

            byte[] ciphertext = kem.processBlock(new byte[0], 0, 0);

            assertNotNull(ciphertext);
            assertNotSame(0, ciphertext.length);

            byte[] key = Arrays.copyOfRange(ciphertext, 0, kem.getKeyBlockSize());
            byte[] ct = Arrays.copyOfRange(ciphertext, kem.getKeyBlockSize(), ciphertext.length);

            return new byte[][]{key, ct};
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return null;
    }

    protected CipherParameters keyGen(CipherParameters publicKey, CipherParameters masterSecretKey, BooleanCircuit circuit) {
        // Init the Generator
        GGHSW13SecretKeyGenerator keyGen = new GGHSW13SecretKeyGenerator();
        keyGen.init(new GGHSW13SecretKeyGenerationParameters(
                (GGHSW13PublicKeyParameters) publicKey,
                (GGHSW13MasterSecretKeyParameters) masterSecretKey,
                circuit
        ));

        // Generate the key
        return keyGen.generateKey();
    }

    protected byte[] decaps(CipherParameters secretKey, byte[] ciphertext) {
        try {
            KeyEncapsulationMechanism kem = new GGHSW13KEMEngine();

            kem.init(false, secretKey);
            byte[] key = kem.processBlock(ciphertext, 0, ciphertext.length);

            assertNotNull(key);
            assertNotSame(0, key.length);

            return key;
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        return null;
    }

}