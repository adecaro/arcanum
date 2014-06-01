package org.arcanum.field.floating;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ApfloatContext {

    private static Map<Integer, ApfloatContext> contextMap;
    static {
        contextMap = new HashMap<Integer, ApfloatContext>();
    }


    public synchronized static ApfloatContext getContext(int precision, int radix) {
        int index = 31 * precision + radix;
        ApfloatContext context = contextMap.get(index);
        if (context == null) {
            context = new ApfloatContext(precision, radix);
            contextMap.put(index, context);
        }
        return context;
    }


    public int precision;
    public int radix;


    public ApfloatContext(int precision, int radix) {
        this.precision = precision;
        this.radix = radix;
    }



    public  Apfloat newApfloat(int n) {
        return new Apfloat(n, precision, radix);
    }

    public  Apfloat newApfloat(String n) {
        return convert(new Apfloat(n));
    }

    public  Apfloat newApfloat(double n) {
        return new Apfloat(n, precision, radix);
    }

    public  Apint newApint(int n) {
        return new Apint(n, radix);
    }


    public  Apfloat pi() {
        return ApfloatMath.pi(precision, radix);
    }


    public  Apfloat convert(Apfloat a) {
        return a.toRadix(radix).precision(precision);
    }


    public  Apfloat sqrt(int n) {
        return ApfloatMath.sqrt(new Apfloat(n, precision, radix));
    }

    public  Apfloat square(Apfloat value) {
        return ApfloatMath.pow(value, 2);
    }


    public  String toString(Apfloat value) {
        if (value == null)
            return "null";
        return value.toRadix(10).toString(true);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApfloatContext)) return false;

        ApfloatContext that = (ApfloatContext) o;

        if (precision != that.precision) return false;
        if (radix != that.radix) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = precision;
        result = 31 * result + radix;
        return result;
    }
}
