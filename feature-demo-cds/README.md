---
marp: true
theme: msg
header: Class Data Sharing (CDS) / AppCDS
footer: © msg group | Spring Boot gibt Gas II | 01.08.2025
paginate: true

---

<!-- _class: title -->

# Spring Boot gibt Gas II

## Class Data Sharing (CDS) / AppCDS

![title h:720](./themes/assets/title-msg.png)

---

# Motivation

Class Data Sharing (CDS) reduces the startup time and memory footprint of Java applications, leading
to improved performance. This is particularly useful for applications that need to be restarted frequently or run in
resource-constrained environments.

Use Cases:

1. Microservices architectures where quick startup times are crucial.
2. Cloud-based applications running in container environments.

# Java

This feature is available starting from Java 5 and is enabled by default (-Xshare:auto).
With Java 8 update 40 "Application Class Data Sharing" (AppCDS) was introduced as commercial feature.
It extends the CDS feature to allow application classes to be archived in the shared archive.
Since Java 10 AppCDS is available in OpenJDK.

AppCDS has to be recreated with every class change, otherwise the cached application archive will not fit to the used
JARs.

CDS commands as parameter of "java <command> -jar <jarfile>":

- `-Xshare:dump` creates a shared archive
- `-Xshare:on` enables the shared archive for use. If the shared archive is not available, the JVM will fail to start.
- `-Xshare:off` disables the shared archive
- `-Xshare:auto` enables the shared archive if possible (default)

How to use AppCDS - as parameter of "java <command> -jr <jarfile>":

1. Write the list of loaded classes to a file:

- `-XX:DumpLoadedClassList=<filename>.lst`

2. Create the shared archive:

- `-Xshare:dump -XX:SharedClassListFile=<filename>.lst -XX:SharedArchiveFile=<filename>.jsa`

3. Use the shared archive:

- `-Xshare:on -XX:SharedArchiveFile=<filename>.jsa`
  The "-Xshare:on" is just for testing purposes. If CDS doesn't work, this will raise an error message.

Since Java 13 we have a slightly simpler way to use AppCDS, since we don't have to create a list of loaded classes
anymore.
The command list changes to:

1. Create the shared archive:

- `-XX:ArchiveClassesAtExit=<filename>.jsa`
- Here we have to stop the application manually. Afterwards the shared archive is created.

2. Use the shared archive:

- `-XX:SharedArchiveFile=<filename>.jsa`
- Again, for testing we could add the "-Xshare:on" parameter.
- For logging we could add `-Xlog:class+load:file=<logfilename>.log` to see which classes are loaded.

# Spring Boot

## CDS / AppCDS
Since CDS / AppCDS is a JDK specific feature, there is no Spring Boot specific implementation.
But Spring Boot 3.3 supports it. Therefore it provides the start parameter `-Dspring.context.exit=onRefresh`,
this can be used to stop the application after the context is refreshed. This is useful for creating the shared
archive with AppCDS.

So the final commands for Spring Boot would be:

0. Build jar file with `mvn clean package` or `gradle build` and change into the target directory.

1. Extract the application JAR to a directory:

- `java -Djarmode=tools -jar <jarfile> extract --destination <destinationFolder>`

2. Change to the extracted directory:

- `cd <destinationFolder>`

3. Creating the shared archive:

- `java -XX:ArchiveClassesAtExit=<filename>.jsa -Dspring.context.exit=onRefresh -jar <jarfile>`

4. Starting the application with the shared archive:

- `java -XX:SharedArchiveFile=<filename>.jsa -jar <jarfile>`

Alternativ mit Logging:

- `java -Xlog:class+load:file=<logfile>.log -XX:SharedArchiveFile=<filename>.jsa -jar <jarfile>`

## AOT Cache

Spring Boot Ahead-of-Time (AOT) Cache is a feature that allows pre-compilation of application classes and resources to
improve startup performance. It is not directly related to CDS or AppCDS, but it can be used in conjunction with them.

0. Build jar file with `mvn clean package` or `gradle build` and change into the target directory.

1. Extract the application JAR to a directory:

- `java -Djarmode=tools -jar <jarfile> extract --destination <destinationFolder>`

2. Change to the extracted directory:

- `cd <destinationFolder>`

3. Record the AOT configuration (.aotconf) file:

-
`java -XX:AOTMode=record -XX:AOTConfiguration=<config_filename>.aotconf -Dspring.context.exit=onRefresh -jar <jarfile>`

4. Create the AOT cache from the recorded configuration:

-
`java -XX:AOTMode=create -XX:AOTConfiguration=<config_filename>.aotconf -XX:AOTCache=<cache_filename>.aot -jar <jarfile>`

5. Start the application with the AOT cache:

- `java -XX:AOTCache=<cache_filename>.aot -jar <jarfile>`

# Picture

Architectural images if necessary?

# Advantages

- Reduced startup time

# Disadvantages

- In long-running applications, the reduced startup time does not make a significant difference.
- The application has to be started with every change to get a new shared archive.

# Benchmarks

| **Startup Time** | Java         | SB-Maven-Plugin | CDS           | AOT-Cache    | 
|------------------|--------------|-----------------|---------------|--------------|
| Hello World      | 1.9 seconds  | 1.118 seconds   | 1.044 seconds | 0.74 seconds |    
| Pet-Clinic       | 10.7 seconds | 6.0 seconds     | 3.94 seconds  | no startup   
| Real-Projekt     | 13.6 seconds | 11.0 seconds    | 7.81 seconds  | Java 17      |

# Helpful Articles/Talks

- Übersichts-Artikel:
  - https://dev.java/learn/jvm/cds-appcds/
  - https://entwickler.de/java/fast-and-furious-001
- AppCDS on Spring Framework: https://docs.spring.io/spring-framework/reference/integration/cds.html
- AppCDS on Spring Boot:
  - https://docs.spring.io/spring-boot/how-to/class-data-sharing.html
  - https://docs.spring.io/spring-boot/reference/packaging/class-data-sharing.html
  - https://docs.spring.io/spring-boot/reference/packaging/container-images/dockerfiles.html#packaging.container-images.dockerfiles.cds
- SpringOne 2024 conference talk: https://www.youtube.com/watch?v=h5tL8DCOjLI

# Further Topics

https://openjdk.org/projects/leyden/






