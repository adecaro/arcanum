package org.arcanum.pairing.e;

import org.arcanum.Field;
import org.arcanum.Parameters;
import org.arcanum.Point;
import org.arcanum.field.curve.CurveField;
import org.arcanum.field.gt.GTFiniteField;
import org.arcanum.field.z.ZrField;
import org.arcanum.pairing.AbstractPairing;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class TypeEPairing extends AbstractPairing {
    protected int exp2;
    protected int exp1;
    protected int sign1;
    protected int sign0;

    protected BigInteger r;
    protected BigInteger q;
    protected BigInteger h;
    protected BigInteger a;
    protected BigInteger b;

    protected BigInteger phikonr;
    protected Point R;

    protected Field Fq;


    public TypeEPairing(Parameters properties) {
        this(new SecureRandom(), properties);
    }

    public TypeEPairing(SecureRandom random, Parameters properties) {
        super(random);

        initParams(properties);
        initMap();
        initFields();
    }

    protected void initParams(Parameters curveParams) {
        // validate the type
        String type = curveParams.getString("type");
        if (type == null || !"e".equalsIgnoreCase(type))
            throw new IllegalArgumentException("Type not valid. Found '" + type + "'. Expected 'e'.");

        // load params
        exp2 = curveParams.getInt("exp2");
        exp1 = curveParams.getInt("exp1");
        sign1 = curveParams.getInt("sign1");
        sign0 = curveParams.getInt("sign0");

        r = curveParams.getBigInteger("r");
        q = curveParams.getBigInteger("q");
        h = curveParams.getBigInteger("h");

        a = curveParams.getBigInteger("a");
        b = curveParams.getBigInteger("b");
    }


    protected void initFields() {
        // Init Zr
        Zr = initFp(r);

        // Init Fq
        Fq = initFp(q);

        // Init Eq
        CurveField<Field> Eq = initEq();

        // k=1, hence phikOnr = (q-1)/r
        phikonr = Fq.getOrder().subtract(BigInteger.ONE).divide(r);

        // Init G1, G2, GT
        G1 = Eq;
        G2 = G1;
        GT = initGT();

        R = (Point) Eq.getGenNoCofac().duplicate();
    }


    protected Field initFp(BigInteger order) {
        return new ZrField(random, order);
    }

    protected CurveField<Field> initEq() {
        return new CurveField<Field>(random,
                                     Fq.newElement().set(a), Fq.newElement().set(b),
                                     r, h);
    }

    protected Field initGT() {
        return new GTFiniteField(random, r, pairingMap, Fq);
    }


    protected void initMap() {
        pairingMap = new TypeETateProjectiveMillerPairingMap(this);
    }
}