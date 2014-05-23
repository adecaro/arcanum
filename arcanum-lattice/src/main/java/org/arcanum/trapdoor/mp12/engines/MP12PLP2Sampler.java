package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.Vector;
import org.arcanum.trapdoor.mp12.params.MP12PLP2PublicKeyParameters;
import org.arcanum.util.cipher.engine.AbstractElementCipher;
import org.arcanum.util.cipher.engine.ElementCipher;
import org.arcanum.util.cipher.params.ElementCipherParameters;

import java.math.BigInteger;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12PLP2Sampler extends AbstractElementCipher {

    protected MP12PLP2PublicKeyParameters parameters;
    protected int n, k;
    protected Queue<BigInteger> zero, one;


    public ElementCipher init(ElementCipherParameters param) {
        this.parameters = (MP12PLP2PublicKeyParameters) param;

        this.n = parameters.getParameters().getN();
        this.k = parameters.getK();
        this.zero = new ConcurrentLinkedDeque<BigInteger>();
        this.one = new ConcurrentLinkedDeque<BigInteger>();

        return this;
    }

    public Element processElements(Element... input) {
        Vector syndrome = (Vector) input[0];
        if (syndrome.getSize() != n)
            throw new IllegalArgumentException("Invalid syndrome length.");

        Vector r = parameters.getPreimageField().newElement();

        for (int i = 0, base = 0; i < n; i++) {

            BigInteger u = syndrome.getAt(i).toBigInteger();

            for (int j = 0; j < k; j++) {
                BigInteger xj = sampleZ(u);
                r.getAt(base + j).set(xj);

                u  = u.subtract(xj).shiftRight(1);
            }

            base += k;
        }

        return r;
    }

    private BigInteger sampleZ(BigInteger u) {
        boolean uLSB = u.testBit(0);

        BigInteger value;
        if (uLSB)
            value = one.poll();
        else
            value = zero.poll();

        if (value == null) {
            while (true) {
                BigInteger x = parameters.getDiscreteGaussianSampler().sample();
                boolean xLSB = x.testBit(0);
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