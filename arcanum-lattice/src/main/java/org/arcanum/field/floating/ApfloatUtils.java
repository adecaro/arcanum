package org.arcanum.field.floating;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ApfloatUtils {

    public final static int precision = 128;
    public final static int radix = 2;


    public final static Apint IZERO = new Apint(0, radix);
    public final static Apint IONE = new Apint(1, radix);
    public final static Apint ITWO = new Apint(2, radix);
    public static Apfloat SQRT_TWO_PI = ApfloatMath.sqrt(pi().multiply(ITWO));
    public static final Apint IFOUR = new Apint(4, radix);
    public static final Apint IFIVE = new Apint(5, radix);
    public static final Apint ISIX = new Apint(6, radix);
    public static final Apint IEIGHT = new Apint(8, radix);
    public static final Apint ITWELVE = new Apint(12, radix);


    public final static Apfloat ONE = new Apfloat(1, precision, radix);
    public final static Apfloat TWO = new Apfloat(2, precision, radix);
    public static Apfloat SQRT_TWO = ApfloatMath.sqrt(TWO);
    public static final Apfloat FIVE = new Apfloat(5, precision, radix);
    public static final Apfloat ZERO = new Apfloat(0, precision, radix);

    public static final Apfloat PI = pi();
    public static final Apfloat SQRT2PI = ApfloatMath.sqrt(PI.multiply(ITWO));


    public static Apfloat convert(Apfloat a) {
        return a.toRadix(radix).precision(precision);
    }

    public static Apfloat newApfloat(int n) {
        return new Apfloat(n, precision, radix);
    }

    public static Apfloat newApfloat(String n) {
        return convert(new Apfloat(n));
    }

    public static Apfloat newApfloat(double n) {
        return new Apfloat(n, precision, radix);
    }

    public static Apint newApint(int n) {
        return new Apint(n, radix);
    }


    public static Apfloat sqrt(int n) {
        return ApfloatMath.sqrt(new Apfloat(n, precision, radix));
    }

    public static Apfloat pi() {
        return ApfloatMath.pi(precision, radix);
    }

    public static Apfloat square(Apfloat value) {
        return ApfloatMath.pow(value, 2);
    }

    public static String toString(Apfloat value) {
        if (value == null)
            return "null";
        return value.toRadix(10).toString(true);
    }

}
