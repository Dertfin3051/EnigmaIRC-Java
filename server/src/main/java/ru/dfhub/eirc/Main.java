package ru.dfhub.eirc;

import io.github.Dertfin3051.Background;
import io.github.Dertfin3051.Color;
import io.github.Dertfin3051.Colored;
import io.github.Dertfin3051.Style;
import org.json.JSONObject;
import ru.dfhub.eirc.util.ResourcesReader;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Main {

    private static ServerSocket server;
    private static final ArrayList<UserHandler> users = new ArrayList<>();
    private static JSONObject config;

    private static int serverPort;

    public static void main(String[] args) {

        // Trying to load config and change server settings
        try {
            Config.init();
            config = Config.getConfig();
            serverPort = config.optInt("server-port", 6667);
        } catch (Exception e)
        {
            new Colored("An error occurred while initializing config (%s)".formatted(e.getMessage()), Color.RED).safePrint();
            System.exit(0);
        }

        // Trying to init server/
        try {
            initServer(serverPort);
        } catch (Exception e)
        {
            new Colored("An error occurred while initializing the server (%s)".formatted(e.getMessage()), Color.RED);
            System.exit(0);
        } // Busy or invalid port, internal network errors

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            new Colored("Sending a server shutdown signal to clients...", Color.YELLOW).safePrint();
            users.forEach(user ->
                    user.sendOutMessage(new ResourcesReader("message_templates/server_shutdown.json").readString())
            );
            new Colored("Server shutdown signal sent successfull!", Color.GREEN).safePrint();
            new Colored("Shutdown server...", Color.RED).safePrint();
        }));

        // Handling new users
        while (true) {
            try {
                UserHandler newUser = new UserHandler(server.accept()); // Accepting new user and create UserHandler
                newUser.start(); // Run UserHandler
                users.add(newUser); // Add to users list
            } catch (IOException e)
            {
                new Colored("An error occurred while adding a user (%s)".formatted(e.getMessage()), Color.RED).safePrint();
            }
        }
    }

    /**
     * Handle user message (now just send it to every user)
     * @param message Message data
     * @throws ConcurrentModificationException Error sending message to disconnected user
     */
    public static void handleUserMessage(String message) {
        users.forEach(user -> user.sendOutMessage(message));
    }

    /**
     * Check if input message is leave message
     * @param message Input message
     * @return Is leave message
     */
    public static boolean isQuitMessage(String message) {
        if (message == null) return false; // Resolves closed Scanner(InputStream) null message
        JSONObject msg = new JSONObject(message);

        if (!msg.optString("type").equals("user-session")) return false;
        return msg.getJSONObject("content").getString("status").equals("leave");
    }

    /**
     * Remove user from users(receivers) list and stop user thread
     * @param userHandler UserHandler
     */
    public static void disconnectUser(UserHandler userHandler) {
        users.remove(userHandler);
        userHandler.interrupt();
    }

    private static void initServer(int port) throws IOException {
        new Colored("Initializing server on port %s...".formatted(port), Color.YELLOW).safePrint();
        server = new ServerSocket(port);
        new Colored("Server initialized successfully!", Color.GREEN).safePrint();
    }
}