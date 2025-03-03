# Virtual Threads

Virtual threads are lightweight threads that dramatically reduce the effort of writing, maintaining, and observing high-throughput concurrent applications.

**Goals**
- Enable server applications written in the simple thread-per-request style to scale with near-optimal hardware utilization.
- Enable existing code that uses the java.lang.Thread API to adopt virtual threads with minimal change.
- Enable easy troubleshooting, debugging, and profiling of virtual threads with existing JDK tools.

**Non-Goals**
- It is not a goal to remove the traditional implementation of threads, or to silently migrate existing applications to use virtual threads.
- It is not a goal to change the basic concurrency model of Java.
- It is not a goal to offer a new data parallelism construct in either the Java language or the Java libraries. The Stream API remains the preferred way to process large data sets in parallel.

## Motivation

- Concurrent requests are typically handled by separate threads (one thread per request). This model is intuitive, easy to implement, and simplifies debugging and profiling.
- Scalability follows Little’s Law: Throughput = Concurrency / Latency. To increase throughput, we need either more concurrency or lower latency.
- Platform threads are limited by OS resources because each thread consumes memory and CPU time. Creating OS threads is expensive, both in terms of time and memory footprint.
- Reactive programming offers higher concurrency, but it requires an asynchronous programming style, which is harder to read, debug, and profile.
- Virtual threads provide a lightweight threading model, where a large number of virtual threads are efficiently scheduled onto a small pool of OS threads by the JVM, maximizing resource utilization and scalability.

## Java
- Project Loom is an attempt by the OpenJDK community to introduce a lightweight concurrency construct to Java
- Virtual threads were first introduced in JDK 19 with JEP 425 as a preview feature (September 22)
- Virtual threads became a stable feature in JDK 21 with JEP 444 (September 2023)
- Alongside Scoped Values (JEP 446) were introduced, which provide a more efficient alternative to ThreadLocal for passing contextual data across threads.”

## Spring Boot
- Spring Boot introduced experimental support for virtual threads in October 2022
- Spring Boot has full support for virtual threads with release 3.2.0 in november 23
- Virtual threads can be enabled by the flag spring.threads.virtual.enabled
  - Tomcat and Jetty use virtual threads for request handling
  - applicationTaskExecutor bean will be a SimpleAsyncTaskExecutor configured to use virtual threads
  - taskScheduler bean will be a SimpleAsyncTaskScheduler configured to use virtual threads

## Picture
![Alt text](https://assets.digitalocean.com/articles/alligator/boo.svg "a title")

Architectural image

## Advantages

- Lightweight & Efficient – Virtual threads have minimal memory overhead, allowing millions of concurrent threads.
- Simplified Concurrency Model – Maintains the familiar thread-per-request model without requiring complex async programming.
- Improved Scalability – Decouples concurrency from the number of OS threads, leading to better hardware utilization.
- Better Debugging & Profiling – Virtual threads integrate seamlessly with existing Java tools like JFR and JStack.
- Compatibility – Works with existing java.lang.Thread APIs, requiring minimal changes in most applications.
- Ideal for I/O-bound Workloads – Virtual threads excel at handling high-throughput, I/O-heavy applications such as web servers and database clients.
- Better Context Propagation – Scoped Values replace ThreadLocal, improving memory efficiency and performance in Virtual Threads.

## Disadvantages

- Not Always a Performance Boost – CPU-intensive tasks don’t benefit much, as they still require OS threads.
- Scheduler Overhead – The JVM must efficiently map many virtual threads onto a small number of OS threads, introducing some overhead. 
- Synchronization Challenges – Lock-based synchronization (synchronized, ReentrantLock) can degrade performance when many virtual threads contend for shared resources. 
- Not a Replacement for Reactive Programming – Virtual threads improve ease of use but do not eliminate all benefits of async/reactive frameworks. 
- Requires JDK 21+ – Adoption may be slow in enterprises still using older Java versions.

## Benchmarks
Performance comparisons if they exist and make sense

## Other

| Property                       | Platform Threads (OS Threads)        | Virtual Threads (Project Loom) |
|--------------------------------|--------------------------------------|--------------------------------|
| **Thread Implementation**      | Managed by the operating system      | Managed by the JVM (userland) |
| **Pooling**                    | Threads should be pooled             | No pooling, a new thread per task |
| **Lifetime**                   | Long-lived, reused                   | Short-lived, created per task |
| **Usage**                      | Multiple tasks per thread            | One task per virtual thread |
| **Thread Cost**                | Heavyweight (more RAM, OS switching) | Lightweight (very low overhead) |
| **Blocking I/O**               | Blocks the OS thread                 | Parks the virtual thread, freeing OS thread |
| **Scalability**                | Limited by OS thread resources       | Millions of concurrent threads possible |
| **Context**                    | Thread Local                         | Scoped Values |
