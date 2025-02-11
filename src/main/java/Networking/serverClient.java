package Networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class serverClient {
    private ServerSocket server;
    private LocalDateTime date = LocalDateTime.now();
    private final int port = 8080;
    private final ArrayList<String> postMessages = new ArrayList<>();

    public void start() {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started on port: " + port);
            while (true) {
                Socket socket = server.accept();
                Runnable ClientHandler = new ClientHandler(socket, this);
                new Thread(ClientHandler).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();

        } finally {
            try {
                server.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    public void saveMessage(String message) {
        postMessages.add(message);
    }

    public String getLastPostMessage() {
        if (postMessages.isEmpty()) {
            return "No messages stored.";
        } else {
            return postMessages.get(postMessages.size() - 1);
        }
    }

    public String getHTML() {
        String htmlData = "<!doctype html>\n" + "<html>\n" + "<head>\n" + "  <title>Example Domain</title>\n" + "\n" + "  <meta charset=\"utf-8\" />\n" + "  <meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" />\n" + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\n" + "  <style type=\"text/css\">\n" + "    body {\n" + "      background-color: #f0f0f2;\n" + "      margin: 0;\n" + "      padding: 0;\n" + "      font-family: -apple-system, system-ui, BlinkMacSystemFont, \"Segoe UI\", \"Open Sans\", \"Helvetica Neue\", Helvetica, Arial, sans-serif;\n" + "\n" + "    }\n" + "    div {\n" + "      width: 600px;\n" + "      margin: 5em auto;\n" + "      padding: 2em;\n" + "      background-color: #fdfdff;\n" + "      border-radius: 0.5em;\n" + "      box-shadow: 2px 3px 7px 2px rgba(0,0,0,0.02);\n" + "    }\n" + "    a:link, a:visited {\n" + "      color: #38488f;\n" + "      text-decoration: none;\n" + "    }\n" + "    @media (max-width: 700px) {\n" + "      div {\n" + "        margin: 0 auto;\n" + "        width: auto;\n" + "      }\n" + "    }\n" + "  </style>\n" + "</head>\n" + "\n" + "<body>\n" + "<div>\n" + "  <h1>Example Domain</h1>\n" + "  <p>This domain is for use in illustrative examples in documents. You may use this\n" + "    domain in literature without prior coordination or asking for permission.</p>\n" + "  <p><a href=\"https://www.iana.org/domains/example\">More information...</a></p>\n" + "</div>\n" + "</body>\n" + "</html>";
        return htmlData;
    }

    public String getSysInfo() {
        String info = "HTTP/1.1 200 OK \n " + "Date: " + date + "\n";
        return info;

    }
}