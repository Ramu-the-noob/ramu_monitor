# Ramu Monitor

<img width="530" height="480" alt="Screenshot_20260308_134958" src="https://github.com/user-attachments/assets/7af5f0f2-77f1-47a6-85da-d80ea6a3420d" />

A simple lightweight, real-time system monitoring tool written in Java. This application provides essential hardware statistics including CPU load, memory usage, temperature, and network traffic. It features both a terminal-based Command Line Interface (CLI) and a Swing-based Graphical User Interface (GUI).

## Features

- **Real-Time Monitoring**: Updates system metrics every second.
- **Metrics Tracked**:
  - **CPU**: Usage percentage and Temperature.
  - **Memory**: Real-time usage percentage and Total Memory (GB).
  - **Network**: Real-time Download (Rx) and Upload (Tx) speeds in KB/s.
  - **System**: Processor Name, OS Name, and System Uptime.
- **Cross-Platform**: Runs on Linux, Windows, and macOS (requires Java 21+).

## Prerequisites

Before running this project, ensure you have the following installed:

- **Java Development Kit (JDK) 21** or higher.
- **Maven** (for building the project).

## Installation & Building

1. **Clone the repository**:
   ```bash
   git clone https://github.com/YOUR_USERNAME/ramu_monitor
   cd ramu_monitor
   ```

2. **Build the project**:
   This project uses Maven to manage dependencies and build a "fat JAR" containing all necessary libraries (OSHI, SLF4J).
   ```bash
   mvn clean package
   ```
   Once the build completes, the executable JAR file will be located in the `target/` directory (e.g., `ramu-monitor-1.0-SNAPSHOT.jar`).
   
4. Run the application:
java -jar target/ramu-monitor-1.0-SNAPSHOT.jar `note : it might me named differently from ramu-monitor-1.0-snapshot.jar chack first`

## Technologies Used

- **Java 21**: Core programming language.
- **OSHI**: Operating System and Hardware Information library for retrieving system metrics.
- **Java Swing**: For the Graphical User Interface.
- **Maven**: Dependency management and build automation.

## License

This project is open source.
