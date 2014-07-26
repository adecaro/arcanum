package org.arcanum.fe.abe.gghsw13;

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

import java.security.SecureRandom;
import java.util.Arrays;

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
        KeyEncapsulationMechanism kem = new GGHSW13KEMEngine();
        kem.init(true, new GGHSW13EncryptionParameters((GGHSW13PublicKeyParameters) publicKey, w));

        byte[] ciphertext = kem.process();

        assertNotNull(ciphertext);
        assertNotSame(0, ciphertext.length);

        byte[] key = Arrays.copyOfRange(ciphertext, 0, kem.getKeyBlockSize());
        byte[] ct = Arrays.copyOfRange(ciphertext, kem.getKeyBlockSize(), ciphertext.length);

        return new byte[][]{key, ct};
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
        KeyEncapsulationMechanism kem = new GGHSW13KEMEngine();

        kem.init(false, secretKey);
        byte[] key = kem.processBlock(ciphertext);

        assertNotNull(key);
        assertNotSame(0, key.length);

        return key;
    }


    public static void main(String[] args) {
        BooleanCircuit circuit =new SmartBooleanCircuitLoader().load(
                "org/arcanum/circuits/circuit3.txt"
        );

        GGHSW13KEM kem = new GGHSW13KEM();

        // Setup
        AsymmetricCipherKeyPair keyPair = kem.setup(circuit.getNumInputs());

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