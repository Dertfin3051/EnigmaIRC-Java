package ru.dfhub.eirc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Gui {

    public static enum MessageType {
        SELF_USER_MESSAGE, USER_MESSAGE, SYSTEM_GOOD, SYSTEM_INFO, SYSTEM_ERROR, USER_SESSION
    }

    private static final JFrame window = new JFrame("EnigmaIRC");
    private static final Box mainPanel = Box.createVerticalBox();

    private static final Box messageBox = Box.createVerticalBox();
    private static JScrollPane messageBoxScrollbar = new JScrollPane(messageBox);

    private static final Box inputFieldPanel = Box.createHorizontalBox();
    private static final JTextField inputField = new JTextField();

    private static final Color COMPONENT_BORDER_COLOR = Color.decode("#614376");

    public static void init() {
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(800, 500);
        window.setResizable(false);

        mainPanel.setPreferredSize(new Dimension(800, 500));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Window padding
        setBackgroundColor(mainPanel, "#2a1f33");

        messageBoxScrollbar.setPreferredSize(new Dimension(780, 450));
        messageBoxScrollbar.getHorizontalScrollBar().setPreferredSize(new Dimension(800, 8));
        messageBoxScrollbar.getVerticalScrollBar().setPreferredSize(new Dimension(8, 500));
        messageBox.setBorder(new EmptyBorder(7, 7, 7, 7)); // Message Box padding
        setBackgroundColor(messageBox, "#4b3c57");
        messageBoxScrollbar.setBorder(new LineBorder(COMPONENT_BORDER_COLOR));

        inputFieldPanel.setBorder(new EmptyBorder(5, 0, 0, 0)); // Top padding
        inputFieldPanel.add(inputField);
        inputField.setPreferredSize(new Dimension(800, 30)); // 25px to input field & 5px free space
        inputField.setForeground(Color.WHITE); // Input text color
        setBackgroundColor(inputField, "#27202e");
        inputField.setBorder(new LineBorder(COMPONENT_BORDER_COLOR));


        inputField.addActionListener(Gui::inputAction);

        mainPanel.add(messageBoxScrollbar); mainPanel.add(inputFieldPanel);
        window.add(mainPanel);
        show();
    }

    /**
     * Pack all components and show window ad center of screen
     */
    public static void show() {
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    /**
     * Show new message
     * @param formattedMessage Formatted message
     */
    public static void showNewMessage(String formattedMessage, MessageType type) {
        JLabel message = new JLabel(formattedMessage);
        switch (type) {
            case SYSTEM_GOOD -> message.setForeground(new Color(0, 245, 0));
            case SYSTEM_INFO -> message.setForeground(new Color(245, 245, 0));
            case SYSTEM_ERROR -> message.setForeground(new Color(245, 0, 0));
            case SELF_USER_MESSAGE -> message.setForeground(Color.decode("#cbb6dc"));
            case USER_MESSAGE -> message.setForeground(Color.WHITE);
            case USER_SESSION -> message.setForeground(Color.decode("#ffb148"));
        }
        message.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));

        messageBox.add(message);
        scrollDown();
        updateWindow();
    }

    /**
     * Show welcome message.
     * Used instead of SYSTEM_GOOD due to increased font size
     */
    public static void showWelcomeMessage() {
        JLabel message = new JLabel("Welcome to EnigmaIRC!");
        message.setForeground(new Color(0, 245, 0));
        message.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        message.setBorder(new EmptyBorder(0, 0, 10, 0));

        messageBox.add(message);
        updateWindow();
    }

    /**
     * Block input and exit the program after 2 minutes
     * Used for critical errors, implying the inability to further work with the program
     */
    public static void breakInput() {
        inputField.setVisible(false);
        updateWindow();
        try {
            Thread.sleep(1000 * 120);
        } catch (InterruptedException e) {}
        System.exit(0);
    }

    /**
     * Scroll down messageBox. Only for vertical scroll
     */
    public static void scrollDown() {
        JScrollBar newScroll = messageBoxScrollbar.getVerticalScrollBar();
        newScroll.setValue(messageBoxScrollbar.getVerticalScrollBar().getMaximum());
        messageBoxScrollbar.setVerticalScrollBar(newScroll);
    }

    /**
     * Revalidate all elements and repaint window
     */
    private static void updateWindow() { window.revalidate(); window.repaint(); }

    /**
     * Handle inputField "Enter" button
     * @param e Event
     */
    private static void inputAction(ActionEvent e) {
        String input = inputField.getText();
        switch (input) {
            case "!!clear" -> messageBox.removeAll();
            case "!!exit" -> {
                // Send exit message
                System.exit(0);
            }
            default -> {
                DataParser.handleOutputMessage(inputField.getText());
            }
        }

        inputField.setText("");
        scrollDown();
        updateWindow(); // Update
    }

    /**
     * Change JComponent background color
     * @param component JComponent
     * @param hex Hex-color (# and 6 symbols)
     */
    private static void setBackgroundColor(JComponent component, String hex) {
        component.setOpaque(true); component.setBackground(Color.decode(hex));
    }

}