package ru.dfhub.eirc;

import io.github.Dertfin3051.Color;
import io.github.Dertfin3051.Colored;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static ServerSocket server;
    private static ArrayList<UserHandler> users = new ArrayList<>();

    public static void main(String[] args) {

        try {
            initServer(6667, 30000); // In ftr, replace args to config params
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

    private static void initServer(int port, int timeout) throws IOException {
        new Colored("Initializing server on port %s...".formatted(port), Color.YELLOW).safePrint();
        server = new ServerSocket(port);
        server.setSoTimeout(timeout);
        new Colored("Server initialized successfully!", Color.GREEN).safePrint();
    }
}