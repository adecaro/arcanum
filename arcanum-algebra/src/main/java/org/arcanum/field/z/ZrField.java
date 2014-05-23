package org.arcanum.field.z;

import org.arcanum.field.base.AbstractField;
import org.arcanum.util.math.BigIntegerUtils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class ZrField extends AbstractField<ZrElement> {
    protected final BigInteger order;
    protected ZrElement nqr;
    protected final int fixedLengthInBytes;
    protected final BigInteger twoInverse;


    public ZrField(BigInteger order) {
        this(new SecureRandom(), order, null);
    }

    public ZrField(long order) {
        this(BigInteger.valueOf(order));
    }

    public ZrField(SecureRandom random, BigInteger order) {
        this(random, order, null);
    }

    public ZrField(BigInteger order, BigInteger nqr) {
        this(new SecureRandom(), order, nqr);
    }

    public ZrField(SecureRandom random, BigInteger order, BigInteger nqr) {
        super(random);
        this.order = order;
        this.orderIsOdd = BigIntegerUtils.isOdd(order);

        this.fixedLengthInBytes = (order.bitLength() + 7) / 8;

        BigInteger twoInverseTemp;
        try {
            twoInverseTemp = BigIntegerUtils.TWO.modInverse(order);
        } catch (Exception e) {
            twoInverseTemp = null;
        }
        this.twoInverse = twoInverseTemp;

        if (nqr != null)
            this.nqr = newElement().set(nqr);
    }


    public ZrElement<ZrField> newElement() {
        return new ZrElement<ZrField>(this);
    }

    public BigInteger getOrder() {
        return order;
    }

    public ZrElement getNqr() {
        if (nqr == null) {
            nqr = newElement();
            do {
                nqr.setToRandom();
            } while (nqr.isSqr());
        }
        
        return nqr.duplicate();
    }

    public int getLengthInBytes() {
        return fixedLengthInBytes;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj))
            return true;

        return obj instanceof ZrField && ((ZrField) obj).order.equals(this.order);
    }
}