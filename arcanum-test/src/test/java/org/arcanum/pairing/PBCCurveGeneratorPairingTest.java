package org.arcanum.pairing;

import org.arcanum.ParametersGenerator;
import org.arcanum.pairing.pbc.curve.*;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assume.assumeTrue;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PBCCurveGeneratorPairingTest extends CurveGeneratorPairingTest {

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {new PBCTypeACurveGenerator(181, 603)},
                {new PBCTypeA1CurveGenerator()},
                {new PBCTypeDCurveGenerator(9563)},
                {new PBCTypeECurveGenerator(160, 1024)},
                {new PBCTypeFCurveGenerator(160)},
                {new PBCTypeGCurveGenerator(35707)},
        };

        return Arrays.asList(data);
    }

    public PBCCurveGeneratorPairingTest(ParametersGenerator parametersGenerator) {
        super(false, parametersGenerator);
    }

    @Override
    public void before() throws Exception {
        assumeTrue(PairingFactory.getInstance().isPBCAvailable());
        super.before();
    }
}