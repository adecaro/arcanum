package org.arcanum.util.concurrent.accumultor;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public class BigIntegerAddAccumulator extends AbstractAccumulator<BigInteger> {


    public BigIntegerAddAccumulator() {
        this.result = BigInteger.ZERO;
    }


    protected void reduce(BigInteger value) {
        result = result.add(value);
    }

}
