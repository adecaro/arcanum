package org.arcanum.field.z;

import org.arcanum.common.math.BigIntegerUtils;
import org.arcanum.field.base.AbstractField;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class SymmetricZrField extends AbstractField<SymmetricZrElement> {
    protected BigInteger order;
    protected BigInteger halfOrder;
    
    protected SymmetricZrElement nqr;
    protected int fixedLengthInBytes;
    protected BigInteger twoInverse;


    public SymmetricZrField(int order) {
        this(new SecureRandom(), BigInteger.valueOf(order), null);
    }

    public SymmetricZrField(BigInteger order) {
        this(new SecureRandom(), order, null);
    }

    public SymmetricZrField(SecureRandom random, BigInteger order) {
        this(random, order, null);
    }

    public SymmetricZrField(BigInteger order, BigInteger nqr) {
        this(new SecureRandom(), order, nqr);
    }

    public SymmetricZrField(SecureRandom random, BigInteger order, BigInteger nqr) {
        super(random);
        this.order = order;
        this.orderIsOdd = BigIntegerUtils.isOdd(order);

        this.fixedLengthInBytes = (order.bitLength() + 7) / 8;

        try {
            this.twoInverse = BigIntegerUtils.TWO.modInverse(order);
        } catch (Exception e) {
            this.twoInverse = null;
        }

        this.halfOrder = order.divide(BigInteger.valueOf(2));

        if (nqr != null)
            this.nqr = newElement().set(nqr);
    }


    public SymmetricZrElement newElement() {
        return new SymmetricZrElement(this);
    }

    public BigInteger getOrder() {
        return order;
    }

    public SymmetricZrElement getNqr() {
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

        return obj instanceof SymmetricZrField && ((SymmetricZrField) obj).order.equals(this.order);
    }

}
