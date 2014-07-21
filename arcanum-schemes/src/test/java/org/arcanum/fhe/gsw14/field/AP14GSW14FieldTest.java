package org.arcanum.fhe.gsw14.field;

import junit.framework.TestCase;
import org.arcanum.Element;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;

public class AP14GSW14FieldTest extends TestCase {

    private SecureRandom random;
    private AP14GSW14Field field;

    @Before
    public void setUp() throws Exception {
        random = SecureRandom.getInstance("SHA1PRNG");
        field = new AP14GSW14Field(random, 4, 30);
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

    @Test
    public void testMul2() {
        Element temp = field.newOneElement();

        int v = 1;
        for (int i = 0; i < 200; i++) {
            System.out.println("i = " + i);

            int nv = 1;//Math.random() > 0.5d ? 1 : 0;

            System.out.println("nv = " + nv);

            Element n = field.newElement(nv);
//            System.out.println("n = " + n.toBigInteger());


            v *= nv;
            System.out.println("v = " + v);

            temp = n.mul(temp);

            assertEquals(BigInteger.valueOf(v), temp.toBigInteger());
            System.out.println("temp = " + temp.toBigInteger());

            if (v == 0) {
                temp = field.newOneElement().add(temp);
                v= 1;
            }
        }
        assertEquals(BigInteger.valueOf(v), temp.toBigInteger());

    }
}