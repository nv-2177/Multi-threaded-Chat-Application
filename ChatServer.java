import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {
    private static final int PORT = 12345;
    // Thread-safe set to keep track of all connected client output streams
    private static final Set<PrintWriter> clientWriters = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {
        System.out.println("Chat Server starting on port " + PORT + "...");
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // Wait for and accept incoming client connections
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getRemoteSocketAddress());
                
                // Spawn a new thread to handle this client
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    // Handles communication with a single connected client
    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
            ) {
                this.out = writer;
                clientWriters.add(out);

                String message;
                // Continuously listen for messages from this client
                while ((message = in.readLine()) != null) {
                    if ("exit".equalsIgnoreCase(message.trim())) {
                        break;
                    }
                    System.out.println("Broadcasting: " + message);
                    broadcast(message, out);
                }
            } catch (IOException e) {
                System.err.println("Client handler error: " + e.getMessage());
            } finally {
                // Clean up when client disconnects
                if (out != null) {
                    clientWriters.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    // Ignore on close
                }
                System.out.println("Client disconnected.");
            }
        }

        // Broadcasts message to all clients except the sender
        private void broadcast(String message, PrintWriter sender) {
            for (PrintWriter writer : clientWriters) {
                if (writer != sender) { // Skip sending back to the sender
                    writer.println(message);
                }
            }
        }
    }
}