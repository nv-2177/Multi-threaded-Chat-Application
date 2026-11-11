Multi-Threaded Java Socket Chat Application

A high-concurrency, multi-threaded client-server chat application built in Java using core TCP Sockets (java.net) and Threading.This system allows multiple client instances to connect concurrently to a central server, broadcast real-time messages to all active participants, and handle incoming/outgoing message streams independently without UI/thread blocking.🏗️ Architecture & How It Works
                        ┌──────────────────┐
                        │    ChatServer    │
                        │   (Port 12345)   │
                        └────────┬─────────┘
                                 │
           ┌─────────────────────┼─────────────────────┐
           ▼                     ▼                     ▼
┌────────────────────┐┌────────────────────┐┌────────────────────┐
│   ClientHandler 1  ││   ClientHandler 2  ││   ClientHandler N  │
│  (Thread Worker)   ││  (Thread Worker)   ││  (Thread Worker)   │
└──────────┬─────────┘└──────────┬─────────┘└──────────┬─────────┘
           │                     │                     │
           │ TCP Socket          │ TCP Socket          │ TCP Socket
           ▼                     ▼                     ▼
┌────────────────────┐┌────────────────────┐┌────────────────────┐
│    ChatClient 1    ││    ChatClient 2    ││    ChatClient N    │
└────────────────────┘└────────────────────┘└────────────────────┘
1. ChatServer Architecture:
Listening Thread: Runs a continuous ServerSocket.accept() loop on port 12345.
 Thread-per-Client Pattern: Spawns a dedicated ClientHandler thread for every incoming connection request.
 Concurrent Registry: Maintains active output streams using a thread-safe set (ConcurrentHashMap.newKeySet()) to prevent race conditions during message broadcasting.
 Broadcasting Logic: Iterates through the active client set and routes incoming messages to all connected clients except the original sender.

2. ChatClient Architecture
Dual-Thread Execution:
Main Thread: Captures user input synchronously from standard console input (System.in) and pushes formatted messages down the socket stream.
Daemon Receiver Thread: Listens asynchronously to incoming server socket data (BufferedReader.readLine()) and renders incoming messages immediately without delaying user input.

📁 Repository StructureKeep both Java source files inside the same project folder:Plaintextchat-application/
├── ChatServer.java   # Server entry point & thread worker logic
├── ChatClient.java   # Client entry point & dual-thread stream handler
└── README.md         # Documentation

⚙️ PrerequisitesJava Development Kit (JDK): Version 8 or higher (java -version).Terminal / CLI: Multiple terminal windows or tabs to simulate client interactions.🚀 Quick Start Guide
Step 1: Clone or Set Up FilesCreate a workspace directory and place both ChatServer.java and ChatClient.java inside it.
Step 2: Compile Java Source CodeOpen your terminal inside the project directory and compile both files using javac:Bashjavac ChatServer.java ChatClient.java
This generates two bytecode files (ChatServer.class and ChatClient.class) in the same directory.
Step 3: Run the ServerLaunch the server instance first so it can open port 12345:Bashjava ChatServer
Output:PlaintextChat Server starting on port 12345...
Step 4: Connect Multiple ClientsOpen separate terminal windows or tabs, navigate to the same project directory, and execute the client program in each:Terminal Window 1 (User: Alice):Bashjava ChatClient
PlaintextConnecting to chat server at 127.0.0.1:12345
Enter your username: Alice
Connected! Type messages below (type 'exit' to quit):
Terminal Window 2 (User: Bob):Bashjava ChatClient
PlaintextConnecting to chat server at 127.0.0.1:12345
Enter your username: Bob
Connected! Type messages below (type 'exit' to quit):
💬 Usage ExampleTerminal / WindowUser Input / ActionOutput DisplayedServerListening...New client connected: /127.0.0.1:54321Broadcasting: 
Alice has joined the chat!Client 1 (Alice)Hello everyone!Connected! Type messages below...[Bob]: Hey Alice!
Client 2 (Bob)Hey Alice!Alice has joined the chat![Alice]: Hello everyone!To gracefully disconnect a client, 
type exit in the client's terminal session. The server will automatically detach the socket connection and broadcast the departure to remaining active clients.
🔐 Technical Highlights & Best PracticesThread Safety: Uses ConcurrentHashMap.newKeySet() to handle concurrent client addition/removal without requiring explicit locked synchronizations.
Resource Management: Implements try-with-resources blocks to ensure automatic closure of Socket, BufferedReader, and PrintWriter resources upon termination.
Daemon Threading: Configures the client message receiver thread with receiverThread.setDaemon(true) ensuring clean application teardown when the main console loop terminates.
Graceful Teardown: Handles stream terminations (null lines from socket closed by peer) safely inside finally cleanup blocks.
