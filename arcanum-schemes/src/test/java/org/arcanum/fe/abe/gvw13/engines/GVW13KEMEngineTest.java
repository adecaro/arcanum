package org.arcanum.fe.abe.gvw13.engines;

import org.arcanum.common.fe.generator.SecretKeyGenerator;
import org.arcanum.common.kem.AbstractKeyEncapsulationMechanism;
import org.arcanum.common.kem.KeyEncapsulationMechanism.Pair;
import org.arcanum.fe.AbstractKEMEngineTest;
import org.arcanum.fe.abe.gvw13.generators.GVW13KeyPairGenerator;
import org.arcanum.fe.abe.gvw13.generators.GVW13ParametersGenerator;
import org.arcanum.fe.abe.gvw13.generators.GVW13SecretKeyGenerator;
import org.arcanum.program.assignment.BooleanAssignment;
import org.arcanum.program.circuit.BooleanCircuit;
import org.arcanum.program.circuit.smart.SmartBooleanCircuitLoader;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.CipherParameters;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class GVW13KEMEngineTest extends AbstractKEMEngineTest<BooleanCircuit> {

    @Test
    public void testEncapsDecaps() {
        // 1. (MPK,MSK) <- Setup
        setup();

        // 2. SK <- KeyGen(MSK, circuit)
        CipherParameters secretKey = keyGen(
                new SmartBooleanCircuitLoader().load("org/arcanum/program/circuit/bool/circuit3.txt")
        );

        // 3. Encaps/Decaps for a satisfying assignment
        Pair pair = encaps(new BooleanAssignment(true, true, true, true));
        assertEquals(true, Arrays.equals(pair.getKey(), decaps(secretKey, pair.getEncapsulation())));

        // 4. Encaps/Decaps for a non-satisfying assignment
        pair = encaps(new BooleanAssignment(false, false, false, true));
        assertEquals(false, Arrays.equals(pair.getKey(), decaps(secretKey, pair.getEncapsulation())));
    }


    protected AbstractKeyEncapsulationMechanism createEngine() {
        return new GVW13KEMEngine();
    }

    protected SecretKeyGenerator createSecretKeyGenerator() {
        return new GVW13SecretKeyGenerator();
    }

    protected AsymmetricCipherKeyPairGenerator createkeyPairGenerator() {
        return new GVW13KeyPairGenerator();
    }

    protected CipherParameters generateParams() {
        return new GVW13ParametersGenerator(random, 4, 2).generateParameters();
    }

}