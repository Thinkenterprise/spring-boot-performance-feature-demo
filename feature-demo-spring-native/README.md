# spring-boot-performance-feature-demo
Shows the new performance features from version 3.0
# Template for our Projects 

# Motivation 

With *Spring Ahead-of-Time Support* the performance of Spring Boot applications is significantly improved.

# Features 
Features of Spring Ahead-of-Time Support are:

- Instant Startup – through ?
- No Warmup – through ?
- Low Resource Usage – through ?
- Compact Packaging – through ?
- Reduced Attack Surface – through ?

# Use Cases 
Use Cases of Spring Ahead-of-Time Support are: 

- FaaS
- Low Memory and CPU
- Zero to Scale
- Microservices (Assess)
- Container & Kubernetes (Assess)

Use Cases where Spring Ahead-of-Time Support is not suitable:
- Huge Memory and CPU
- Very Frequent Deployments
- High Traffic Websites – Why?
- Big Monolithic Application – Why?

# Java
A fundamental requirement for using Spring Ahead-of-Time Support is **GraalVM**. GraalVM was released by Oracle Labs in 2019. It provides an Ahead-of-Time (AOT) compiler that can generate native OS binaries for Java and other languages. When using GraalVM, it is important to ensure that the bundled JDK is used, as it includes specific adaptations for GraalVM that other JDKs do not have. For example, GraalVM 21 comes with JDK 21 included.

# Spring Boot
Spring Ahead-of-Time Support has been available since version 3.0 (2022). It is based on the Spring Ahead-of-Time (AOT) support introduced in Spring Framework 6.0, which includes:

- Build Integration
- Spring Metadata for Reflection & Proxies
- Configuration Hints for Custom Reflection & Proxies

# Picture 
Architectural images if necessary? 

# Advantages
Disadvantages die das Feature mit sich bringt 

# Disadvantages
Disadvantages of the feature

# Benchmarks 
Performance comparisons if they exist and make sense

# Other  
Comments you would like to make






