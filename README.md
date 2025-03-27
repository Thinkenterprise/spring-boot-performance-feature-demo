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
<!-- _class: agenda -->

<!--
Class Data Sharing (CDS) ist ein Feature der JVM (ab Java 9), das das schnellere Starten von Java-Anwendungen ermöglicht, indem es vorverarbeitete Klassendaten in einem gemeinsamen Archiv speichert. Dieses Archiv kann beim nächsten Start wiederverwendet werden.

CRaC ist ein OpenJDK-Projekt, das es ermöglicht, den Zustand einer laufenden Java-Anwendung einzufrieren (Checkpoint) und später genau an diesem Punkt wiederherzustellen (Restore). Ziel ist es, Java-Anwendungen nahezu sofort startbereit zu machen – mit allen initialisierten Komponenten, warmem Cache, Datenbankverbindungen etc.

-->



# Agenda

1. **Spring AOT** (Ahead-of-Time Compilation) (Spring Boot 3.0, 2022)
1. **CRaC** (Coordinated Restore at Checkpoint) (Spring Boot 3.1, 2023)
3. **CDS** (Class Data Sharing) (Spring Boot 3.2, 2023)
4. **Virtual Threads** (Spring Boot 3.2, 2023)