package Presentation.HRUI;

import defult.BusinessLayer.Controllers.HR.ShiftBoard;
import defult.BusinessLayer.HRsystem.Shift;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.Scanner;

public class InShiftGUI extends JFrame {
    private ShiftBoard sb;
    private Scanner s;
    JButton cancelTransButton, endShiftShiftButton, exitButton;

    public InShiftGUI(ShiftBoard sb, Scanner s, Shift shift) {
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
        JLabel title = new JLabel("hello, have a nice shift!");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Serif", Font.BOLD, 25));
        title.setForeground(Color.magenta);
        // Create buttons
        cancelTransButton = new JButton("Cancel Transaction");
        endShiftShiftButton = new JButton("End Shift");
        exitButton = new JButton("Exit");

        // Set button sizes
        cancelTransButton.setPreferredSize(new Dimension(300, 50));
        endShiftShiftButton.setPreferredSize(new Dimension(300, 50));
        exitButton.setPreferredSize(new Dimension(300, 50));
        cancelTransButton.setForeground(Color.magenta);
        endShiftShiftButton.setForeground(Color.magenta);
        exitButton.setForeground(Color.magenta);
        cancelTransButton.setMaximumSize(cancelTransButton.getPreferredSize());
        endShiftShiftButton.setMaximumSize(endShiftShiftButton.getPreferredSize());
        exitButton.setMaximumSize(exitButton.getPreferredSize());

        // Center align the buttons
        cancelTransButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        endShiftShiftButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        cancelTransButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cashier1 ="";
                String cashier = JOptionPane.showInputDialog(null, "Enter cashier Name:", "Cancel transaction", JOptionPane.QUESTION_MESSAGE);
                while (cashier.equals(""));
                //cashier = JOptionPane.showInputDialog(null, "Enter cashier Name:", "Cancel transaction", JOptionPane.QUESTION_MESSAGE);
                try {
                    sb.SC.validateCashierInShift(cashier, shift);
                    JOptionPane.showMessageDialog(null, "Transaction canceled successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    sb.SC.logCancelTransaction(shift, cashier);
                }catch (Exception ex){
                    cashier1="";
                    JOptionPane.showMessageDialog(null, "Cashier is not in shift", "Error", JOptionPane.ERROR_MESSAGE);
                }
                dispose();
                InShiftGUI window = new InShiftGUI(sb, s, shift);
                window.setExtendedState(JFrame.MAXIMIZED_BOTH);
                window.setVisible(true);

            }
        });
        endShiftShiftButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sb.endShiftPolicy(shift);
                sb.SC.logEndShift(shift);
                JOptionPane.showMessageDialog(null, "thank you, goodbye!", "Shift Ended", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                ShiftManagerUI window = new ShiftManagerUI(sb, s);
                window.setExtendedState(JFrame.MAXIMIZED_BOTH);
                window.setVisible(true);

            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                ShiftManagerUI window = new ShiftManagerUI(sb, s);
                window.setExtendedState(JFrame.MAXIMIZED_BOTH);
                window.setVisible(true);
            }
        });

        // Add components to the main panel
        mainPanel.add(Box.createRigidArea(new Dimension(0, 100)));  // Creates empty space
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));  // Creates empty space
        mainPanel.add(cancelTransButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));  // Creates empty space
        mainPanel.add(endShiftShiftButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));  // Creates empty space
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