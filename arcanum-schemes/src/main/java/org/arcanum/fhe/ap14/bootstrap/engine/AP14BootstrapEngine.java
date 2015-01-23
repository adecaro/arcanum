package org.arcanum.fhe.ap14.bootstrap.engine;

import org.arcanum.Element;
import org.arcanum.Vector;
import org.arcanum.common.cipher.engine.AbstractElementCipher;
import org.arcanum.fhe.ap14.bootstrap.params.AP14BootstrapKeyParameters;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class AP14BootstrapEngine extends AbstractElementCipher<Element, Element, AP14BootstrapKeyParameters> {

    protected AP14BootstrapKeyParameters keyParameters;

    @Override
    public AP14BootstrapEngine init(AP14BootstrapKeyParameters param) {
        this.keyParameters = param;

        return this;
    }

    @Override
    public Element processElements(Element... input) {
        Vector ct = (Vector) input[0];

        // 1. Apply Dimension/Modulo to the ciphertext

        // 2. Bit decompose the resulting ciphertext

        // 3. Bootstrap

        // 3.1. Homomorphically compute an encryption of the inner product

/*
        // 1. Switch ct to (d, q, bootSmallS)
        Element bootSmallCt = bv14DMREngine.processElements(ct);
//        System.out.printf("%s - %s\n", element.toBigInteger(), decrypt(switchedCt, getDecryptionKey(q)));


        // 2. TODO: Homomorphically compute an encryption of the inner product
//        Vector ciphertext = element.getViewColAt(element.getM() - 2);
//        for (int i = 0, base = 0; i < n; i++) {
//
//            BigInteger u = (ciphertext.isZeroAt(i)) ? BigInteger.ZERO : ciphertext.getAt(i).toBigInteger();
//
//            for (int j = 0; j < k; j++) {
//                BigInteger xj = (u.testBit(0)) ?  BigInteger.ONE : BigInteger.ZERO;
//
//                r.getAt(base + j).set(xj);
//
//                u  = u.subtract(xj).shiftRight(1);
//            }
//
//            base += k;
//        }

        Vector[] ip = new Vector[t];
        for (int i = 0; i < t; i++) {

            Element cursor = bsKeys[i][0].add(bootFields[i].newOneElement());
            for (int j = 1; j < d; j++) {
                // if c_j = 1
                cursor = bsKeys[i][j].add(cursor);
            }

            ip[i] = (Vector) cursor;
        }

        // TODO: 3. Homomorphically round
*/
        throw new IllegalStateException("Not finished yet!!!");
    }
}
