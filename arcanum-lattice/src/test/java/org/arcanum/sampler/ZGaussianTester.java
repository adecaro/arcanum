package org.arcanum.sampler;

import org.apfloat.Apfloat;
import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.field.floating.ApfloatUtils;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.field.z.SymmetricZrField;
import org.arcanum.trapdoor.mp12.utils.MP12P2Utils;

import java.math.BigInteger;
import java.security.SecureRandom;

import static org.arcanum.field.floating.ApfloatUtils.convert;
import static org.arcanum.field.floating.ApfloatUtils.newApfloat;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ZGaussianTester {

    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        int k = 16;
        int nn = 10;
        int mm = 10;
        BigInteger q = BigInteger.ONE.shiftLeft(k);

        Apfloat gaussianParameter = convert(new Apfloat("4.5"));
        GaussianSampler<BigInteger> sampler = new DiscreteGaussianLazyRSSampler(random, gaussianParameter);
        sampler.setCenter(newApfloat("0.000002456"));
        MatrixField<Field> RField = new MatrixField<Field>(random, new SymmetricZrField(q), nn, mm);

        Matrix R = RField.newElementFromSampler(sampler);

        System.out.println("R = " + R);
        System.out.println("LatticeUtils.getS1R() = " + ApfloatUtils.toString(MP12P2Utils.getS1R(gaussianParameter, nn, mm)));

    }


}
