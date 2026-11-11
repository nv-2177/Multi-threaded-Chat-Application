Multi-Threaded Java Socket Chat Application

A high-concurrency, multi-threaded client-server chat application built using Java Core TCP Sockets (java.net) and Multithreading.

This application allows multiple clients to connect simultaneously to a central server, exchange real-time messages, and communicate through a centralized broadcasting system. It uses independent threads for handling client connections and receiving messages to ensure smooth, non-blocking communication.

🏗️ Architecture & How It Works

The application follows a Client-Server Architecture using TCP socket communication.

text
                       ┌──────────────────┐
                       │    ChatServer    │
                       │   (Port 12345)   │
                       └────────┬─────────┘
                                │
          ┌─────────────────────┼─────────────────────┐
          ▼                     ▼                     ▼
┌───────────────────┐ ┌───────────────────┐ ┌───────────────────┐
│  ClientHandler 1  │ │  ClientHandler 2  │ │  ClientHandler N  │
│  (Thread Worker)  │ │  (Thread Worker)  │ │  (Thread Worker)  │
└─────────┬─────────┘ └─────────┬─────────┘ └─────────┬─────────┘
          │                     │                     │
          │ TCP Socket          │ TCP Socket          │ TCP Socket
          ▼                     ▼                     ▼
┌───────────────────┐ ┌───────────────────┐ ┌───────────────────┐
│    ChatClient 1   │ │    ChatClient 2   │ │    ChatClient N   │
└───────────────────┘ └───────────────────┘ └───────────────────┘

1. ChatServer Architecture
Listening Thread: Continuously listens for incoming client connections using ServerSocket.accept() on port 12345.
Thread-per-Client Pattern: Creates a dedicated ClientHandler thread for every incoming client connection.
Concurrent Client Registry: Uses ConcurrentHashMap.newKeySet() to maintain active client connections safely across multiple threads.
Message Broadcasting: Receives messages from clients and broadcasts them to all other connected clients, excluding the original sender.
Connection Management: Handles client disconnections and removes inactive clients from the active connection registry.
2. ChatClient Architecture

The client application uses a dual-thread execution model.

Main Thread: Reads user input synchronously from the console using System.in and sends messages to the server through the socket output stream.
Receiver Thread: A separate daemon thread continuously listens for incoming messages using BufferedReader.readLine() and displays them immediately.
Non-Blocking Communication: Allows users to type messages while incoming messages are received independently.
Graceful Disconnection: Supports the exit command to disconnect from the chat server safely.
📁 Project Structure

Keep both Java source files inside the same project folder.

text
chat-application/
│
├── ChatServer.java    # Server entry point and client handler logic
├── ChatClient.java    # Client entry point and message receiver
└── README.md          # Project documentation

⚙️ Prerequisites

Before running the application, ensure you have the following installed:

Java Development Kit (JDK): Version 8 or higher.
Terminal / Command Prompt: To compile and execute Java programs.
Multiple Terminal Windows: To simulate multiple chat clients.

Verify your Java installation:

bash
java -version
javac -version

🚀 Quick Start Guide
Step 1: Set Up the Project

Create a project directory named chat-application and place both Java source files inside it.

text
chat-application/
├── ChatServer.java
└── ChatClient.java

Step 2: Compile the Java Source Files

Open a terminal inside the project directory and execute:

bash
javac ChatServer.java ChatClient.java


This generates the corresponding .class bytecode files.

Step 3: Start the Chat Server

Run the server first:

bash
java ChatServer


Expected Output:

text
Chat Server starting on port 12345...


The server is now ready to accept multiple client connections.

Step 4: Connect Multiple Clients

Open separate terminal windows and execute the following command in each terminal:

bash
java ChatClient


Terminal Window 1 – Alice

text
Connecting to chat server at 127.0.0.1:12345
Enter your username: Alice

Connected! Type messages below (type 'exit' to quit):


Terminal Window 2 – Bob

text
Connecting to chat server at 127.0.0.1:12345
Enter your username: Bob

Connected! Type messages below (type 'exit' to quit):


Both clients are now connected to the same chat server and can exchange messages.

💬 Usage Example

The following example demonstrates real-time communication between two clients.

Terminal / User	Input / Action	Output
Server	Listening...	Chat Server starting on port 12345...
Alice	Connects	Alice has joined the chat!
Bob	Connects	Bob has joined the chat!
Alice	Hello everyone!	Message sent to Bob
Bob	Hey Alice!	Message sent to Alice

Alice's Terminal:

text
Connected! Type messages below (type 'exit' to quit):

Hey Alice!


Bob's Terminal:

text
Connected! Type messages below (type 'exit' to quit):

[Alice]: Hello everyone!


Server Terminal:

text
Chat Server starting on port 12345...
New client connected: /127.0.0.1:54321
New client connected: /127.0.0.1:54322
Broadcasting: Hello everyone!
Broadcasting: Hey Alice!

Disconnecting from the Chat

To gracefully disconnect a client, type:

text
exit


The client will close its connection, and the server will remove the disconnected client from its active client registry and notify the remaining participants.

🔐 Technical Highlights & Best Practices
1. Multithreading
Uses Java Thread to handle multiple client connections concurrently.
Each client is managed by an independent ClientHandler thread.
A separate receiver thread handles incoming messages on the client side.
2. Thread Safety
Uses ConcurrentHashMap.newKeySet() for thread-safe client registration and removal.
Supports concurrent client connections and message broadcasting.
3. TCP Socket Communication
Uses ServerSocket to accept incoming connections.
Uses Socket for client-server communication.
Uses BufferedReader and PrintWriter for text-based message transmission.
4. Resource Management
Uses try-with-resources to automatically close sockets and streams.
Prevents resource leaks during normal execution and unexpected disconnections.
5. Daemon Threading
Configures the client receiver thread using:
java
receiverThread.setDaemon(true);


This allows the application to terminate when the main thread finishes.

6. Graceful Teardown
Handles unexpected client disconnections.
Detects end-of-stream conditions using readLine().
Removes disconnected clients from the active registry.
Closes socket connections and associated resources safely.
🛠️ Technologies Used
Technology	Purpose
Java	Core programming language
java.net.Socket	Client-server communication
java.net.ServerSocket	Accepting client connections
Java Threads	Concurrent client handling
ConcurrentHashMap	Thread-safe client management
BufferedReader	Reading incoming messages
PrintWriter	Sending outgoing messages
TCP/IP	Reliable network communication
🎯 Learning Outcomes

By developing this project, you can gain practical experience in:

Java socket programming.
Client-server architecture.
Multithreading and concurrency.
TCP/IP communication.
Thread-safe data structures.
Real-time message broadcasting.
Exception handling and resource management.
Concurrent connection management.
🔮 Future Enhancements

Possible improvements for future versions include:

Private messaging between users.
Group chat rooms.
Online user list.
Message timestamps.
Graphical User Interface (GUI) using Java Swing or JavaFX.
User authentication and registration.
File sharing between clients.
Message history and database integration.
Improved scalability using thread pools.
👨‍💻 Author

Developed as a Java networking and multithreading project to demonstrate real-time client-server communication using core Java technologies.

If you find this project useful, consider giving the repository a ⭐ on GitHub!
