package org.arcanum.fhe.gsw14.field;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.arcanum.field.floating.ApfloatUtils;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class Test {

    public static void main(String[] args) {

//        BigInteger v1 = BigInteger.valueOf(2);
//        BigInteger v2 = v1.nextProbablePrime();
//
//        for (int i=0; i< 200; i++) {
//            System.out.println(v2.subtract(v1));
//
//            v1 = v2;
//            v2 = v1.nextProbablePrime();
//        }

        int x = 7;
        Apfloat xx = ApfloatUtils.newApfloat(x);

        BigInteger q = BigInteger.ONE;
        for (int i = 2; i <= x; i++) {
            if (!BigInteger.valueOf(i).isProbablePrime(12))
                continue;

            Apfloat ii = ApfloatUtils.newApfloat(i);

            Apfloat power = ApfloatMath.log(xx, ii).floor();
//            System.out.println("power = " + power.toRadix(10).toString(true));
            Apfloat res = ApfloatMath.pow(
                    ii,
                    power
            );

//            System.out.println("-- res = " + res.toRadix(10).toString(true));
            BigInteger resi = res.floor().toBigInteger();
//            if (resi.isProbablePrime(12)) {
            System.out.println("res = " + resi);
            q = q.multiply(resi);
//            }

        }

        BigInteger lb = ApfloatMath.exp(xx.multiply(ApfloatUtils.ITHREE).divide(ApfloatUtils.IFOUR)).floor().toBigInteger();

        System.out.println("q = " + q);
        System.out.println("lb = " + lb);
        System.out.println("q.compareTo(lb) = " + q.compareTo(lb));
    }
}
