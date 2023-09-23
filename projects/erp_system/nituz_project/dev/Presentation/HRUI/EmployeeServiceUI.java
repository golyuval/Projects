package Presentation.HRUI;

import Presentation.HRUI.templates.StyledButtonUI;
import defult.BusinessLayer.Controllers.HR.ShiftBoard;
import defult.BusinessLayer.HRsystem.Employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class EmployeeServiceUI extends JFrame {
    private ShiftBoard sb;
    private Scanner s;
    JButton submitAShiftButton, removeSubmissionButton, exitButton;

    public EmployeeServiceUI(ShiftBoard sb,Scanner s, Employee employee) {



        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.PINK);


        JLabel title = new JLabel("Employee system");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Serif", Font.BOLD, 25));
//        title.setForeground(Color.magenta);


        submitAShiftButton = new JButton("Submit a shift");
        removeSubmissionButton = new JButton("Remove submission");
        exitButton = new JButton("Exit");
        exitButton.setForeground(Color.magenta);
        submitAShiftButton.setForeground(Color.magenta);
        removeSubmissionButton.setForeground(Color.magenta);

        submitAShiftButton.setPreferredSize(new Dimension(300, 50));
        removeSubmissionButton.setPreferredSize(new Dimension(300, 50));
        exitButton.setPreferredSize(new Dimension(300, 50));

        submitAShiftButton.setMaximumSize(submitAShiftButton.getPreferredSize());
        removeSubmissionButton.setMaximumSize(removeSubmissionButton.getPreferredSize());
        exitButton.setMaximumSize(exitButton.getPreferredSize());


        submitAShiftButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeSubmissionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitAShiftButton.addActionListener(new ActionListener() {
                                                 @Override
                                                 public void actionPerformed(ActionEvent e) {
                                                    dispose();
                                                    ShiftSubmissionGUI window = new ShiftSubmissionGUI(sb, s, employee);
                                                     window.setExtendedState(JFrame.MAXIMIZED_BOTH);
                                                    window.setVisible(true);
                                                 }
                                             });
        removeSubmissionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                RemoveSubmissionGUI window = new RemoveSubmissionGUI(sb, s, employee);
                window.setExtendedState(JFrame.MAXIMIZED_BOTH);
                window.setVisible(true);
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                employeeServiceGUI window = new employeeServiceGUI(sb, s);
                window.setExtendedState(JFrame.MAXIMIZED_BOTH);
                window.setVisible(true);
            }
        });
        submitAShiftButton.setUI(new StyledButtonUI());
        removeSubmissionButton.setUI(new StyledButtonUI());
        exitButton.setUI(new StyledButtonUI());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 100)));  // Creates empty space
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));  // Creates empty space
        mainPanel.add(submitAShiftButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));  // Creates empty space
        mainPanel.add(removeSubmissionButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));  // Creates empty space
        mainPanel.add(exitButton);

        // Add mainPanel to the frame
        this.add(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }



    //public static void main(String[] args) {
        //SwingUtilities.invokeLater(() -> new EmployeeServiceUI());
    }
