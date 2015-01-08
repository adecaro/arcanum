package org.arcanum.field.z;

import org.arcanum.common.math.BigIntegerUtils;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class SinglePrecisionModularArithmetic {


    public static  long AddMod(long a, long b, long n)
// return (a+b)%n

    {
        long res = a + b;
        if (res >= n)
            return res - n;
        else
            return res;
    }

    public static  long SubMod(long a, long b, long n)
// return (a-b)%n

    {
        long res = a - b;
        if (res < 0)
            return res + n;
        else
            return res;
    }

    public static long MulMod(long a, long b, long n) {
//        long q, res;
//
//        q = (long) ((((double) a) * ((double) b)) / ((double) n));
//        res = a * b - q * n;
//        if (res >= n)
//            res -= n;
//        else if (res < 0)
//            res += n;
//        return res;
        return BigInteger.valueOf(a).multiply(BigInteger.valueOf(b)).mod(BigInteger.valueOf(n)).longValue();
    }

    public static long PowerMod(long a, long ee, long n) {
//        long x, y;
//
//        long e;
//
//        if (ee < 0)
//            e = -(ee);
//        else
//            e = ee;
//
//        x = 1;
//        y = a;
//        while (e != 0) {
//            if ((e & 1) != 0) x = MulMod(x, y, n);
//            y = MulMod(y, y, n);
//            e = e >> 1;
//        }
//
//        if (ee < 0) x = InvMod(x, n);
//
//        return x;

        return BigInteger.valueOf(a).modPow(BigInteger.valueOf(ee), BigInteger.valueOf(n)).longValue();

    }

    public static  double PrepMulModPrecon(long b, long n, double ninv)
    {
        return ((double) b) * ninv;
    }

    public static  long MulModPrecon(long a, long b, long n, double bninv)
    {
        return MulMod2(a, b, n, bninv);
    }

    public static long MulMod2(long a, long b, long n, double bninv)
    {
        long q, res;

        q  = (long) (((double) a) * bninv);
        res = a*b - q*n;
        if (res >= n)
            res -= n;
        else if (res < 0)
            res += n;
        return res;
    }



    public static long InvMod(long a, long n) {
        long[] r; //d, s, t;

        r = XGCD(a, n);
        if (r[0] != 1)
            throw new IllegalArgumentException("InvMod: inverse undefined");

        if (r[1] < 0)
            return r[1] + n;
        else
            return r[1];
    }

    public static long SqrRoot(long x) {
        return BigIntegerUtils.sqrt(BigInteger.valueOf(x)).longValue();
    }

    public static long[] XGCD(long a, long b) {
        long d, s, t;
        long u, v, u0, v0, u1, v1, u2, v2, q, r;

        long aneg = 0, bneg = 0;

        if (a < 0) {
            if (a < -Integer.MAX_VALUE)
                throw new IllegalArgumentException("XGCD: integer overflow");
            a = -a;
            aneg = 1;
        }

        if (b < 0) {
            if (b < -Integer.MAX_VALUE)
                throw new IllegalArgumentException("XGCD: integer overflow");
            b = -b;
            bneg = 1;
        }

        u1 = 1;
        v1 = 0;
        u2 = 0;
        v2 = 1;
        u = a;
        v = b;

        while (v != 0) {
            q = u / v;
            r = u % v;
            u = v;
            v = r;
            u0 = u2;
            v0 = v2;
            u2 = u1 - q * u2;
            v2 = v1 - q * v2;
            u1 = u0;
            v1 = v0;
        }

        if (aneg != 0)
            u1 = -u1;

        if (bneg != 0)
            v1 = -v1;

        return new long[]{u, u1, v1};
    }
}
