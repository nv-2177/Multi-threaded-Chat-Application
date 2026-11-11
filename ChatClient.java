import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        System.out.println("Connecting to chat server at " + SERVER_ADDRESS + ":" + SERVER_PORT);

        try (
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner consoleScanner = new Scanner(System.in)
        ) {
            System.out.print("Enter your username: ");
            String username = consoleScanner.nextLine();
            out.println(username + " has joined the chat!");

            // Thread to listen for incoming messages from the server
            Thread receiverThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });
            receiverThread.setDaemon(true); // Allows JVM to exit when main thread terminates
            receiverThread.start();

            System.out.println("Connected! Type messages below (type 'exit' to quit):");
            
            // Main thread handles reading keyboard input and sending to server
            while (true) {
                String input = consoleScanner.nextLine();
                if ("exit".equalsIgnoreCase(input.trim())) {
                    out.println(username + " left the chat.");
                    break;
                }
                out.println("[" + username + "]: " + input);
            }
        } catch (IOException e) {
            System.err.println("Unable to connect to server: " + e.getMessage());
        }
    }
}