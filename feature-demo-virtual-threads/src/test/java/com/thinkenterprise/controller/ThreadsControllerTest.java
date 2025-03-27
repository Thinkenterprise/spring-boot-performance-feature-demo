package com.thinkenterprise.controller;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

class ThreadsControllerTest {

    private static final Logger log =  LoggerFactory.getLogger(ThreadsControllerTest.class);

    /**
     * Create Virtual Threads with the Thread Class
     */
    @Test
    void testVirtualThreadsClass() throws InterruptedException {
        var thread = Thread.startVirtualThread(() -> {
            log.info("{}: Hello World with Thread API!", Thread.currentThread());
        });
        thread.join();
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
                log.info("{}: Hello World with executor!", Thread.currentThread());
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
                log.info("{}: Hello World synchronized block 1!", Thread.currentThread());
                sleep(3000);
                log.info("{}: finished block 1!", Thread.currentThread());
            }
        });
        var thread2 = Thread.startVirtualThread(() -> {
            synchronized (lock) {
                log.info("{}: Hello World synchronized block 2!", Thread.currentThread());
                sleep(3000);
                log.info("{}: finished block 2!", Thread.currentThread());
            }
        });
        thread1.join();
        thread2.join();
    }

    /**
     * Limit Concurrency with Semaphores
     * - limit the number of threads that can access a resource
     * - avoid flooding of an external API with hundreds of parallel requests
     */
    @Test
    void testVirtualThreadsWithSemaphores() {
        var semaphore = new Semaphore(2);
        try (var executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            for (var i = 0; i < 10; i++) {
                executorService.submit(() -> {
                    try {
                        semaphore.acquire();
                        log.info("{}: Hello World!", Thread.currentThread());
                        sleep(4000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        semaphore.release();
                    }
                });
            }
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
