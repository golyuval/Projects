package Presentation.DeliveryUI.Manager_UI;

import Presentation.DeliveryUI.DeliverySystemUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerUI extends JFrame {

    JButton sendDeliveryButton, updateDeliveryButton, exitButton;

    public ManagerUI() {

        // Set the frame to full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        // Create a panel with pink color
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.PINK);

        // Create title and align it center
        JLabel title = new JLabel("Manager Menu");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Serif", Font.BOLD, 25));

        // Create buttons
        sendDeliveryButton = new JButton("Send delivery");
        updateDeliveryButton = new JButton("Update delivery state to : Delivery Done");
        exitButton = new JButton("Exit");

        // Set button sizes
        sendDeliveryButton.setPreferredSize(new Dimension(300, 50));
        updateDeliveryButton.setPreferredSize(new Dimension(300, 50));
        exitButton.setPreferredSize(new Dimension(300, 50));

        sendDeliveryButton.setMaximumSize(sendDeliveryButton.getPreferredSize());
        updateDeliveryButton.setMaximumSize(updateDeliveryButton.getPreferredSize());
        exitButton.setMaximumSize(exitButton.getPreferredSize());

        // Center align the buttons
        sendDeliveryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateDeliveryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add action listeners to buttons
        sendDeliveryButton.addActionListener(e -> {
            dispose(); // Close the current window
            SendDelivery_M_UI sendDeliveryMUi = new SendDelivery_M_UI();
            sendDeliveryMUi.setVisible(true);
            System.out.println("Send delivery button pressed");
        });
        updateDeliveryButton.addActionListener(e -> {
            dispose(); // Close the current window
            UpdateDelivery_M_UI updateDeliveryMUi = new UpdateDelivery_M_UI();
            updateDeliveryMUi.setVisible(true);
            System.out.println("Update delivery state button pressed");
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new DeliverySystemUI(); // Open the DeliverySystemUI window
            }
        });

        // Add components to the main panel
        mainPanel.add(Box.createRigidArea(new Dimension(0, 100)));  // Creates empty space
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));  // Creates empty space
        mainPanel.add(sendDeliveryButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));  // Creates empty space
        mainPanel.add(updateDeliveryButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));  // Creates empty space
        mainPanel.add(exitButton);

        // Add mainPanel to the frame
        this.add(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManagerUI());
    }
}
