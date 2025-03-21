# Virtual Threads

## Motivation
Virtual threads are lightweight threads that dramatically reduce the effort of writing, maintaining, and observing high-throughput concurrent applications.

Goals:
- Enable server applications written in the simple thread-per-request style to scale with near-optimal hardware utilization.
- Enable existing code that uses the java.lang.Thread API to adopt virtual threads with minimal change.
- Enable easy troubleshooting, debugging, and profiling of virtual threads with existing JDK tools.

Non-Goals
- It is not a goal to remove the traditional implementation of threads, or to silently migrate existing applications to use virtual threads.
- It is not a goal to change the basic concurrency model of Java.
- It is not a goal to offer a new data parallelism construct in either the Java language or the Java libraries. The Stream API remains the preferred way to process large data sets in parallel.

Threads-per-Request
- Server applications, concurrent request handled independent of each other by threads
- Little's law: relates latency, concurrency, and throughput
    - latency 50ms, throughput of 200 request in a second with 10 concurrent request => for throughout of 2000 you need 100 concurrent requests
- Number of threads is limited by OS (JDK Threads are wrappers around OS threads)
- Pooling avoids costs of creating threads, but does not increase the total number

Implications:
Virtual threads are cheap and plentiful, and thus should never be pooled
A new virtual thread should be created for every application task
Most virtual threads will thus be short-lived
Platform threads, by contrast, are heavyweight and expensive, and thus often must be pooled
they tend to be long-lived,

In summary, virtual threads preserve the reliable thread-per-request style that is harmonious with the design of the Java Platform while utilizing the available hardware optimally. Using virtual threads does not require learning new concepts, though it may require unlearning habits developed to cope with today's high cost of threads. Virtual threads will not only help application developers — they will also help framework designers provide easy-to-use APIs that are compatible with the platform's design without compromising on scalability.

They exist to provide scale (higher throughput), not speed (lower latency),  There can be many more of them than platform threads, so they enable the higher concurrency needed for higher throughput according to Little's Law.

To put it another way, virtual threads can significantly improve application throughput when

    The number of concurrent tasks is high (more than a few thousand), and
    The workload is not CPU-bound, since having many more threads than processor cores cannot improve throughput in that case.

The JDK's virtual thread scheduler is a work-stealing ForkJoinPool that operates in FIFO mode. The parallelism of the scheduler is the number of platform threads available for the purpose of scheduling virtual threads. By default it is equal to the number of available processors, but it can be tuned with the system property jdk.virtualThreadScheduler.parallelism. This ForkJoinPool is distinct from the common pool which is used, for example, in the implementation of parallel streams, and which operates in LIFO mode.



Our team has been experimenting with Virtual Threads since they were called Fibers. Since then and still with the release of Java 19, a limitation was prevalent, leading to Platform Thread pinning, effectively reducing concurrency when using synchronized. The use of synchronized code blocks is not in of itself a problem; only when those blocks contain blocking code, generally speaking I/O operations. These arrangements can be problematic as carrier Platform Threads are a limited resource and Platform Thread pinning can lead to application performance degradation when running code on Virtual Threads without careful inspection of the workload. In fact, the same blocking code in synchronized blocks can lead to performance issues even without Virtual Threads.

Spring Framework makes a lot of use of synchronized to implement locking, mostly around local data structures. Over the years, before Virtual Threads were available, we have revised synchronized blocks which might potentially interact with third-party resources, removing lock contention in highly concurrent applications. So Spring is in pretty good shape already owing to its large community and extensive feedback from existing concurrent applications. On the path to becoming the best possible citizen in a Virtual Thread scenario, we will further revisit synchronized usage in the context of I/O or other blocking code to avoid Platform Thread pinning in hot code paths so that your application can get the most out of Project Loom.

# Java
- Project Look: JDK 21: JEP 444 https://openjdk.org/jeps/444

# Spring Boot
- eingeführt mit Spring 3.2.0
  A few words about Spring Boot (Version)

# Picture
Architectural images if necessary?

# Advantages
Um Hardware komplett auszureizen: thread per request aufgeben; komplett anderer Programmierstil:
Stack traces provide no usable context, debuggers cannot step through request-handling logic, and profilers cannot associate an operation's cost with its caller.

Virtuelle Threads ermöglichen bei Thread per request zu bleiben;
The result is the same scalability as the asynchronous style, except it is achieved transparently: When code running in a virtual thread calls a blocking I/O operation in the java.* API, the runtime performs a non-blocking OS call and automatically suspends the virtual thread until it can be resumed later




# Disadvantages
Disadvantages of the feature

# Benchmarks
Performance comparisons if they exist and make sense

# Other
Comments you would like to make






