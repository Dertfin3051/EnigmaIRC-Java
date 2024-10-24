package ru.dfhub.eirc;

import io.github.Dertfin3051.Color;
import io.github.Dertfin3051.Colored;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ConcurrentModificationException;

/**
 * A class that contains everything needed to work with a user,
 * including its socket, input/output paths, and message receiving loop.
 */
public class UserHandler extends Thread {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    private static final Logger logger = LogManager.getLogger(UserHandler.class);

    /**
     * Creating a new user
     * @param socket User socket
     */
    public UserHandler(Socket socket) throws IOException {
        this.socket = socket;
        logger.debug("Initializing user I/O streams");
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        logger.debug("I/O streams initialized successfully");
    }

    /**
     * Cycle of receiving and processing user messages
     */
    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                String inputMessage = in.readLine();
                logger.debug("User message received");
                Main.handleUserMessage(inputMessage); // Handle new message
                if (Main.isQuitMessage(inputMessage)) Main.disconnectUser(this);
            } catch (IOException e)
            {
                logger.error("An error occurred while retrieving user message (%s)".formatted(e.getMessage()));
            } catch (ConcurrentModificationException e) {} // Ignore this XD
        }
    }

    /**
     * Send message from server to user
     * @param message Message
     */
    public void sendOutMessage(String message) {
        out.println(message);
        logger.trace("Message sent to user");
    }
}
