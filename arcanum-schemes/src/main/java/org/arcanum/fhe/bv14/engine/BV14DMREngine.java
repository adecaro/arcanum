package org.arcanum.fhe.bv14.engine;

import org.arcanum.Element;
import org.arcanum.Vector;
import org.arcanum.common.cipher.engine.AbstractElementCipher;
import org.arcanum.fhe.bv14.params.BV14DMRKeyParameters;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class BV14DMREngine extends AbstractElementCipher<Element, Element, BV14DMRKeyParameters> {

    protected BV14DMRKeyParameters parameters;

    @Override
    public BV14DMREngine init(BV14DMRKeyParameters param) {
        this.parameters = param;

        return this;
    }

    @Override
    public Element processElements(Element... input) {
        Vector e = (Vector) input[0];
        System.out.println("e = " + e);

        int k = parameters.getK();

        // 1. Bit Decompose e
        Vector decomp = parameters.getDecompositionField().newElement();

        for (int i = 0, base = 0; i < e.getSize(); i++) {

            BigInteger u = (e.isZeroAt(i)) ? BigInteger.ZERO : e.getAt(i).toBigInteger();

            for (int j = 0; j < k; j++) {
                BigInteger xj = (u.testBit(0)) ? BigInteger.ONE : BigInteger.ZERO;
                decomp.getAt(base + j).set(xj);
                u = u.subtract(xj).shiftRight(1);
            }

            base += k;
        }

        Element result = parameters.getP().mul(decomp);
        System.out.println("n = " + result);

        return result;
    }

}
