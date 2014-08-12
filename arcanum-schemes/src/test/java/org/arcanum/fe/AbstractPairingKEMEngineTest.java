package org.arcanum.fe;

import org.arcanum.Parameters;
import org.arcanum.pairing.PairingFactory;
import org.arcanum.program.Program;
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
public abstract class AbstractPairingKEMEngineTest<P extends Program> extends AbstractKEMEngineTest<P> {

    static {
        PairingFactory.getInstance().setReuseInstance(false);
    }

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {false, "org/arcanum/params/a_181_603.properties"},
                {true, "org/arcanum/params/a_181_603.properties"},
        };

        return Arrays.asList(data);
    }


    protected String parametersPath;
    protected boolean usePBC;

    protected Parameters parameters;

    public AbstractPairingKEMEngineTest(boolean usePBC, String parametersPath) {
        this.usePBC = usePBC;
        this.parametersPath = parametersPath;
    }

    @Before
    public void before() throws Exception {
        assumeTrue(!usePBC || PairingFactory.getInstance().isPBCAvailable());

        PairingFactory.getInstance().setUsePBCWhenPossible(usePBC);
        parameters = PairingFactory.getInstance().loadParameters(parametersPath);

        assumeTrue(parameters != null);

        super.before();
    }

}
