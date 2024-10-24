package ru.dfhub.eirc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import ru.dfhub.eirc.util.ResourcesReader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Main {

    private static ServerSocket server;
    private static final ArrayList<UserHandler> users = new ArrayList<>();
    private static JSONObject config;

    private static int serverPort;

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        // Trying to load config and change server settings
        try {
            Config.init();
            config = Config.getConfig();
            serverPort = config.optInt("server-port", 6667);
        } catch (Exception e)
        {
            logger.error("An error occurred while initializing config (%s)".formatted(e.getMessage()));
            System.exit(0);
        }

        // Trying to init server/
        try {
            initServer(serverPort);
        } catch (Exception e)
        {
            logger.error("An error occurred while initializing the server (%s)".formatted(e.getMessage()));
            System.exit(0);
        } // Busy or invalid port, internal network errors

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Sending a server shutdown signal to clients...");
            users.forEach(user ->
                    user.sendOutMessage(new ResourcesReader("message_templates/server_shutdown.json").readString())
            );
            logger.info("Server shutdown signal sent successfully!");
            logger.info("Shutdown server...");
        }));

        // Handling new users
        while (true) {
            try {
                Socket user = server.accept();
                logger.debug("New user accepted. Trying to init UserHandler.");
                UserHandler userHandler = new UserHandler(user); // Accepting new user and create UserHandler
                logger.debug("UserHandler initialized successfully. Start processing thread...");
                userHandler.start(); // Run UserHandler
                users.add(userHandler); // Add to users list
                logger.debug("Thread started. User added to user-list");
                logger.info("New user handled successfully!");
            } catch (IOException e)
            {
                logger.error("An error occurred while adding a user (%s)".formatted(e.getMessage()));
            }
        }
    }

    /**
     * Handle user message (now just send it to every user)
     * @param message Message data
     * @throws ConcurrentModificationException Error sending message to disconnected user
     */
    public static void handleUserMessage(String message) {
        logger.debug("Trying to handle user message.");
        users.forEach(user -> user.sendOutMessage(message));
        logger.info("User message handled successfully.");
    }

    /**
     * Check if input message is leave message
     * @param message Input message
     * @return Is leave message
     */
    public static boolean isQuitMessage(String message) {
        logger.debug("Checking for user-leave message...");
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
        logger.info("Received user-leave message, disconnecting user...");
        users.remove(userHandler);
        userHandler.interrupt();
        logger.info("User disconnected successfully.");
    }

    private static void initServer(int port) throws IOException {
        logger.info("Initializing server on port %s...".formatted(port));
        server = new ServerSocket(port);
        logger.info("Server initialized successfully!");
    }
}