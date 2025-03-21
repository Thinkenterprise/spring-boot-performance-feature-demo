package com.thinkenterprise.controller;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

class ThreadsControllerTest {

    private static final Logger log =  LoggerFactory.getLogger(ThreadsControllerTest.class);

    /**
     * Create Virtual Threads with the Thread Class
     */
    @Test
    void testVirtualThreadsClass() throws InterruptedException {
        var thread1 = Thread.startVirtualThread(() -> {
            log.info("{}: Hello World!", Thread.currentThread());
        });
        thread1.join();

        var thread2 = Thread.ofVirtual().unstarted(() -> {
            log.info("{}: Hello World!", Thread.currentThread());
        });
        thread2.join();

        Thread.ofVirtual().start(() -> {
            log.info("{}: Hello World!", Thread.currentThread());
        });
    }

    /**
     * Create Virtual Threads with the ExecutorService
     * - newVirtualThreadPerTaskExecutor creates a thread pool without limits
     * - virtual threads should never be pooled, create one thread for each new task
     */
    @Test
    void testVirtualThreadsExecutor() {
        try (var executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            executorService.submit(() -> {
                log.info("{}: Hello World!", Thread.currentThread());
            });
        }
    }

    /**
     * Show pinning problem with synchronized
     * - virtual thread is pinned to the carrier thread because of the synchronized block
     */
    @Test
    void testVirtualThreadsPinning() throws InterruptedException {
        var lock = new Object();
        var thread1 = Thread.startVirtualThread(() -> {
            synchronized (lock) {
                log.info("{}: Hello World!", Thread.currentThread());
                sleep(3000);
            }
        });
        var thread2 = Thread.startVirtualThread(() -> {
            synchronized (lock) {
                log.info("{}: Hello World!", Thread.currentThread());
                sleep(3000);
            }
        });
        thread1.join();
        thread2.join();
    }

    /**
     * Show solution for pinning with ReentrantLock
     * TODO warum funktioniert das nicht?
     */
    @Test
    void testVirtualThreadsReentrantLock() throws InterruptedException {
        var lock = new ReentrantLock();
        var thread1 = Thread.startVirtualThread(() -> {
            lock.lock();
            try {
                log.info("{}: Hello World!", Thread.currentThread());
                sleep(3000);
            } finally {
                lock.unlock();
            }
        });
        var thread2 = Thread.startVirtualThread(() -> {
            lock.lock();
            try {
                log.info("{}: Hello World!", Thread.currentThread());
                sleep(3000);
            } finally {
                lock.unlock();
            }
        });
        thread1.join();
        thread2.join();
    }

    /**
     * Limit Concurrency with Semaphores
     */
    @Test
    void testVirtualThreadsWithSemaphores() {
        var semaphore = new Semaphore(2);
        try (var executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 10; i++) {
                executorService.submit(() -> {
                    try {
                        semaphore.acquire();
                        log.info("{}: Hello World!", Thread.currentThread());
                        sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        semaphore.release();
                    }
                });
            }
        }
    }

    /**
     * Show usage of ScopedValues
     */
    void testVirtualThreadsWithScopedValues() {
        // TODO
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
