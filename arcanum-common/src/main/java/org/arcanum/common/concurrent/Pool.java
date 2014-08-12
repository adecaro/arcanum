package org.arcanum.common.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 1.0.0
 */
public interface Pool<T> {

    Future<T> submitFuture(Callable<T> callable);

    Pool<T> submit(Callable<T> callable);

    Pool<T> submit(Runnable runnable);

    Pool<T> awaitTermination();

}
