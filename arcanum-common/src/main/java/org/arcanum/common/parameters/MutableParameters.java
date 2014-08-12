package org.arcanum.common.parameters;

import org.arcanum.Parameters;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 *
 * @since 1.0.0
 */
public interface MutableParameters extends Parameters {

    void putObject(String key, Object value);

    void putBigIntegerAt(String key, int index, BigInteger value);

    void putBigInteger(String key, BigInteger value);

    void putBoolean(String key, boolean value);

}
