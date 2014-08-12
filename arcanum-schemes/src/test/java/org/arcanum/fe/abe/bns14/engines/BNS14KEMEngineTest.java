package org.arcanum.fe.abe.bns14.engines;

import org.arcanum.Field;
import org.arcanum.common.fe.generator.SecretKeyGenerator;
import org.arcanum.common.kem.AbstractKeyEncapsulationMechanism;
import org.arcanum.common.kem.KeyEncapsulationMechanism.Pair;
import org.arcanum.fe.AbstractKEMEngineTest;
import org.arcanum.fe.abe.bns14.generators.BNS14KeyPairGenerator;
import org.arcanum.fe.abe.bns14.generators.BNS14ParametersGenerator;
import org.arcanum.fe.abe.bns14.generators.BNS14SecretKeyGenerator;
import org.arcanum.fe.abe.bns14.params.BNS14PublicKeyParameters;
import org.arcanum.program.assignment.ElementAssignment;
import org.arcanum.program.circuit.ArithmeticCircuit;
import org.arcanum.program.circuit.smart.SmartArithmeticCircuitLoader;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.CipherParameters;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BNS14KEMEngineTest extends AbstractKEMEngineTest<ArithmeticCircuit> {

    @Test
    public void testEncapsDecaps() {
        // 1. (MPK,MSK) <- Setup
        setup();
        Field Zq = ((BNS14PublicKeyParameters) keyPair.getPublic()).getLatticePk().getZq();

        // 2. SK <- KeyGen(MSK, circuit)
        ArithmeticCircuit circuit = new SmartArithmeticCircuitLoader().load(
                Zq, "org/arcanum/program/circuit/arithmetic/circuit4.txt"
        );
        CipherParameters secretKey = keyGen(circuit);

        // 3. Encaps/Decaps for a satisfying assignment
        Pair pair = encaps(new ElementAssignment(Zq, 1, 0, 1, -1));
        assertEquals(true, Arrays.equals(pair.getKey(), decaps(secretKey, pair.getEncapsulation())));

        // 4. Encaps/Decaps for a non-satisfying assignment
        pair = encaps(new ElementAssignment(Zq, 1, 1, 1, 1));
        assertEquals(false, Arrays.equals(pair.getKey(), decaps(secretKey, pair.getEncapsulation())));
    }


    protected AbstractKeyEncapsulationMechanism createEngine() {
        return new BNS14KEMEngine();
    }

    protected SecretKeyGenerator createSecretKeyGenerator() {
        return new BNS14SecretKeyGenerator();
    }

    protected AsymmetricCipherKeyPairGenerator createkeyPairGenerator() {
        return new BNS14KeyPairGenerator();
    }

    protected CipherParameters generateParams() {
        return new BNS14ParametersGenerator(random, 4, 2).generateParameters();
    }

}