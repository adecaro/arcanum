package org.arcanum;

import org.arcanum.pairing.Pairing;
import org.arcanum.pairing.PairingFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assume.assumeTrue;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
@RunWith(value = Parameterized.class)
public abstract class AbstractArcanumTest {

    static {
        PairingFactory.getInstance().setReuseInstance(false);
    }

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {false, "org/arcanum/pairing/a/a_181_603.properties"},
                {false, "org/arcanum/pairing/a1/a1_3primes.properties"},
                {false, "org/arcanum/pairing/d/d_9563.properties"},
                {false, "org/arcanum/pairing/e/e.properties"},
                {false, "org/arcanum/pairing/f/f.properties"},
                {false, "org/arcanum/pairing/g/g149.properties"},
                {true, "org/arcanum/pairing/a/a_181_603.properties"},
                {true, "org/arcanum/pairing/a1/a1_3primes.properties"},
                {true, "org/arcanum/pairing/d/d_9563.properties"},
                {true, "org/arcanum/pairing/e/e.properties"},
                {true, "org/arcanum/pairing/f/f.properties"},
                {true, "org/arcanum/pairing/g/g149.properties"}
        };

        return Arrays.asList(data);
    }


    protected String curvePath;
    protected boolean usePBC;
    protected Pairing pairing;


    public AbstractArcanumTest(boolean usePBC, String curvePath) {
        this.usePBC = usePBC;
        this.curvePath = curvePath;
    }

    @Before
    public void before() throws Exception {
        assumeTrue(!usePBC || PairingFactory.getInstance().isPBCAvailable());

        PairingFactory.getInstance().setUsePBCWhenPossible(usePBC);
        pairing = PairingFactory.getPairing(curvePath);

        assumeTrue(pairing != null);
    }

}
