package ru.dfhub.eirc;

import io.github.Dertfin3051.Color;
import io.github.Dertfin3051.Colored;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

/**
 * A class that contains everything needed to work with a user,
 * including its socket, input/output paths, and message receiving loop.
 */
public class UserHandler extends Thread {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    /**
     * Creating a new user
     * @param socket User socket
     */
    public UserHandler(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * Cycle of receiving and processing user messages
     */
    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                Main.handleUserMessage(in.readLine()); // Handle new message
            } catch (IOException e)
            {
                new Colored("An error occurred while retrieving user message (%s)".formatted(e.getMessage()), Color.RED);
            }
        }
    }

    public void sendOutMessage(String message) {
        out.println(message);
    }
}
