package ru.dfhub.eirc;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * A class that contains everything needed to work with a user,
 * including its socket, input/output paths, and message receiving loop.
 */
public class UserHandler extends Thread {

    private final Socket socket;
    private final Scanner in;
    private final PrintWriter out;

    /**
     * Creating a new user
     * @param socket User socket
     */
    public UserHandler(Socket socket) throws IOException {
        this.socket = socket;
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
    }

    /**
     * Cycle of receiving and processing user messages
     */
    @Override
    public void run() {
        while (socket.isConnected()) {
            // Reading messages
        }
    }
}
