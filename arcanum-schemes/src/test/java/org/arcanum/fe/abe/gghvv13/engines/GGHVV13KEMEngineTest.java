package org.arcanum.fe.abe.gghvv13.engines;

import org.arcanum.common.fe.generator.SecretKeyGenerator;
import org.arcanum.common.kem.KeyEncapsulationMechanism;
import org.arcanum.fe.AbstractPairingKEMEngineTest;
import org.arcanum.fe.abe.gghvv13.generators.GGHVV13KeyPairGenerator;
import org.arcanum.fe.abe.gghvv13.generators.GGHVV13ParametersGenerator;
import org.arcanum.fe.abe.gghvv13.generators.GGHVV13SecretKeyGenerator;
import org.arcanum.program.assignment.BooleanAssignment;
import org.arcanum.program.circuit.BooleanCircuit;
import org.arcanum.program.circuit.smart.SmartBooleanCircuitLoader;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.CipherParameters;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author Angelo De Caro
 */
public class GGHVV13KEMEngineTest extends AbstractPairingKEMEngineTest<BooleanCircuit> {

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {false, "org/arcanum/params/ctl13_toy.properties"}
        };

        return Arrays.asList(data);
    }

    public GGHVV13KEMEngineTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }

    @Test
    public void testEncapsDecaps() {
        // 1. (MPK,MSK) <- Setup
        setup();

        // 2. SK <- KeyGen(MSK, circuit)
        BooleanCircuit circuit = new SmartBooleanCircuitLoader().load(
                "org/arcanum/program/circuit/bool/circuit3.txt"
        );

        CipherParameters secretKey = keyGen(circuit);

        // 3. Encaps/Decaps for a satisfying assignment
        KeyEncapsulationMechanism.Pair pair = encaps(new BooleanAssignment(true, true, false, true));
        assertEquals(true, Arrays.equals(pair.getKey(), decaps(secretKey, pair.getEncapsulation())));

        // 4. Encaps/Decaps for a non-satisfying assignment
        pair = encaps(new BooleanAssignment(true, false, false, true));
        assertEquals(false, Arrays.equals(pair.getKey(), decaps(secretKey, pair.getEncapsulation())));
    }


    @Override
    protected KeyEncapsulationMechanism createEngine() {
        return new GGHVV13KEMEngine();
    }

    @Override
    protected SecretKeyGenerator createSecretKeyGenerator() {
        return new GGHVV13SecretKeyGenerator();
    }

    @Override
    protected AsymmetricCipherKeyPairGenerator createkeyPairGenerator() {
        return new GGHVV13KeyPairGenerator();
    }

    @Override
    protected CipherParameters generateParams() {
        return new GGHVV13ParametersGenerator().init(parameters, 4).generateParameters();
    }

}