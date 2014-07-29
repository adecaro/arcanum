package org.arcanum.fe.abe.gghvv13;

import org.arcanum.AbstractArcanumCryptoTest;
import org.arcanum.Pairing;
import org.arcanum.circuit.BooleanCircuit;
import org.arcanum.circuit.smart.SmartBooleanCircuitLoader;
import org.arcanum.fe.abe.gghvv13.engines.GGHVV13KEMEngine;
import org.arcanum.fe.abe.gghvv13.generators.GGHVV13KeyPairGenerator;
import org.arcanum.fe.abe.gghvv13.generators.GGHVV13ParametersGenerator;
import org.arcanum.fe.abe.gghvv13.generators.GGHVV13SecretKeyGenerator;
import org.arcanum.fe.abe.gghvv13.params.*;
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
public class GGHVV13KEMEngineTest extends AbstractArcanumCryptoTest {

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {false, "org/arcanum/params/ctl13_toy.properties"}
        };

        return Arrays.asList(data);
    }

    protected Pairing pairing;
    protected SecureRandom random;

    public GGHVV13KEMEngineTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }

    @Override
    public void before() throws Exception {
        super.before();

        this.random = SecureRandom.getInstance("SHA1PRNG");
        this.pairing = PairingFactory.getPairing(parameters);
    }

    @Test
    public void testGGHVV13KEMEngine() {
        BooleanCircuit circuit =new SmartBooleanCircuitLoader().load(
                "org/arcanum/circuits/bool/circuit3.txt"
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


    protected GGHVV13Parameters createParameters(int n) {
        return new GGHVV13ParametersGenerator().init(pairing, n).generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(GGHVV13Parameters parameters) {
        GGHVV13KeyPairGenerator setup = new GGHVV13KeyPairGenerator();
        setup.init(new GGHVV13KeyPairGenerationParameters(
                random,
                parameters
        ));

        return setup.generateKeyPair();
    }

    protected byte[][] encaps(CipherParameters publicKey, String w) {
        try {
            KeyEncapsulationMechanism kem = new GGHVV13KEMEngine();
            kem.init(true, new GGHVV13EncryptionParameters((GGHVV13PublicKeyParameters) publicKey, w));

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
        GGHVV13SecretKeyGenerator keyGen = new GGHVV13SecretKeyGenerator();
        keyGen.init(new GGHVV13SecretKeyGenerationParameters(
                (GGHVV13PublicKeyParameters) publicKey,
                (GGHVV13MasterSecretKeyParameters) masterSecretKey,
                circuit
        ));

        // Generate the key
        return keyGen.generateKey();
    }

    protected byte[] decaps(CipherParameters secretKey, byte[] ciphertext) {
        try {
            KeyEncapsulationMechanism kem = new GGHVV13KEMEngine();

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