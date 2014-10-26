package org.arcanum.fhe.ap14.field;

import junit.framework.TestCase;
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
        field = new AP14BootstrappableGSW14Field(random, 4, 40);
    }

    @Test
    public void testNewElement() {
        AP14GSW14Element a = field.newZeroElement();
        AP14GSW14Element b = field.newOneElement();

        assertEquals(a.toBigInteger(), BigInteger.ZERO);
        assertEquals(field.bootstrap(a).toBigInteger(), BigInteger.ZERO);

        assertEquals(b.toBigInteger(), BigInteger.ONE);
        assertEquals(field.bootstrap(b).toBigInteger(), BigInteger.ONE);
    }

}