# Live Coding for virtual threads

## Java

### Create Virtual Threads with the Thread Class

```java
var thread1 = Thread.startVirtualThread(() -> {
  logger.info("{}: Hello World!", Thread.currentThread());
});
thread1.join();

var thread2 = Thread.ofVirtual().unstarted(() -> {
  logger.info("{}: Hello World!", Thread.currentThread());
});
thread2.join();

var thread = Thread.ofVirtual().started(() -> {
  logger.info("{}: Hello World!", Thread.currentThread());
});
```

### Create Virtual Threads with Executors

- newVirtualThreadPerTaskExecutor creates a thread pool without limits
- virtual threads should never be pooled, create one thread for each new task

```java
try (var executorService = Executors.newVirtualThreadPerTaskExecutor()) {
    executorService.submit(() -> {
        logger.info("{}: Hello World!", Thread.currentThread());
    });
}
```

### Pinning

When a virtual thread is pinned to a carrier, it remains attached even when blocked. Virtual threads get pinned in the following scenarios:

- When the method or block executed by the virtual thread is marked with the synchronized keyword.
- When the virtual thread runs external functions.

```java
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
```

### Limit Concurrency with Semaphores

```java
var semaphore = new Semaphore(2);
try (var executorService = Executors.newVirtualThreadPerTaskExecutor()) {
    for (var i = 0; i < 10; i++) {
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
```

### Scoped Values
- e.g. do not flood external apis

## Spring Boot

### Classic Threads

```yaml
server:
  tomcat:
    threads:
      max: 4

spring:
  threads:
    virtual:
      enabled: false
```

**1 request**

no concurrency, response in 3 seconds

```shell
hey -n 1 -c 1 -t 60 http://127.0.0.1:8080/threads/sleep/3
```

**4 parallel requests**

requests are processed in parallel, response in 3 seconds

```shell
hey -n 4 -c 4 -t 60 http://127.0.0.1:8080/threads/sleep/3
```

**20 parallel requests**

Only four requests can be processed in parallel, response in 15 seconds (5 * 3 seconds)

```shell
hey -n 20 -c 20 -t 60 http://127.0.0.1:8080/threads/sleep/3
```

### Virtual Threads

Show Spring Boot Autoconfiguration: org.springframgework.boot:spring-boot-autoconfigure:3.4.3

org.springframework.boot.autoconfigure.thread.Threading
- Enum on the flag spring.threads.virtual.enabled for decision for virtual threads or platform threads

org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration
- Creates TomcatVirtualThreadsWebServerFactoryCustomizer
- Customizer creates a VirtualThreadExecutor

```yaml
server:
  tomcat:
    threads:
      max: 4
    accept-count: 10000
    max-connections: 10000

spring:
  threads:
    virtual:
      enabled: true
```

Logging shows that the thread id is always increased, threads are not reused

**1 request**

no concurrency, response in 3 seconds

```shell
hey -n 1 -c 1 -t 60 http://127.0.0.1:8080/threads/sleep/3
```

**4 parallel requests**

requests are processed in parallel, response in 3 seconds

```shell
hey -n 4 -c 4 -t 60 http://127.0.0.1:8080/threads/sleep/3
```

**20 parallele Requests**

requests are processed in parallel, response in 3 seconds

```shell
hey -n 20 -c 20 -t 60 http://127.0.0.1:8080/threads/sleep/3
```

**200 parallele Requests**

requests are processed in parallel, response in 3 seconds

```shell
hey -n 20 -c 20 -t 60 http://127.0.0.1:8080/threads/sleep/3
```

**2000 parallele Requests**

requests are processed in parallel, response in 3 seconds

```shell
hey -n 20 -c 20 -t 60 http://127.0.0.1:8080/threads/sleep/3
```
