package org.arcanum.fe.rl.w12;

import org.arcanum.dfa.DFA;
import org.arcanum.dfa.DefaultDFA;
import org.arcanum.fe.rl.w12.engines.RLW12KEMEngine;
import org.arcanum.fe.rl.w12.generators.RLW12KeyPairGenerator;
import org.arcanum.fe.rl.w12.generators.RLW12ParametersGenerator;
import org.arcanum.fe.rl.w12.generators.RLW12SecretKeyGenerator;
import org.arcanum.fe.rl.w12.params.*;
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
public class RLW12KEM {


    public RLW12KEM() {
    }


    public AsymmetricCipherKeyPair setup(DFA.Alphabet alphabet) {
        RLW12KeyPairGenerator setup = new RLW12KeyPairGenerator();
        setup.init(new RLW12KeyPairGenerationParameters(
                new SecureRandom(),
                new RLW12ParametersGenerator().init(
                        PairingFactory.getPairingParameters("params/curves/a.properties"),
                        alphabet).generateParameters()
        ));
        return setup.generateKeyPair();
    }

    public byte[][] encaps(CipherParameters publicKey, String w) {
        KeyEncapsulationMechanism kem = new RLW12KEMEngine();
        kem.init(true, new RLW12EncryptionParameters((RLW12PublicKeyParameters) publicKey, w));

        byte[] ciphertext = kem.process();

        assertNotNull(ciphertext);
        assertNotSame(0, ciphertext.length);

        byte[] key = Arrays.copyOfRange(ciphertext, 0, kem.getKeyBlockSize());
        byte[] ct = Arrays.copyOfRange(ciphertext, kem.getKeyBlockSize(), ciphertext.length);

        return new byte[][]{key, ct};
    }

    public CipherParameters keyGen(CipherParameters publicKey, CipherParameters masterSecretKey, DFA dfa) {
        RLW12SecretKeyGenerator keyGen = new RLW12SecretKeyGenerator();
        keyGen.init(new RLW12SecretKeyGenerationParameters(
                (RLW12PublicKeyParameters) publicKey,
                (RLW12MasterSecretKeyParameters) masterSecretKey,
                dfa
        ));

        return keyGen.generateKey();
    }

    public byte[] decaps(CipherParameters secretKey, byte[] ciphertext) {
        KeyEncapsulationMechanism kem = new RLW12KEMEngine();

        kem.init(false, secretKey);
        byte[] key = kem.processBlock(ciphertext);

        assertNotNull(key);
        assertNotSame(0, key.length);

        return key;
    }


    public static void main(String[] args) {
        DefaultDFA dfa = new DefaultDFA(2);
        dfa.addFinalState(0);
        dfa.addTransition(0, '0', 1);
        dfa.addTransition(0, '1', 0);
        dfa.addTransition(1, '0', 0);
        dfa.addTransition(1, '1', 1);

        DefaultDFA.DefaultAlphabet alphabet = new DefaultDFA.DefaultAlphabet();
        alphabet.addLetter('0', '1');

        RLW12KEM rlw12KEM = new RLW12KEM();

        // setup
        AsymmetricCipherKeyPair keyPair = rlw12KEM.setup(alphabet);

        // keygen
        CipherParameters secretKey = rlw12KEM.keyGen(keyPair.getPublic(), keyPair.getPrivate(), dfa);

        // Encaps/Decaps for accepting word
        String w = "00111100";
        assertTrue(dfa.accept(w));
        byte[][] ct = rlw12KEM.encaps(keyPair.getPublic(), w);
        assertEquals(true, Arrays.equals(ct[0], rlw12KEM.decaps(secretKey, ct[1])));

        // Encaps/Decaps for non-accepting word
        w = "01111100";
        assertFalse(dfa.accept(w));
        ct = rlw12KEM.encaps(keyPair.getPublic(), "01111100");
        assertEquals(false, Arrays.equals(ct[0], rlw12KEM.decaps(secretKey, ct[1])));
    }
}

