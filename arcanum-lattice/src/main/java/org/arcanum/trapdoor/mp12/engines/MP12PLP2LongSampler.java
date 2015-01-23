package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.Sampler;
import org.arcanum.Vector;
import org.arcanum.common.cipher.engine.AbstractElementCipher;
import org.arcanum.common.cipher.engine.ElementCipher;
import org.arcanum.sampler.SamplerFactory;
import org.arcanum.trapdoor.mp12.params.MP12PLP2PublicKeyParameters;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import static org.arcanum.field.floating.ApfloatUtils.ITWO;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12PLP2LongSampler extends AbstractElementCipher<Element, Vector, MP12PLP2PublicKeyParameters> {

    private ThreadLocal<ZSampler> zSampler = new ThreadLocal<ZSampler>() {
        @Override
        protected ZSampler initialValue() {
            return new ZSampler();
        }
    };

    protected MP12PLP2PublicKeyParameters parameters;
    protected int n, k;

    protected Sampler<BigInteger> sampler;
    protected Queue<Long> zero, one;


    public ElementCipher init(MP12PLP2PublicKeyParameters param) {
        this.parameters = param;

        this.n = parameters.getParameters().getN();
        this.k = parameters.getK();
        this.sampler = SamplerFactory.getInstance().getDiscreteGaussianSampler(
                parameters.getParameters().getRandom(),
                parameters.getRandomizedRoundingParameter().multiply(ITWO)
        );
        this.zero = new ConcurrentLinkedDeque<Long>();
        this.one = new ConcurrentLinkedDeque<Long>();

        return this;
    }

    public Vector processElements(Element... input) {
        Vector syndrome = (Vector) input[0];
        if (syndrome.getSize() != n)
            throw new IllegalArgumentException("Invalid syndrome length.");

        ZSampler sampler = getZSampler();

        Vector r = parameters.getPreimageField().newElement();

        for (int i = 0, base = 0; i < n; i++) {
            long u = (syndrome.isZeroAt(i)) ? 0 : syndrome.getAt(i).toBigInteger().longValue();

            for (int j = 0; j < k; j++) {
                long xj = sampler.sampleZ(u);
                r.getAt(base + j).set(xj);

                u = (u - xj) >> 1;
            }

            base += k;
        }

        return r;
    }

    public Vector processElementsTo(Element to, Element... input) {
        Vector syndrome = (Vector) input[0];
        if (syndrome.getSize() != n)
            throw new IllegalArgumentException("Invalid syndrome length.");

        ZSampler sampler = getZSampler();

        Vector r = (Vector) to;

        for (int i = 0, base = 0; i < n; i++) {

            long u = (syndrome.isZeroAt(i)) ? 0 : syndrome.getAt(i).toBigInteger().longValue();

            for (int j = 0; j < k; j++) {
                long xj = sampler.sampleZ(u);
                r.getAt(base + j).set(xj);

                u = (u - xj) >> 1;
            }

            base += k;
        }

        return r;
    }


    public ZSampler getZSampler() {
        return zSampler.get();
    }

    public class ZSampler {
        protected Queue<Long> zero, one;

        public ZSampler() {
            this.zero = new LinkedList<Long>();
            this.one = new LinkedList<Long>();
        }

        private Long sampleZ(long u) {
            boolean uLSB = (u & (1)) != 0;

            Long value;
            if (uLSB)
                value = one.poll();
            else
                value = zero.poll();

            if (value == null) {
                while (true) {
                    long x = sampler.sample().longValue();

                    boolean xLSB = (x & 1) != 0;
                    if (xLSB == uLSB) {
                        value = x;
                        break;
                    } else {
                        if (xLSB)
                            one.add(x);
                        else
                            zero.add(x);
                    }
                }
            }

            return value;
        }


    }

}