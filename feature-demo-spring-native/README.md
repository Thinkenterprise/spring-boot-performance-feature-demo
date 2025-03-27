---
marp: true
theme: msg
header: Spring Native
footer: © msg group | Spring Boot gibt Gas | 28.03.2025
paginate: true

---
<!-- _class: title -->

# Spring Boot gibt Gas

## Spring Native

![title h:720](./themes/assets/title-msg.png)

---
<!-- 
(JIT: JVM-start, class Loading e.g. is not necessary)
(JIT: Optimization after first start needs time)
(JIT: JVM, Metadata) 
(JIT: Includes unused classes and libraries ) 
(JIT: Reflection are a point of attack) 
-->

# Motivation 

1. **Instant Startup** 
2. **No Warmup** 
3. Low Resource Usage 
4. Compact Packaging 
5. Reduced Attack Surface 

---

<!-- 
(Why: Startup)
(Why:Low resources and memory)
(Why: 1&2)
(Can!!)
(Can!!)
-->


# Use Cases 
1. **Function as a Service (FaaS)**
2. **Container & Kubernetes (CaaS)** 
3. Zero to Scale 
4. Low Memory and CPU 
5. Microservices 

---

<!-- 
(Why: Compilation Time to long)
(Why: Long-term application, path optimization)
(Why: Long-term application, path optimization)

-->

# No Suitable Use Cases 

1. Very Frequent Deployments 
2. High Traffic Websites 
3. Big Monolithic Application 

---


<!--
A fundamental requirement for using Spring Ahead-of-Time Support is **GraalVM**. GraalVM was released by **Oracle Labs** in 2019. It provides an Ahead-of-Time (AOT) compiler that can generate native OS binaries for Java and other languages. When using GraalVM, it is important to ensure that the bundled JDK is used, as it includes specific adaptations for GraalVM that other JDKs do not have. For example, GraalVM 21 comes with JDK 21 included.
-->


# Java

* GraalVM Oracle Labs (2019)
* Ensure that the GraalVM bundled JDK is used 

---

<!-- 
**Spring Boot** Ahead-of-Time Support has been available since version 3.0 (2022). It is based on the Spring Ahead-of-Time (AOT) support introduced in Spring Framework 6.0, which includes:
- Build Integration
- Spring Metadata for Reflection & Proxies
- Configuration Hints for Custom Reflection & Proxies
-->

# Spring Boot
* Spring Boot Ahead-of-Time Support 3.0 (2022)
* Spring Framework 6.0 (Build Integration, Compiler Metadata, Hints)

---


# Advantages
- Faster startup 
- Constant fast response times
- Less resource consumption
- Smaller Artefacts   

---

# Disadvantages
- Long build times
- No runtime optimization 
- Reflection must be configured manually 

---

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







