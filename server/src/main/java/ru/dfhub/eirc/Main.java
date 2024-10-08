package ru.dfhub.eirc;

import io.github.Dertfin3051.Color;
import io.github.Dertfin3051.Colored;
import org.json.JSONObject;

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
        users.forEach(user -> {
            try {
                user.sendOutMessage(message);
            } catch (ConcurrentModificationException e)
            {
                users.remove(user);
            } // User left and output stream throws exception
        } );
    }

    private static void initServer(int port) throws IOException {
        new Colored("Initializing server on port %s...".formatted(port), Color.YELLOW).safePrint();
        server = new ServerSocket(port);
        new Colored("Server initialized successfully!", Color.GREEN).safePrint();
    }
}