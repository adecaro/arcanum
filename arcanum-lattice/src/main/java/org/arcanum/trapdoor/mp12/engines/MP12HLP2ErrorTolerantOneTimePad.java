package org.arcanum.trapdoor.mp12.engines;

import org.arcanum.Element;
import org.arcanum.common.cipher.engine.AbstractElementCipher;
import org.arcanum.common.cipher.engine.ElementCipher;
import org.arcanum.common.math.BigIntegerUtils;
import org.arcanum.field.vector.ListVectorElement;

import java.math.BigInteger;
import java.util.BitSet;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class MP12HLP2ErrorTolerantOneTimePad extends AbstractElementCipher {

    protected ListVectorElement key;
    protected BigInteger order;
    protected BigInteger oneFourthOrder;

    @Override
    public ElementCipher init(Element key) {
        this.key = (ListVectorElement) key;
        this.order = this.key.getField().getTargetField().getOrder();
        this.oneFourthOrder = order.divide(BigIntegerUtils.FOUR);

        return this;
    }

    @Override
    public Element processBytes(byte[] buffer) {
        Element halfOrder = key.getField().getTargetField().newElement(order.divide(BigIntegerUtils.TWO));
        ListVectorElement r = key.duplicate();

        BitSet bits = BitSet.valueOf(buffer);
        for (int i = 0; i < key.getSize(); i++) {
            if (bits.get(i))
                r.getAt(i).add(halfOrder);
        }

        return r;
    }

    @Override
    public byte[] processElementsToBytes(Element... input) {
        ListVectorElement r = (ListVectorElement) input[0].duplicate().sub(key);

        BitSet bits = new BitSet(key.getSize());
        for (int i = 0; i < key.getSize(); i++) {
            bits.set(i, r.getAt(i).toBigInteger().abs().compareTo(oneFourthOrder) >= 0);
        }

        return bits.toByteArray();
    }
}