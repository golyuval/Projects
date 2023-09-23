package Presentation.HRUI;

import defult.BusinessLayer.Controllers.HR.ShiftBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class ShiftManagerUI extends JFrame {
    private ShiftBoard sb;
    private Scanner s;
    JButton startShiftButton, backToShiftButton, exitButton;

    public ShiftManagerUI(ShiftBoard sb, Scanner s) {
        this.sb =sb;
        // Set the frame to full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // Create a panel with pink color
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.PINK);

        // Create title and align it center
        JLabel title = new JLabel("Shift manager system");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Serif", Font.BOLD, 25));
        title.setForeground(Color.magenta);
        // Create buttons
        startShiftButton = new JButton("Enter Shift");
        //backToShiftButton = new JButton("Back to shift");
        exitButton = new JButton("Exit");

        // Set button sizes
        startShiftButton.setPreferredSize(new Dimension(300, 50));
        //backToShiftButton.setPreferredSize(new Dimension(300, 50));
        exitButton.setPreferredSize(new Dimension(300, 50));
        startShiftButton.setForeground(Color.magenta);
        //backToShiftButton.setForeground(Color.magenta);
        exitButton.setForeground(Color.magenta);
        startShiftButton.setMaximumSize(startShiftButton.getPreferredSize());
        //backToShiftButton.setMaximumSize(backToShiftButton.getPreferredSize());
        exitButton.setMaximumSize(exitButton.getPreferredSize());

        // Center align the buttons
        startShiftButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        //backToShiftButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        startShiftButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                StartShiftGui window = new StartShiftGui(sb, s);
                window.setExtendedState(JFrame.MAXIMIZED_BOTH);
                window.setVisible(true);

            }
                                             });
//        backToShiftButton.addActionListener(new ActionListener() {
//                                               public void actionPerformed(ActionEvent e) {
//                                                   Shift shift = sb.SC.getShiftByDateHour(lacal);
//                                                   dispose();
//                                                   InShiftGUI window = new InShiftGUI(shift,sb,s);
//                                                   window.setVisible(true);
//                                               }
//                                           });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                HRServiceUI window = new HRServiceUI(sb, s);
                window.setExtendedState(JFrame.MAXIMIZED_BOTH);
                window.setVisible(true);
            }
        });

        // Add components to the main panel
        mainPanel.add(Box.createRigidArea(new Dimension(0, 100)));  // Creates empty space
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));  // Creates empty space
        mainPanel.add(startShiftButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));  // Creates empty space
        //mainPanel.add(backToShiftButton);
        //mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));  // Creates empty space
        mainPanel.add(exitButton);

        // Add mainPanel to the frame
        this.add(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new ShiftManagerUI());
//    }
}