package org.arcanum.fhe.gsw14.field;

import junit.framework.TestCase;
import org.arcanum.Element;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;

public class GSW14FieldTest extends TestCase {

    private SecureRandom random;
    private GSW14Field field;

    @Before
    public void setUp() throws Exception {
        random = SecureRandom.getInstance("SHA1PRNG");
        field = new GSW14Field(random, 4, 30);
    }

    @Test
    public void testNewElement() {
        Element a = field.newElement(0);
        Element b = field.newElement(1);

        assertEquals(a.toBigInteger(), BigInteger.ZERO);
        assertEquals(b.toBigInteger(), BigInteger.ONE);
    }

    @Test
    public void testSum() {
        Element a = field.newElement(0);
        Element b = field.newElement(1);

        assertEquals(BigInteger.ONE, a.add(b).toBigInteger());

        a = field.newElement(1);
        b = field.newElement(0);

        assertEquals(BigInteger.ONE, a.add(b).toBigInteger());

        a = field.newElement(0);
        b = field.newElement(0);

        assertEquals(BigInteger.ZERO, a.add(b).toBigInteger());
    }

    @Test
    public void testMul() {
        Element a = field.newElement(0);
        Element b = field.newElement(1);

        assertEquals(BigInteger.ZERO, a.mul(b).toBigInteger());

        a = field.newElement(0);
        b = field.newElement(0);

        assertEquals(BigInteger.ZERO, a.mul(b).toBigInteger());

        a = field.newElement(1);
        b = field.newElement(1);

        assertEquals(BigInteger.ONE, a.mul(b).toBigInteger());

        a = field.newElement(1);
        b = field.newElement(0);

        assertEquals(BigInteger.ZERO, a.mul(b).toBigInteger());
    }

}