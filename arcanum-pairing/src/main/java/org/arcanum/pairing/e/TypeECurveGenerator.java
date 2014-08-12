package org.arcanum.pairing.e;

import org.arcanum.Field;
import org.arcanum.common.math.BigIntegerUtils;
import org.arcanum.common.parameters.Parameters;
import org.arcanum.common.parameters.ParametersGenerator;
import org.arcanum.common.parameters.PropertiesParameters;
import org.arcanum.field.curve.CurveField;
import org.arcanum.field.z.ZrField;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class TypeECurveGenerator implements ParametersGenerator {
    protected SecureRandom random;
    protected int rBits, qBits;


    public TypeECurveGenerator(SecureRandom random, int rBits, int qBits) {
        this.random = random;
        this.rBits = rBits;
        this.qBits = qBits;
    }

    public TypeECurveGenerator(int rBits, int qBits) {
        this(new SecureRandom(), rBits, qBits);
    }

    
    public Parameters generate() {
        // 3 takes 2 bits to represent
        BigInteger q;
        BigInteger r;
        BigInteger h = null;
        BigInteger n = null;

        // won't find any curves is hBits is too low
        int hBits = (qBits - 2) / 2 - rBits;
        if (hBits < 3)
            hBits = 3;

        int exp2;
        int exp1;
        int sign0, sign1;

        boolean found = false;
        do {
            r = BigInteger.ZERO;

            if (random.nextInt(Integer.MAX_VALUE) % 2 != 0) {
                exp2 = rBits - 1;
                sign1 = 1;
            } else {
                exp2 = rBits;
                sign1 = -1;
            }
            r = r.setBit(exp2);

            exp1 = (random.nextInt(Integer.MAX_VALUE) % (exp2 - 1)) + 1;

            //use q as a temp variable
            q = BigInteger.ZERO.setBit(exp1);

            if (sign1 > 0) {
                r = r.add(q);
            } else {
                r = r.subtract(q);
            }

            if (random.nextInt(Integer.MAX_VALUE) % 2 != 0) {
                sign0 = 1;
                r = r.add(BigInteger.ONE);
            } else {
                sign0 = -1;
                r = r.subtract(BigInteger.ONE);
            }
            if (!r.isProbablePrime(10))
                continue;

            for (int i = 0; i < 10; i++) {
                //use q as a temp variable
                q = BigInteger.ZERO.setBit(hBits + 1);

                h = BigIntegerUtils.getRandom(q, random);
                h = h.multiply(h).multiply(BigIntegerUtils.THREE);

                //finally q takes the value it should
                n = r.multiply(r).multiply(h);
                q = n.add(BigInteger.ONE);
                if (q.isProbablePrime(10)) {
                    found = true;
                    break;
                }
            }
        } while (!found);

        Field Fq = new ZrField(random, q);
        CurveField curveField = new CurveField(random, Fq.newZeroElement(), Fq.newOneElement(), n);

        // We may need to twist it.
        // Pick a random point P and twist the curve if P has the wrong order.
        if (!curveField.newRandomElement().mul(n).isZero())
            curveField.twist();

        PropertiesParameters params = new PropertiesParameters();
        params.put("type", "e");
        params.put("q", q.toString());
        params.put("r", r.toString());
        params.put("h", h.toString());
        params.put("exp1", String.valueOf(exp1));
        params.put("exp2", String.valueOf(exp2));
        params.put("sign0", String.valueOf(sign0));
        params.put("sign1", String.valueOf(sign1));
        params.put("a", curveField.getA().toBigInteger().toString());
        params.put("b", curveField.getB().toBigInteger().toString());

        return params;
    }

    public static void main(String[] args) {
        if (args.length < 2)
            throw new IllegalArgumentException("Too few arguments. Usage <rbits> <qbits>");

        if (args.length > 2)
            throw new IllegalArgumentException("Too many arguments. Usage <rbits> <qbits>");

        Integer rBits = Integer.parseInt(args[0]);
        Integer qBits = Integer.parseInt(args[1]);

        ParametersGenerator generator = new TypeECurveGenerator(rBits, qBits);
        Parameters curveParams = generator.generate();

        System.out.println(curveParams.toString(" "));
    }

}