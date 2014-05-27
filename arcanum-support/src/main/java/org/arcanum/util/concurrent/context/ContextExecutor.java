package org.arcanum.util.concurrent.context;

import org.arcanum.util.concurrent.ExecutorServiceUtils;
import org.arcanum.util.concurrent.Pool;
import org.arcanum.util.concurrent.PoolExecutor;
import org.arcanum.util.parameters.MutableParameters;

import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public class ContextExecutor extends PoolExecutor implements MutableParameters {

    private MutableParameters parameters;


    public ContextExecutor(MutableParameters parameters) {
        super(ExecutorServiceUtils.getCachedThreadPool());
        this.parameters = parameters;
    }

    public ContextExecutor(Executor executor, MutableParameters parameters) {
        super(executor);
        this.parameters = parameters;
    }


    public Pool submit(Callable callable) {
        throw new IllegalStateException("Invalid method invocation!");
    }

    public ContextExecutor submit(ContextRunnable runnable) {
        runnable.setExecutor(this);
        super.submit(runnable);

        return this;
    }

    public void putBigIntegerAt(String key, int index, BigInteger value) {
        parameters.putBigIntegerAt(key, index, value);
    }

    public void putBigInteger(String key, BigInteger value) {
        parameters.putBigInteger(key, value);
    }

    public void putBoolean(String key, boolean value) {
        parameters.putBoolean(key, value);
    }

    public void putObject(String key, Object value) {
        parameters.putObject(key, value);
    }

    public boolean containsKey(String key) {
        return parameters.containsKey(key);
    }

    public String getString(String key) {
        return parameters.getString(key);
    }

    public String getString(String key, String defaultValue) {
        return parameters.getString(key, defaultValue);
    }

    public int getInt(String key) {
        return parameters.getInt(key);
    }

    public int getInt(String key, int defaultValue) {
        return parameters.getInt(key, defaultValue);
    }

    public BigInteger getBigInteger(String key) {
        return parameters.getBigInteger(key);
    }

    public BigInteger getBigIntegerAt(String key, int index) {
        return parameters.getBigIntegerAt(key, index);
    }

    public BigInteger getBigInteger(String key, BigInteger defaultValue) {
        return parameters.getBigInteger(key, defaultValue);
    }

    public long getLong(String key) {
        return parameters.getLong(key);
    }

    public long getLong(String key, long defaultValue) {
        return parameters.getLong(key, defaultValue);
    }

    public byte[] getBytes(String key) {
        return parameters.getBytes(key);
    }

    public byte[] getBytes(String key, byte[] defaultValue) {
        return parameters.getBytes(key, defaultValue);
    }

    public String toString(String separator) {
        return parameters.toString(separator);
    }

    public Object getObject(String key) {
        return parameters.getObject(key);
    }

}
