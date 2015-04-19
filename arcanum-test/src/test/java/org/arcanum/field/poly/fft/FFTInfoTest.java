package org.arcanum.field.poly.fft;

import org.arcanum.field.poly.PolyElement;
import org.arcanum.field.poly.PolyField;
import org.arcanum.field.z.ZrField;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;

public class FFTInfoTest {


    @Test
    public void test() {
        SecureRandom random = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(64, random);

        ZrField zrField = new ZrField(random, p);
        PolyField<ZrField> polyField = new PolyField<>(random, zrField);

        PolyElement poly = polyField.newElement().ensureSize(10).setToRandom();
        System.out.println("poly = " + poly);
    }


}