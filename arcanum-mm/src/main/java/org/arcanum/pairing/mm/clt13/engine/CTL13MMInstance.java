package org.arcanum.pairing.mm.clt13.engine;

import org.arcanum.pairing.mm.clt13.parameters.CTL13MMSystemParameters;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public interface CTL13MMInstance {

    CTL13MMSystemParameters getSystemParameters();

    BigInteger reduce(BigInteger value);

    boolean isZero(BigInteger value, int index);

    BigInteger sampleAtLevel(int index);

    BigInteger sampleAtZero();

    BigInteger encodeAt(BigInteger value, int startIndex, int endIndex);

    BigInteger encodeAt(int degree);

    BigInteger encodeZero();

    BigInteger encodeZeroAt(int index);

    BigInteger encodeOne();

    BigInteger encodeOneAt(int index);

    BigInteger extract(BigInteger value, int index);

    BigInteger reRandomize(BigInteger value, int index);

}
