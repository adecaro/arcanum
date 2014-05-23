package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.field.vector.VectorElement;
import org.arcanum.util.cipher.engine.AbstractElementCipher;
import org.arcanum.util.cipher.engine.ElementCipher;
import org.arcanum.util.math.BigIntegerUtils;

import java.math.BigInteger;
import java.util.BitSet;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2ErrorTolerantOneTimePad extends AbstractElementCipher {

    protected VectorElement key;
    protected BigInteger order;
    protected BigInteger oneFourthOrder;

    @Override
    public ElementCipher init(Element key) {
        this.key = (VectorElement) key;
        this.order = this.key.getField().getTargetField().getOrder();
        this.oneFourthOrder = order.divide(BigIntegerUtils.FOUR);

        return this;
    }

    @Override
    public Element processBytes(byte[] buffer) {
        Element halfOrder = key.getField().getTargetField().newElement(order.divide(BigIntegerUtils.TWO));
        VectorElement r = key.duplicate();

        BitSet bits = BitSet.valueOf(buffer);
        for (int i = 0; i < key.getSize(); i++) {
            if (bits.get(i))
                r.getAt(i).add(halfOrder);
        }

        return r;
    }

    @Override
    public byte[] processElementsToBytes(Element... input) {
        VectorElement r = (VectorElement) input[0].duplicate().sub(key);

        BitSet bits = new BitSet(key.getSize());
        for (int i = 0; i < key.getSize(); i++) {
            bits.set(i, r.getAt(i).toBigInteger().abs().compareTo(oneFourthOrder) >= 0);
        }

        return bits.toByteArray();
    }
}
