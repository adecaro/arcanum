package org.arcanum.fe.abe.gghsw13;

import org.arcanum.circuit.BooleanCircuit;
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

import java.security.SecureRandom;
import java.util.Arrays;

import static org.arcanum.circuit.BooleanCircuit.BooleanCircuitGate;
import static org.arcanum.circuit.Gate.Type.*;
import static org.junit.Assert.*;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GGHSW13KEM {

    public GGHSW13KEM() {
    }


    public AsymmetricCipherKeyPair setup(int n) {
        GGHSW13KeyPairGenerator setup = new GGHSW13KeyPairGenerator();
        setup.init(new GGHSW13KeyPairGenerationParameters(
                new SecureRandom(),
                new GGHSW13ParametersGenerator().init(
                        PairingFactory.getPairing("params/mm/ctl13/toy.properties"),
                        n).generateParameters()
        ));

        return setup.generateKeyPair();
    }

    public byte[][] encaps(CipherParameters publicKey, String w) {
        try {
            KeyEncapsulationMechanism kem = new GGHSW13KEMEngine();
            kem.init(true, new GGHSW13EncryptionParameters((GGHSW13PublicKeyParameters) publicKey, w));

            byte[] ciphertext = kem.process();

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

    public CipherParameters keyGen(CipherParameters publicKey, CipherParameters masterSecretKey, BooleanCircuit circuit) {
        GGHSW13SecretKeyGenerator keyGen = new GGHSW13SecretKeyGenerator();
        keyGen.init(new GGHSW13SecretKeyGenerationParameters(
                (GGHSW13PublicKeyParameters) publicKey,
                (GGHSW13MasterSecretKeyParameters) masterSecretKey,
                circuit
        ));

        return keyGen.generateKey();
    }

    public byte[] decaps(CipherParameters secretKey, byte[] ciphertext) {
        try {
            KeyEncapsulationMechanism kem = new GGHSW13KEMEngine();

            kem.init(false, secretKey);
            byte[] key = kem.processBlock(ciphertext);

            assertNotNull(key);
            assertNotSame(0, key.length);

            return key;
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        return null;
    }


    public static void main(String[] args) {
        int n = 4;
        int q = 3;
        BooleanCircuit circuit = new BooleanCircuit(n, q, 3, new BooleanCircuitGate[]{
                new BooleanCircuitGate(INPUT, 0, 1),
                new BooleanCircuitGate(INPUT, 1, 1),
                new BooleanCircuitGate(INPUT, 2, 1),
                new BooleanCircuitGate(INPUT, 3, 1),

                new BooleanCircuitGate(AND, 4, 2, new int[]{0, 1}),
                new BooleanCircuitGate(OR, 5, 2, new int[]{2, 3}),

                new BooleanCircuitGate(AND, 6, 3, new int[]{4, 5}),
        });

        GGHSW13KEM kem = new GGHSW13KEM();

        // Setup
        AsymmetricCipherKeyPair keyPair = kem.setup(n);

        // Keygen
        CipherParameters secretKey = kem.keyGen(keyPair.getPublic(), keyPair.getPrivate(), circuit);

        // Encaps/Decaps for satisfying assignment
        String assignment = "1101";
        byte[][] ct = kem.encaps(keyPair.getPublic(), assignment);
        assertEquals(true, Arrays.equals(ct[0], kem.decaps(secretKey, ct[1])));

        // Encaps/Decaps for not-satisfying assignment
        assignment = "1001";
        ct = kem.encaps(keyPair.getPublic(), assignment);
        assertEquals(false, Arrays.equals(ct[0], kem.decaps(secretKey, ct[1])));
    }

}