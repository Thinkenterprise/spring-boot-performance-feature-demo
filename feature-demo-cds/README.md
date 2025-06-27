# spring-boot-performance-feature-demo

Shows the new performance features from version 3.0

# Template for our Projects

# Motivation

Class Data Sharing (CDS) or "Project Leyden" reduces the startup time and memory footprint of Java applications, leading
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

Since CDS / AppCDS is a JDK specific feature, there is no Spring Boot specific implementation.
But Spring Boot has the start parameter `-Dspring.context.exit=onRefresh`, this can be used to stop the application
after the context is refreshed. This is useful for creating the shared archive with AppCDS.

So the final commands for Spring Boot would be:

1. Creating the shared archive:

- `java -Dspring.context.exit=onRefresh -XX:ArchiveClassesAtExit=<filename>.jsa -jar <jarfile>`

2. Starting the application with the shared archive:

- `java -Xshare:on -Xlog:class+load:file=<logfile>.log -XX:SharedArchiveFile=<filename>.jsa -jar <jarfile>`

# Picture

Architectural images if necessary?

# Advantages

CDS & AppCDS:

- Reduced startup time

# Disadvantages

CDS:

- In large or long-running applications, the reduced startup time of the JVM does not make a significant difference.

AppCDS:

- The application has to be started with every change to get a new shared archive.
- Based on the application size and runtime, it is questionable whether the shared archive is worth the effort.

# Benchmarks

Time until the "Started ..." message is shown:
Plain start with "java -jar <jarfile>": 2.308 seconds

CDS:
Start with shared archive: 2.173 seconds

AppCDS (old):
Start with shared archive: 2.147 seconds

AppCDS (new):
Start with shared archive: 1.69 seconds

# Other

Comments you would like to make

# Helpful Articles

CDS & AppCDS on Java: https://entwickler.de/java/fast-and-furious-001
AppCDS on Spring: https://docs.spring.io/spring-framework/reference/integration/cds.html

# Further Topics leaning on CDS

https://openjdk.org/projects/leyden/






