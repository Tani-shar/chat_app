# JavaFX Chat Application

This is a simple chat application built with JavaFX for the client interface and Java Sockets for network communication. The application allows multiple clients to connect to a server, send and receive messages in real-time. Each message displays on opposite sides of the chat window based on whether it was sent or received.

## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Setup and Running the Application](#setup-and-running-the-application)
  - [1. Clone the Repository](#1-clone-the-repository)
  - [2. Set Up JavaFX](#2-set-up-javafx)
  - [3. Run the Application](#3-run-the-application)
- [Code Overview](#code-overview)
  - [ChatServer.java](#chatserverjava)
  - [ChatClient.java](#chatclientjava)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Features
- Real-time message broadcasting among multiple clients.
- Messages from the sender are aligned on the right, and received messages are aligned on the left.
- Simple and clean JavaFX GUI for a user-friendly experience.

## Prerequisites
To run this project, you'll need:
- **Java 8 or higher**
- **JavaFX SDK** (if not included with your JDK)
- **IDE**: Works well with IntelliJ IDEA or Visual Studio Code (with Java and JavaFX extensions).

## Project Structure

The main files in this project are:
├── ChatServer.java # Server application to manage clients ├── ChatClient.java # Client application with JavaFX GUI └── README.md # Project documentation
## Setup and Running the Application

### 1. Clone the Repository
To get started, clone the repository to your local machine:
```bash
git clone https://github.com/yourusername/JavaFX-Chat-App.git
cd JavaFX-Chat-App
```
 


