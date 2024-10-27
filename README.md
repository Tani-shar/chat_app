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
Download the JavaFX SDK from openjfx.io.
Extract the SDK, and locate the lib folder.
Add the JavaFX library to your project:
IntelliJ IDEA: Go to File > Project Structure > Libraries and add the lib folder from the JavaFX SDK.
VS Code: Update the launch.json file in .vscode with the following:
json
Copy code
"vmArgs": "--module-path /path/to/javafx-sdk-<version>/lib --add-modules javafx.controls,javafx.fxml"
Replace /path/to/javafx-sdk-<version> with the path to your JavaFX lib folder.
3. Run the Application
Step 1: Start the Server
Open ChatServer.java in your IDE.
Run ChatServer to start listening on a specified port for client connections.
Step 2: Run the Client
Open ChatClient.java in your IDE.
Run ChatClient to launch the client interface.
Open additional instances of ChatClient to simulate multiple clients connecting to the same server.
Code Overview
ChatServer.java
The ChatServer class manages all client connections and broadcasts messages to each client.

Main Method: Starts the server, listening on port 12345 for incoming client connections.
ClientHandler Inner Class: A thread that handles communication with each client, reading incoming messages and broadcasting them to all connected clients.
ChatClient.java
The ChatClient class connects to the server, allowing the user to send and receive messages.

start() Method: Sets up the JavaFX user interface, including the chat display and input field.
connectToServer Method: Establishes a connection to the server and continuously listens for messages.
addMessage Method: Adds messages to the chat window, aligning them based on whether they were sent or received.
Usage
Type a message in the input field at the bottom of the client window and press Enter to send.
Messages you send appear aligned to the right, while messages received appear on the left.
Each client connected to the server will see messages from all other clients in real-time.
Contributing
Contributions to this project are welcome! You can improve the user interface, add more features, or optimize server-client communication. Submit a pull request or open an issue with your ideas.

License
This project is licensed under the MIT License - see the LICENSE file for details.

Screenshots
Screenshots will be added soon!


