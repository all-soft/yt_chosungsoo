package com.chsapps.yt_nahoonha.db;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockExecutorTemplate {
    private Lock lock;
    protected LockExecutorTemplate() {
        lock = new ReentrantLock();
    }

    protected <T> T execute(Executor<T> executor) {
        try {
            lock.lock();
            return executor.execute();
        } finally {
            lock.unlock();
        }
    }
    public interface Executor<T> {
        T execute();
    }
}
