package ru.dfhub.eirc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Class for working with the server. Receiving and sending messages is done here.
 */
public class ServerConnection extends Thread {

    private Socket server;
    private BufferedReader in;
    private PrintWriter out;

    public ServerConnection(String serverAddress, int serverPort) throws Exception {
        server = new Socket(serverAddress, serverPort);
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        out = new PrintWriter(server.getOutputStream(), true);

        this.start();
    }

    @Override
    public void run() {
        while (server.isConnected()) {
            try {
                DataParser.handleInputData(in.readLine());
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Send information to the server
     * @param data Data
     */
    public void sendToServer(String data) {
        out.write(data);
    }
}
