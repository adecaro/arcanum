package org.arcanum.fhe.ap14.field.dmr;

import org.arcanum.Element;
import org.arcanum.fhe.ap14.field.AP14GSW14Element;
import org.arcanum.fhe.ap14.field.AP14GSW14Field;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;

public class DimensionModulusReductionTest {

    private SecureRandom random;
    private AP14GSW14Field f1, f2;

    @Before
    public void setUp() throws Exception {
        random = SecureRandom.getInstance("SHA1PRNG");

        f1 = new AP14GSW14Field(random, 16, 32);
        f2 = new AP14GSW14Field(random, 8, 24);
    }


    @Test
    public void testName() throws Exception {
        DimensionModulusReduction reduction = new DimensionModulusReduction();
        reduction.init(f1.getsDec(), f2.getS());

        AP14GSW14Element c1 = f1.newElement(0);
        assertEquals(BigInteger.ZERO, c1.toBigInteger());
        f1.decrypt(c1.getViewColAt(c1.getM() - 2));

        Element c2 = reduction.processElements(c1.getViewColAt(c1.getM() - 2));
        assertEquals(BigInteger.ZERO, f2.decrypt(c2));


        c1 = f1.newElement(1);
        assertEquals(BigInteger.ONE, c1.toBigInteger());
        c2 = reduction.processElements(c1.getViewColAt(c1.getM() - 2));
        assertEquals(BigInteger.ONE, f2.decrypt(c2));
    }
}