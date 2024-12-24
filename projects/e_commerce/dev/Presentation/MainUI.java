package Presentation;

import Presentation.DeliveryUI.DeliverySystemUI;
import Presentation.HRUI.HRServiceUI;
import Presentation.HRUI.ShiftSubmissionGUI;
import Presentation.HRUI.templates.StyledButtonUI;
import defult.BusinessLayer.Controllers.HR.ShiftBoard;
import defult.BusinessLayer.HRsystem.Employee;
import defult.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class MainUI extends JFrame {

    JButton hrButton, deliveryButton, exitButton;

    public MainUI() {

        // Set the frame to full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setUndecorated(true);

        // Create a panel with pink color
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.PINK);

        // Create title and align it center
        JLabel title = new JLabel("Main Menu");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Serif", Font.BOLD, 25));

        // Create buttons
        hrButton = new JButton("HR System");
        deliveryButton = new JButton("Delivery System");
        exitButton = new JButton("Exit");

        // Set button sizes
        hrButton.setPreferredSize(new Dimension(300, 50));
        deliveryButton.setPreferredSize(new Dimension(300, 50));
        exitButton.setPreferredSize(new Dimension(300, 50));

        hrButton.setMaximumSize(hrButton.getPreferredSize());
        deliveryButton.setMaximumSize(deliveryButton.getPreferredSize());
        exitButton.setMaximumSize(exitButton.getPreferredSize());

        // Set the StyledButtonUI for all buttons
        hrButton.setUI(new StyledButtonUI());
        deliveryButton.setUI(new StyledButtonUI());
        exitButton.setUI(new StyledButtonUI());

        // Center align the buttons
        hrButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deliveryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components to the main panel
        mainPanel.add(Box.createRigidArea(new Dimension(0, 100)));  // Creates empty space
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));  // Creates empty space
        mainPanel.add(hrButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));  // Creates empty space
        mainPanel.add(deliveryButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));  // Creates empty space
        mainPanel.add(exitButton);

        // Add mainPanel to the frame
        this.add(mainPanel);

        // Set ActionListener for exit button
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Set ActionListener for delivery button
        deliveryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                DeliverySystemUI deliverySystemUI = new DeliverySystemUI();
                deliverySystemUI.setVisible(true);
            }
        });

        // Set ActionListener for HR button
        hrButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                HRServiceUI hrSystemUI = new HRServiceUI(ShiftBoard.getInstance(), new Scanner(System.in));
                hrSystemUI.setVisible(true);
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

//                Main.initData();
                new MainUI();
            }
        });
    }

}
