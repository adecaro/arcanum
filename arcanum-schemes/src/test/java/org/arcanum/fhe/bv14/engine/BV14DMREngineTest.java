package org.arcanum.fhe.bv14.engine;

import org.arcanum.Element;
import org.arcanum.fhe.ap14.field.AP14GSW14Element;
import org.arcanum.fhe.ap14.field.AP14GSW14Field;
import org.arcanum.fhe.bv14.generators.BV14DMRKeyGenerator;
import org.arcanum.fhe.bv14.params.BV14DMRKeyGenerationParameters;
import org.arcanum.fhe.bv14.params.BV14DMRKeyParameters;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;

public class BV14DMREngineTest {

    private SecureRandom random;
    private AP14GSW14Field f1, f2;

    @Before
    public void setUp() throws Exception {
        random = SecureRandom.getInstance("SHA1PRNG");

        f1 = new AP14GSW14Field(random, 16, 40);
        f2 = new AP14GSW14Field(random, 8, 32, f1.getS().duplicate());
    }


    @Test
    public void testDecryption() throws Exception {
        BV14DMRKeyParameters parameters = new BV14DMRKeyGenerator()
                .init(new BV14DMRKeyGenerationParameters(random, f1.getDecryptionKey(), f2.getS()))
                .generateKey();

        BV14DMREngine engine = new BV14DMREngine();
        engine.init(parameters);

        // Test for message 0
        AP14GSW14Element c1 = f1.newElement(0);
        assertEquals(BigInteger.ZERO, c1.toBigInteger());
        f1.decrypt(c1.getViewColAt(c1.getM() - 2));
        Element c2 = engine.processElements(c1.getViewColAt(c1.getM() - 2));
        assertEquals(BigInteger.ZERO, f2.decrypt(c2));

        // Test for message 1
        c1 = f1.newElement(1);
        assertEquals(BigInteger.ONE, c1.toBigInteger());
        c2 = engine.processElements(c1.getViewColAt(c1.getM() - 2));
        assertEquals(BigInteger.ONE, f2.decrypt(c2));
    }
}