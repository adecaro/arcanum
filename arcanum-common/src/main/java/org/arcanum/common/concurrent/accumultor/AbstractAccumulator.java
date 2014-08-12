package org.arcanum.common.concurrent.accumultor;

import org.arcanum.common.concurrent.ExecutorServiceUtils;
import org.arcanum.common.concurrent.Pool;
import org.arcanum.common.concurrent.PoolExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public abstract class AbstractAccumulator<T> extends PoolExecutor<T> implements Accumulator<T> {


    protected T result;


    public AbstractAccumulator() {
        this(ExecutorServiceUtils.getFixedThreadPool());
    }

    public AbstractAccumulator(Executor executor) {
        super(executor);
    }


    public Accumulator<T> accumulate(Callable<T> callable) {
        submit(callable);

        return this;
    }

    public Pool<T> submit(Runnable runnable) {
        throw new IllegalStateException("Invalid call method!");
    }

    public Accumulator<T> awaitTermination() {
        try {
            for (int i = 0; i < counter; i++)
                reduce(pool.take().get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            counter = 0;
        }
        return this;
    }

    public T getResult() {
        return result;
    }

    public T awaitResult() {
        return awaitTermination().getResult();
    }


    protected abstract void reduce(T value);

}
