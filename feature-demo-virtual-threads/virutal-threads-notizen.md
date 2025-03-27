# Virtual Threads

## Motivation

- Spring Web: One-Thread per Request Ansatz
- Vorteil: einfacher synchroner Programmierstil
- aber: jeder Thread ist ein Betriebssystem-Thread
    - Resourcenintensiv in Bezug auf CPU und Speicher
    - teuer zu erstellen
    - skaliert nur begrenzt
- Little's Law: Durchsatz ist der Quotient aus Parallelität und Latenz
- Parallelität kann ich mit dem Ansatz nicht beliebig skalieren, genauso kann ich die Latenz nicht beliebig verringeern, da die Hardware nicht endlos hochskaliert
- Alternative um den Durchsatz zu erhöhen: Reaktive Programmierung. Asynchroner, nicht blockierender Programmierstil, nicht trivial
- Virtuelle Threads versprechen leichtgewichtige, effiziente Threads, die massiv skalieren

## Java

- Projekt Loom, mit dem Ziel ein leichtgewichtiges Concurrency-Konstrukt für Java einzuführen
- September '22: Preview im JDK 19 (JEP 425)
- September '23: Stable im JDK 21 (JEP 444)
- außerdem wurden Scoped Values mit dem JEP 446 eingeführt: effiziente Alternative für ThreadLocal um Kontext-Daten in Threads zu geben

## Spring Boot

- Oktober '22: Experimenteller Support
- November '23: Full support mit Release 3.2.0
- Einfach zu benutzen: Flag spring.threads.virtual.enabled
  => Tomcat und Jetty nutzen virtuelle Threads für das Request Handling

# Architecture

...

## Advantages

- sehr gut geeignet für I/O lastige Workloads
- leichtgewichtig, wenig Speicheroverhead
- Skaliert sehr gut: millionen von parallelen Threads sind möglich
- Einfaches Concurrency Model bei bekanntem One Thread per Request Ansatz
- Kompatibel mit dem bisherigen Thread API

## Disadvantages

- nicht geeignet für CPU lastige Tasks
- Overhead durch Schedular, aber zum Glück vernachlässigbar
- Performance kann durch synchronized Blöcke leiden
- erfordert JDK 21

## Code

###

Virtuelle Threads erzeugen, zwei Möglichkeiten:
- Threads API, z.B. startVirtualThread
- aber auch klassische Thread.ofPlatform
- Executors, erzeugen ein Pool ohne Limit
- Threads werden nicht wiederverwendet

## Comparison

hey -n 1 -c 1 http://127.0.0.1:8080/threads/sleep/3

Show Spring Boot Autoconfiguration: org.springframgework.boot:spring-boot-autoconfigure:3.4.3

org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration
- Creates TomcatVirtualThreadsWebServerFactoryCustomizer
- Customizer creates a VirtualThreadExecutor