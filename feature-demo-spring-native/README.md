# spring-boot-performance-feature-demo
Shows the new performance features from version 3.0
# Template for our Projects 

# Motivation 

With *Spring Ahead-of-Time Support* the performance of Spring Boot applications is significantly improved.

# Features 
Features of Spring Ahead-of-Time Support are:

- Instant Startup (JIT: JVM-start, class Loading e.g. is not necessary)
- No Warmup (JIT: Optimization after first start needs time)
- Low Resource Usage (JIT: JVM, Metadata) 
- Compact Packaging (JIT: Includes unused classes and libraries ) 
- Reduced Attack Surface (JIT: Reflection are a point of attack) 

# Use Cases 
Use Cases of Spring Ahead-of-Time Support are: 

1. FaaS (Why: Startup)
2. Low Memory and CPU (Why:Low resources and memory)
3. Zero to Scale (Why: 1&2)
4. Microservices (Can!!)
5. Container & Kubernetes (Can!!)

Use Cases where Spring Ahead-of-Time Support is not suitable:
- Huge Memory and CPU (Why: AOT Features are not necessary )
- Very Frequent Deployments (Why: Compilation Time to long)
- High Traffic Websites (Why: Long-term application, path optimization)
- Big Monolithic Application – (Why: Long-term application, path optimization)

# Java
A fundamental requirement for using Spring Ahead-of-Time Support is **GraalVM**. GraalVM was released by **Oracle Labs** in 2019. It provides an Ahead-of-Time (AOT) compiler that can generate native OS binaries for Java and other languages. When using GraalVM, it is important to ensure that the bundled JDK is used, as it includes specific adaptations for GraalVM that other JDKs do not have. For example, GraalVM 21 comes with JDK 21 included.

# Spring Boot
**Spring Boot** Ahead-of-Time Support has been available since version 3.0 (2022). It is based on the Spring Ahead-of-Time (AOT) support introduced in Spring Framework 6.0, which includes:

- Build Integration
- Spring Metadata for Reflection & Proxies
- Configuration Hints for Custom Reflection & Proxies

# Picture 
Architectural images if necessary? 

# Advantages
- Faster startup 
- Constant fast response times
- Less resource consumption
- Smaller Artefacts   

# Disadvantages
- Long build times
- No runtime optimization 
- Reflection must be configured manually 

# Benchmarks 
Performance comparisons if they exist and make sense

| Metric                             | Spring Boot Native (GraalVM AOT) | Spring Boot JVM (HotSpot) | Difference                         |
|------------------------------------|----------------------------------|---------------------------|-------------------------------------|
| **Startup Time**                   | 0.22 seconds                    | 7.18 seconds             | **32x faster**                     |
| **Memory Usage (RSS)**             | 694 MB                           | 1,751 MB                  | **~60% less memory consumption**   |
| **Throughput (300 users, 20 iterations)** | 449.8 requests/sec           | 433.4 requests/sec       | **16.4 more requests per second**  |
| **Avg Response Time (300 users, 20 iterations)** | 409 ms                    | 433 ms                    | **24 ms faster**                   |
| **CPU Usage**                      | Lower                            | Higher                    | **Less CPU consumption**           |
| **Heap Usage**                     | Higher                           | Lower                     | **More heap usage in Native**      |
| **Build Time**                     | 6 minutes 11 seconds             | 28.4 seconds             | **~12x longer build time**         |
| **Application Size (Docker Image)** | 159 MB                           | 501 MB                    | **~68% smaller image**             |



# Other  
Comments you would like to make






