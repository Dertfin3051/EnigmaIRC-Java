package ru.dfhub.eirc;

public class Main {

    private static ServerConnection serverConnection;

    public static void main(String[] args) {
        Gui.init();
        Gui.showWelcomeMessage();

        try {
            serverConnection = new ServerConnection("127.0.0.1", 6667);
        } catch (Exception e)
        {
            Gui.breakInput();
            Gui.showNewMessage("Failed connect to the server!", Gui.MessageType.SYSTEM_ERROR);
        }
    }

    public ServerConnection getServerConnection() {
        return serverConnection;
    }
}