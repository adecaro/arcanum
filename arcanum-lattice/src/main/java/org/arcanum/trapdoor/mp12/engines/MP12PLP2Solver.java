package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.ElementCipher;
import org.arcanum.ElementCipherParameters;
import org.arcanum.Vector;
import org.arcanum.common.cipher.engine.AbstractElementCipher;
import org.arcanum.trapdoor.mp12.params.MP12PLPublicKeyParameters;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12PLP2Solver extends AbstractElementCipher {

    protected MP12PLPublicKeyParameters parameters;
    protected int n, k;


    public ElementCipher init(ElementCipherParameters param) {
        this.parameters = (MP12PLPublicKeyParameters) param;

        this.n = parameters.getParameters().getN();
        this.k = parameters.getK();

        return this;
    }

    public Element processElements(Element... input) {
        Vector syndrome = (Vector) input[0];
        if (syndrome.getSize() != n)
            throw new IllegalArgumentException("Invalid syndrome length.");

        Vector r = parameters.getPreimageField().newElement();

        for (int i = 0, base = 0; i < n; i++) {

            BigInteger u = (syndrome.isZeroAt(i)) ? BigInteger.ZERO : syndrome.getAt(i).toBigInteger();

            for (int j = 0; j < k; j++) {
                BigInteger xj = (u.testBit(0)) ?  BigInteger.ONE : BigInteger.ZERO;
                r.getAt(base + j).set(xj);
                u  = u.subtract(xj).shiftRight(1);
            }

            base += k;
        }

        return r;
    }

    public Element processElementsTo(Element to, Element... input) {
        Vector syndrome = (Vector) input[0];
        if (syndrome.getSize() != n)
            throw new IllegalArgumentException("Invalid syndrome length.");

        Vector r = (Vector) to;

        for (int i = 0, base = 0; i < n; i++) {

            BigInteger u = (syndrome.isZeroAt(i)) ? BigInteger.ZERO : syndrome.getAt(i).toBigInteger();

            for (int j = 0; j < k; j++) {
                BigInteger xj = (u.testBit(0)) ?  BigInteger.ONE : BigInteger.ZERO;
                r.getAt(base + j).set(xj);
                u  = u.subtract(xj).shiftRight(1);
            }

            base += k;
        }

        return r;
    }

}