package org.arcanum.field.poly;

import org.arcanum.Element;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PolyUtils {


    public static PolyElement constMul(Element a, PolyElement poly) {
        int n = poly.getSize();

        PolyElement res = poly.getField().newElement();
        res.ensureSize(n);

        for (int i = 0; i < n; i++) {
            res.getAt(i).set(a).mul(poly.getAt(i));
        }
        res.removeLeadingZeroes();

        return res;
    }

    public static void div(Element quot, Element rem, PolyElement a, PolyElement b) {
        if (b.isZero())
            throw new IllegalArgumentException("Division by zero!");

        int n = b.getDegree();
        int m = a.getDegree();

        if (n > m) {
            rem.set(a);
            quot.setToZero();

            return;
        }

        int k = m - n;

        PolyElement r = a.duplicate();
        PolyElement q = a.getField().newElement();
        q.ensureSize(k + 1);

        Element temp = a.getField().getTargetField().newElement();
        Element bn = b.getAt(n).duplicate().invert();

        while (k >= 0) {
            Element qk = q.getAt(k);
            qk.set(bn).mul(r.getAt(m));

            for (int i = 0; i <= n; i++) {
                temp.set(qk).mul(b.getAt(i));
                r.getAt(i + k).sub(temp);
            }
            k--; m--;
        }
        r.removeLeadingZeroes();
        
        quot.set(q);
        rem.set(r);
    }

    public static void reminder(Element rem, PolyElement a, PolyElement b) {
        if (b.isZero())
            throw new IllegalArgumentException("Division by zero!");

        int n = b.getDegree();
        int m = a.getDegree();

        if (n > m) {
            rem.set(a);

            return;
        }

        int k = m - n;

        PolyElement r = a.duplicate();
        PolyElement q = a.getField().newElement();
        q.ensureSize(k + 1);

        Element temp = a.getField().getTargetField().newElement();
        Element bn = b.getAt(n).duplicate().invert();

        while (k >= 0) {
            Element qk = q.getAt(k);
            qk.set(bn).mul(r.getAt(m));

            for (int i = 0; i <= n; i++) {
                temp.set(qk).mul(b.getAt(i));
                r.getAt(i + k).sub(temp);
            }
            k--; m--;
        }
        r.removeLeadingZeroes();

        rem.set(r);
    }

}
