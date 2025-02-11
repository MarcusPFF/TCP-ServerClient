package Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final serverClient serverClient;

    public ClientHandler(Socket socket, serverClient serverClient) {
        this.clientSocket = socket;
        this.serverClient = serverClient;
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            System.out.println("Connected to client: " + clientSocket.getRemoteSocketAddress());
            String requestLine;

            while ((requestLine = in.readLine()) != null) {
                if (requestLine.isEmpty()) {
                    continue;
                }

                System.out.println("Request received: " + requestLine);
                if (requestLine.equalsIgnoreCase("quit")) {
                    System.out.println("Client disconnected: " + clientSocket.getRemoteSocketAddress());
                    break;
                }

                if (requestLine.startsWith("/")) {
                    requestLine = "GET " + requestLine + " HTTP/1.1";
                }
                else if (requestLine.startsWith("GET")) {
                    handleGet(requestLine, out);

                } else if (requestLine.startsWith("POST")) {
                    handlePost(requestLine, out);
                }
                out.flush();
               /* {
                    out.println("Unsupported command.");


                */
                }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void handleGet(String requestLine, PrintWriter out) {
        String response;
        if (requestLine.contains("/hello")) {
            response = "Hello, World!\n";
        } else if (requestLine.contains("/time")) {
            String time = LocalDateTime.now().toString();
            response = time + "\n";
        } else if (requestLine.contains("/echo")) {
            String message = serverClient.getLastPostMessage();
            response = message + "\n";
        } else if (requestLine.contains("/html")) {
            String html = serverClient.getHTML();
            response = html;
        } else {
            response = "error.\n";
        }

        out.print(response);
        out.flush();
    }


    private void handlePost(String requestLine, PrintWriter out) {
        if (requestLine.startsWith("POST /echo ")) {
            String message = requestLine.substring("POST /echo ".length()).trim();
            if (!message.isEmpty()) {
                serverClient.saveMessage(message);
                out.print("Message stored: " + message + "\n");
                out.flush();
            } else {
                out.print("Invalid format\n");
                out.flush();
            }
        } else {
            out.print("error.\n");
            out.flush();
        }
    }

}
