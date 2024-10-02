package ru.dfhub.eirc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Gui {

    public static enum MessageType {
        USER_MESSAGE, SYSTEM_GOOD, SYSTEM_INFO, SYSTEM_ERROR
    }

    private static final JFrame window = new JFrame("EnigmaIRC");
    private static final Box mainPanel = Box.createVerticalBox();

    private static final Box messageBox = Box.createVerticalBox();
    private static JScrollPane messageBoxScrollbar = new JScrollPane(messageBox);

    private static final Box inputFieldPanel = Box.createHorizontalBox();
    private static final JTextField inputField = new JTextField();

    public static void init() {
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(800, 500);
        window.setResizable(false);

        mainPanel.setPreferredSize(new Dimension(800, 500));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Window padding

        messageBoxScrollbar.setPreferredSize(new Dimension(780, 450));
        messageBox.setBorder(new EmptyBorder(7, 7, 7, 7)); // Message Box padding

        inputFieldPanel.add(inputField);
        inputField.setPreferredSize(new Dimension(800, 30)); // 25px to input field & 5px free space
        inputFieldPanel.setBorder(new EmptyBorder(5, 0, 0, 0)); // Top padding

        inputField.addActionListener(Gui::inputAction);

        mainPanel.add(messageBoxScrollbar); mainPanel.add(inputFieldPanel);
        window.add(mainPanel);
        show();
    }

    public static void show() {
        window.setVisible(true);
    }

    /**
     * Show new message
     * @param formattedMessage Formatted message
     */
    public static void showNewMessage(String formattedMessage, MessageType type) {
        JLabel message = new JLabel(formattedMessage);
        switch (type) {
            case SYSTEM_GOOD -> message.setForeground(new Color(0, 180, 0));
            case SYSTEM_INFO -> message.setForeground(new Color(180, 180, 0));
            case SYSTEM_ERROR -> message.setForeground(new Color(180, 0, 0));
            default -> message.setForeground(Color.BLACK);
        }
        message.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));

        messageBox.add(message);
        updateWindow();
    }

    public static void showWelcomeMessage() {
        JLabel message = new JLabel("Welcome to EnigmaIRC!");
        message.setForeground(new Color(0, 180, 0));
        message.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        message.setBorder(new EmptyBorder(0, 0, 10, 0));

        messageBox.add(message);
        updateWindow();
    }

    public static void breakInput() {
        inputField.setVisible(false);
    }

    private static void updateWindow() { window.revalidate(); window.repaint(); }

    private static void inputAction(ActionEvent e) {
        String input = inputField.getText();
        switch (input) {
            case "!!clear" -> messageBox.removeAll();
            case "!!exit" -> {
                // Send exit message
                System.exit(0);
            }
            default -> {
                // Send message method
            }
        }

        inputField.setText("");
        updateWindow(); // Update
    }

}
