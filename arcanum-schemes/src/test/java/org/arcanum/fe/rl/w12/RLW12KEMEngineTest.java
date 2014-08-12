package org.arcanum.fe.rl.w12;

import org.arcanum.common.fe.generator.SecretKeyGenerator;
import org.arcanum.common.kem.KeyEncapsulationMechanism;
import org.arcanum.common.kem.KeyEncapsulationMechanism.Pair;
import org.arcanum.fe.AbstractPairingKEMEngineTest;
import org.arcanum.fe.rl.w12.engines.RLW12KEMEngine;
import org.arcanum.fe.rl.w12.generators.RLW12KeyPairGenerator;
import org.arcanum.fe.rl.w12.generators.RLW12ParametersGenerator;
import org.arcanum.fe.rl.w12.generators.RLW12SecretKeyGenerator;
import org.arcanum.program.assignment.CharAssignment;
import org.arcanum.program.dfa.DFA;
import org.arcanum.program.dfa.DefaultDFA;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.CipherParameters;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author Angelo De Caro
 */

public class RLW12KEMEngineTest extends AbstractPairingKEMEngineTest<DFA> {


    public RLW12KEMEngineTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }

    @Test
    public void testEncapsDecaps() {
        // 1. (MPK,MSK) <- Setup
        setup();

        // 2. SK <- KeyGen(MSK, circuit)
        DefaultDFA dfa = new DefaultDFA(2);
        dfa.addFinalState(0);
        dfa.addTransition(0, '0', 1);
        dfa.addTransition(0, '1', 0);
        dfa.addTransition(1, '0', 0);
        dfa.addTransition(1, '1', 1);

        CipherParameters secretKey = keyGen(dfa);

        // 3. Encaps/Decaps for a satisfying assignment
        Pair pair = encaps(new CharAssignment("00111100"));
        assertEquals(true, Arrays.equals(pair.getKey(), decaps(secretKey, pair.getEncapsulation())));

        // 4. Encaps/Decaps for a non-satisfying assignment
        pair = encaps(new CharAssignment("01111100"));
        assertEquals(false, Arrays.equals(pair.getKey(), decaps(secretKey, pair.getEncapsulation())));
    }


    protected KeyEncapsulationMechanism createEngine() {
        return new RLW12KEMEngine();
    }

    @Override
    protected SecretKeyGenerator createSecretKeyGenerator() {
        return new RLW12SecretKeyGenerator();
    }

    @Override
    protected AsymmetricCipherKeyPairGenerator createkeyPairGenerator() {
        return new RLW12KeyPairGenerator();
    }

    @Override
    protected CipherParameters generateParams() {
        DefaultDFA.DefaultAlphabet alphabet = new DefaultDFA.DefaultAlphabet();
        alphabet.addLetter('0', '1');

        return new RLW12ParametersGenerator().init(parameters, alphabet).generateParameters();
    }
}

