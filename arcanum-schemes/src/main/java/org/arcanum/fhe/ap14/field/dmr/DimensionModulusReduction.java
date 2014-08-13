package org.arcanum.fhe.ap14.field.dmr;

import org.apfloat.Apfloat;
import org.arcanum.*;
import org.arcanum.common.cipher.engine.AbstractElementCipher;
import org.arcanum.field.base.AbstractVectorElement.AbstractVectorReader;
import org.arcanum.field.floating.ApfloatUtils;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.field.vector.VectorField;
import org.arcanum.field.z.ZFieldSelector;
import org.arcanum.sampler.DiscreteGaussianLazyRSSampler;
import org.arcanum.sampler.GaussianSampler;
import org.arcanum.trapdoor.mp12.utils.MP12P2Utils;

import java.math.BigInteger;

import static org.arcanum.field.floating.ApfloatUtils.log2;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class DimensionModulusReduction extends AbstractElementCipher {

    protected Vector s, t;
    protected BigInteger q, p;
    protected int k, nsHat;
    protected Sampler<BigInteger> xsi;
    protected GaussianSampler<BigInteger> roundingSampler;

    protected Matrix P;

    public DimensionModulusReduction init(final Vector s, final Vector t) {
        this.xsi = MP12P2Utils.getLWENoiseSampler(null, 16);
        this.roundingSampler = new DiscreteGaussianLazyRSSampler(null, MP12P2Utils.RRP);

        this.s = s;
        this.t = t;
        System.out.println("s = " + s);
        System.out.println("t = " + t);

        this.q = s.getTargetField().getOrder();
        this.k = log2(s.getTargetField().getOrder());
        this.p = t.getTargetField().getOrder();

        int ns = s.getSize();
        int nt = t.getSize();

        this.nsHat = ns * k;

        // 1. Init P
        this.P = new MatrixField<Field>(null, t.getTargetField(), nt + 1, nsHat).newElement();

        // 3. Sample A_{s:t} \from Z_pˆ{nsHat \times nt}
        P.setRowsToRandom(0, nt);
//        System.out.println("P = " + P);

        // 4. Compute b_{s:t}
        P.getViewRowsAt(0, nt).mulTo(t, P.getViewRowAt(nt));

        final Apfloat pOverQ = ApfloatUtils.newApfloat(p).divide(ApfloatUtils.newApfloat(q));

        P.getViewRowAt(nt).add(new AbstractVectorReader<BigInteger>(nsHat) {
            public BigInteger getAt(int index) {
                int relativeIndex = index % k;
                return roundingSampler.sample(
                                pOverQ.multiply(
                                        ApfloatUtils.newApfloat(
                                                BigInteger.ONE.shiftLeft(relativeIndex).multiply(
                                                        s.getAt(index / k).toBigInteger()
                                                )
                                        )
                                ));
            }
        }).add(xsi);

        P.getViewRowAt(nt).negate();

        return this;
    }

    @Override
    public Element processElements(Element... input) {
        Vector e = (Vector) input[0];
        System.out.println("e = " + e);

        // 1. Bit Decompose e
        Vector decomp = new VectorField(null, t.getTargetField(), nsHat).newElement();

        for (int i = 0, base = 0; i < e.getSize(); i++) {

            BigInteger u = (e.isZeroAt(i)) ? BigInteger.ZERO : e.getAt(i).toBigInteger();

            for (int j = 0; j < k; j++) {
                BigInteger xj = (u.testBit(0)) ? BigInteger.ONE : BigInteger.ZERO;
                decomp.getAt(base + j).set(xj);
                u = u.subtract(xj).shiftRight(1);
            }

            base += k;
        }

        Element result = P.mul(decomp);
        System.out.println("n = " + result);

        return result;
    }

    public static void main(String[] args) {
        int k = 24, n = 4, nsHat = n * k;
        Field zr = ZFieldSelector.getInstance().getSymmetricZrFieldPowerOfTwo(null, k);

        VectorField<Field> vf = new VectorField<Field>(null, zr, n);
        Vector v = vf.newRandomElement();
        Vector t = vf.newRandomElement();
        System.out.println("v = " + v);
        System.out.println("t = " + t);

        Element r = v.mul(t);
        System.out.println("r = " + r);

        // Decompose/Power2


        Vector decomp = new VectorField(null, t.getTargetField(), nsHat).newElement();
        for (int i = 0, base = 0; i < t.getSize(); i++) {
            BigInteger u = (t.isZeroAt(i)) ? BigInteger.ZERO : t.getAt(i).toBigInteger();
            for (int j = 0; j < k; j++) {
                BigInteger xj = (u.testBit(0)) ? BigInteger.ONE : BigInteger.ZERO;
                decomp.getAt(base + j).set(xj);
                u = u.subtract(xj).shiftRight(1);
            }
            base += k;
        }
        System.out.println("decomp = " + decomp);

        Vector power2 = new VectorField(null, t.getTargetField(), nsHat).newElement();
        for (int i = 0; i < nsHat; i++) {
            int relativeIndex = i % k;

            power2.setAt(i,
                    BigInteger.ONE.shiftLeft(relativeIndex).multiply(
                            v.getAt(i / k).toBigInteger()
                    )
            );

        }
        System.out.println("power2 = " + power2);


        Element r2 = decomp.mul(power2);
        System.out.println("r2 = " + r2);


    }

}
