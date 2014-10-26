package org.arcanum.fhe.ap14.bootstrap.engine;

import org.arcanum.Element;
import org.arcanum.fhe.ap14.bootstrap.generators.AP14BootstrapKeyGenerator;
import org.arcanum.fhe.ap14.bootstrap.params.AP14BootstrapKeyGenerationParameters;
import org.arcanum.fhe.ap14.bootstrap.params.AP14BootstrapKeyParameters;
import org.arcanum.fhe.ap14.field.AP14GSW14Element;
import org.arcanum.fhe.ap14.field.AP14GSW14Field;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;

public class AP14BootstrapEngineTest {

    private SecureRandom random;
    private AP14GSW14Field fheField;

    @Before
    public void setUp() throws Exception {
        random = SecureRandom.getInstance("SHA1PRNG");

        fheField = new AP14GSW14Field(random, 16, 40);
    }


    @Test
    public void testBoostrap() throws Exception {
        AP14BootstrapKeyParameters parameters = new AP14BootstrapKeyGenerator()
                .init(new AP14BootstrapKeyGenerationParameters(random, fheField))
                .generateKey();

        AP14BootstrapEngine engine = new AP14BootstrapEngine();
        engine.init(parameters);

        // Test for message 0
        AP14GSW14Element c1 = fheField.newElement(0);
        assertEquals(BigInteger.ZERO, c1.toBigInteger());
        
        // Bootstrap c1
        Element c2 = engine.processElements(c1.getViewColAt(c1.getM() - 2));
        assertEquals(BigInteger.ZERO, c2.toBigInteger());
    }

}