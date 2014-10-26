package org.arcanum.fhe.bv14.generators;

import org.apfloat.Apfloat;
import org.arcanum.Field;
import org.arcanum.Matrix;
import org.arcanum.Sampler;
import org.arcanum.Vector;
import org.arcanum.common.cipher.generators.ElementKeyGenerator;
import org.arcanum.fhe.bv14.params.BV14DMRKeyGenerationParameters;
import org.arcanum.fhe.bv14.params.BV14DMRKeyParameters;
import org.arcanum.field.base.AbstractVectorElement.AbstractVectorReader;
import org.arcanum.field.floating.ApfloatUtils;
import org.arcanum.field.vector.MatrixField;
import org.arcanum.field.vector.VectorField;
import org.arcanum.sampler.DiscreteGaussianLazyRSSampler;
import org.arcanum.sampler.GaussianSampler;
import org.arcanum.trapdoor.mp12.utils.MP12P2Utils;

import java.math.BigInteger;

import static org.arcanum.field.floating.ApfloatUtils.log2;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BV14DMRKeyGenerator implements ElementKeyGenerator<BV14DMRKeyGenerationParameters, BV14DMRKeyParameters> {

    protected Vector s, t;
    protected BigInteger q, p;
    protected int k, nsHat, nt;
    protected Sampler<BigInteger> xsi;
    protected GaussianSampler<BigInteger> roundingSampler;

    @Override
    public BV14DMRKeyGenerator init(BV14DMRKeyGenerationParameters keyGenerationParameters) {
        this.xsi = MP12P2Utils.getLWENoiseSampler(null, 16);
        this.roundingSampler = new DiscreteGaussianLazyRSSampler(null, MP12P2Utils.RRP);

        this.s = keyGenerationParameters.getS();
        this.t = keyGenerationParameters.getT();

        this.q = s.getTargetField().getOrder();
        this.k = log2(s.getTargetField().getOrder());
        this.p = t.getTargetField().getOrder();

        this.nt = t.getSize();
        this.nsHat = s.getSize() * k;

        return this;
    }

    @Override
    public BV14DMRKeyParameters generateKey() {
        // 1. Init P
        Matrix P = new MatrixField<Field>(null, t.getTargetField(), nt + 1, nsHat).newElement();

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

        return new BV14DMRKeyParameters(P, k, new VectorField(null, t.getTargetField(), nsHat));
    }
}
