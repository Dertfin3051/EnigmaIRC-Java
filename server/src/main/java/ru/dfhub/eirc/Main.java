package ru.dfhub.eirc;

import io.github.Dertfin3051.Color;
import io.github.Dertfin3051.Colored;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static ServerSocket server;
    private static final ArrayList<UserHandler> users = new ArrayList<>();
    private static JSONObject config;

    private static int serverPort;

    public static void main(String[] args) {

        try {
            Config.init();
            config = Config.getConfig();
            serverPort = config.optInt("server-port", 6667);
        } catch (Exception e)
        {
            new Colored("An error occurred while initializing config (%s)".formatted(e.getMessage()), Color.RED).safePrint();
            System.exit(0);
        }

        try {
            initServer(serverPort); // In ftr, replace args to config params
        } catch (Exception e)
        {
            new Colored("An error occurred while initializing the server (%s)".formatted(e.getMessage()), Color.RED);
            System.exit(0);
        } // Busy or invalid port, internal network errors

        while (true) {
            try {
                UserHandler newUser = new UserHandler(server.accept());
                newUser.start();
                users.add(newUser);
            } catch (IOException e)
            {
                if (!e.getMessage().contains("timeout")) new Colored("An error occurred while adding a user (%s)".formatted(e.getMessage()), Color.RED).safePrint();
            }
        }
    }

    public static void handleUserMessage(String message) {
        users.forEach(user -> user.sendOutMessage(message)); // Yeah, nothing more for now :D
    }

    private static void initServer(int port) throws IOException {
        new Colored("Initializing server on port %s...".formatted(port), Color.YELLOW).safePrint();
        server = new ServerSocket(port);
        new Colored("Server initialized successfully!", Color.GREEN).safePrint();
    }
}