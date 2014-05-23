package org.arcanum.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 * @since 2.0.0
 */
public class ExecutorServiceUtils {

    private static ExecutorService fixedThreadPool;
    private static ExecutorService cachedThreadPool;
    private static DaemonThreadFactory threadFactory = new DaemonThreadFactory();

    static {
        fixedThreadPool = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * 4,
                threadFactory
        );
        cachedThreadPool = Executors.newCachedThreadPool(
                threadFactory
        );
    }


    private ExecutorServiceUtils() {
    }


    public static ExecutorService getFixedThreadPool() {
        return fixedThreadPool;
    }

    public static ExecutorService getNewFixedThreadPool() {
        return Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * 4,
                threadFactory
        );
    }

    public static ExecutorService getNewFixedThreadPool(int numThread) {
        return Executors.newFixedThreadPool(
                numThread,
                threadFactory
        );
    }

    public static ExecutorService getCachedThreadPool() {
        return cachedThreadPool;
    }

    public static void shutdown() {
        fixedThreadPool.shutdown();
        cachedThreadPool.shutdown();
    }


    public static class DaemonThreadFactory implements ThreadFactory {

        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }

    }

    public abstract static class IndexCallable<T> implements Callable<T> {
        protected int i, j;

        public IndexCallable(int i) {
            this.i = i;
        }

        public IndexCallable(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    public abstract static class IndexRunnable implements Runnable {
        protected int i, j;

        public IndexRunnable(int i) {
            this.i = i;
        }

        public IndexRunnable(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    public abstract static class IntervalCallable<T> implements Callable<T> {
        protected int from, to;

        protected IntervalCallable(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }

}
