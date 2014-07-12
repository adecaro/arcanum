package org.arcanum.fhe.gsw14.field;

import junit.framework.TestCase;
import org.arcanum.Element;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;

public class AP14BootstrappableGSW14FieldTest extends TestCase {

    private SecureRandom random;
    private AP14BootstrappableGSW14Field field;

    @Before
    public void setUp() throws Exception {
        random = SecureRandom.getInstance("SHA1PRNG");
        field = new AP14BootstrappableGSW14Field(random, 4, 30);
    }

    @Test
    public void testNewElement() {
        Element a = field.newZeroElement();
        Element b = field.newOneElement();

        assertEquals(a.toBigInteger(), BigInteger.ZERO);
        assertEquals(b.toBigInteger(), BigInteger.ONE);
    }

}