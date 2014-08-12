package org.arcanum.fhe.ap14.field;

import org.arcanum.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;

import static junit.framework.Assert.assertEquals;

public class AP14GSW14FieldTest {

    private SecureRandom random;
    private AP14GSW14Field field;
    private long start;

    @Before
    public void setUp() throws Exception {
        random = SecureRandom.getInstance("SHA1PRNG");
        field = new AP14GSW14Field(random, 16, 24);
        start = System.currentTimeMillis();
    }

    @After
    public void after() throws Exception {
        long end = System.currentTimeMillis();
        System.out.println("(end-start) = " + (end - start));
    }


    @Test
    public void testNewElement() {
        Element a = field.newZeroElement();
        Element b = field.newOneElement();

        assertEquals(a.toBigInteger(), BigInteger.ZERO);
        assertEquals(b.toBigInteger(), BigInteger.ONE);
    }

    @Test
    public void testSum() {
        Element a = field.newZeroElement();
        Element b = field.newOneElement();

        assertEquals(BigInteger.ONE, a.add(b).toBigInteger());

        a = field.newOneElement();
        b = field.newZeroElement();

        assertEquals(BigInteger.ONE, a.add(b).toBigInteger());

        a = field.newZeroElement();
        b = field.newZeroElement();

        assertEquals(BigInteger.ZERO, a.add(b).toBigInteger());

        a = field.newZeroElement();
        b = field.newElementErrorFree(1);

        assertEquals(BigInteger.ONE, a.add(b).toBigInteger());

        a = field.newZeroElement();
        b = field.newElementErrorFree(0);

        assertEquals(BigInteger.ZERO, a.add(b).toBigInteger());
    }

    @Test
    public void testMulOne() {
        Element a = field.newOneElement();
        Element b = field.newZeroElement();

        assertEquals(BigInteger.ZERO, a.mul(b).toBigInteger());
    }

        @Test
    public void testMul() {
        Element a = field.newOneElement();
        Element b = field.newZeroElement();

        assertEquals(BigInteger.ZERO, a.mul(b).toBigInteger());

        a = field.newZeroElement();
        b = field.newZeroElement();

        assertEquals(BigInteger.ZERO, a.mul(b).toBigInteger());

        a = field.newOneElement();
        b = field.newOneElement();

        assertEquals(BigInteger.ONE, a.mul(b).toBigInteger());

        a = field.newOneElement();
        b = field.newZeroElement();

        assertEquals(BigInteger.ZERO, a.mul(b).toBigInteger());

        a = field.newZeroElement();
        b = field.newOneElement();

        assertEquals(BigInteger.ZERO, a.mul(b).toBigInteger());

        a = field.newOneElement();
        b = field.newElementErrorFree(1);

        assertEquals(BigInteger.ONE, a.mul(b).toBigInteger());

        a = field.newZeroElement();
        b = field.newElementErrorFree(1);

        assertEquals(BigInteger.ZERO, a.mul(b).toBigInteger());

        a = field.newElementErrorFree(1);
        b = field.newElementErrorFree(1);

        assertEquals(BigInteger.ONE, a.mul(b).toBigInteger());

        a = field.newElementErrorFree(0);
        b = field.newElementErrorFree(0);

        assertEquals(BigInteger.ZERO, a.mul(b).toBigInteger());

        a = field.newElementErrorFree(1);
        b = field.newOneElement();

        assertEquals(BigInteger.ONE, a.mul(b).toBigInteger());

        a = field.newElementErrorFree(1);
        b = field.newZeroElement();

        assertEquals(BigInteger.ZERO, a.mul(b).toBigInteger());

        a = field.newElementErrorFree(0);
        b = field.newZeroElement();

        assertEquals(BigInteger.ZERO, a.mul(b).toBigInteger());

        a = field.newElementErrorFree(0);
        b = field.newOneElement();

        assertEquals(BigInteger.ZERO, a.mul(b).toBigInteger());
    }

}