package Presentation.HRUI;

import Presentation.HRUI.templates.StyledButtonUI;
import defult.BusinessLayer.Controllers.HR.ShiftBoard;
import defult.BusinessLayer.HRsystem.Employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import static defult.ServiceLayer.HR.EmployeeService.submitAShift;

public class ShiftSubmissionGUI extends JFrame {
    private ShiftBoard sb;
    private Scanner s;
    private Employee employee;
    private JTextField dateField;
    private JButton submitButton;
    private JButton exitButton;

    public ShiftSubmissionGUI(ShiftBoard sb, Scanner s, Employee employee) {
        this.sb = sb;
        this.s = s;
        this.employee = employee;



        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.PINK);

        JLabel title = new JLabel("Shift Submission");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Serif", Font.BOLD, 25));
//        title.setForeground(Color.magenta);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new FlowLayout());
        formPanel.setBackground(Color.pink);
        formPanel.setBorder(BorderFactory.createEmptyBorder(100, 650, 400, 650));
        JLabel dateLabel = new JLabel("Date (dd/MM/yyyy):");
//        dateLabel.setForeground(Color.magenta);
        dateField = new JTextField(10);
        submitButton = new JButton("Submit");
        submitButton.setForeground(Color.magenta);
        exitButton = new JButton("Go back");
        exitButton.setForeground(Color.magenta);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateP = dateField.getText();
                boolean flag = false;
                LocalDate date = null;
                //while (date == null) {
                    try {
                        date = LocalDate.parse(dateP, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        flag = true;
                    } catch (DateTimeParseException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid date format. Please enter date in dd/MM/yyyy format.", "Error", JOptionPane.ERROR_MESSAGE);
                        //dateP = JOptionPane.showInputDialog("Enter date on (dd/MM/yyyy):");
                    }
                //}
                if(flag) {
                    if (date.isAfter(LocalDate.now()) && date.isBefore(LocalDate.now().plusDays(6))) {
                        String[] shiftOptions = {"Morning", "Night"};
                        int typeP = JOptionPane.showOptionDialog(null, "Which shift:", "Shift Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, shiftOptions, shiftOptions[0]);
                        boolean type = (typeP != 0);

                        try {
                            sb.EC.employeeSubmit(employee, date, type);
                            JOptionPane.showMessageDialog(null, "Shift was submitted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            dispose();
                            ShiftSubmissionGUI window = new ShiftSubmissionGUI(sb, s, employee);
                            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
                            window.setVisible(true);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Enter a date in the next week.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
                                         @Override
                                         public void actionPerformed(ActionEvent e) {
                                             dispose();
                                             EmployeeServiceUI window = new EmployeeServiceUI(sb, s, employee);
                                             window.setExtendedState(JFrame.MAXIMIZED_BOTH);
                                             window.setVisible(true);
                                         }
                                     });

        submitButton.setUI(new StyledButtonUI());
        exitButton.setUI(new StyledButtonUI());
        formPanel.add(dateLabel);
        formPanel.add(dateField);
        formPanel.add(submitButton);
        formPanel.add(exitButton);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(Box.createRigidArea(new Dimension(100, 200)));
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
        pack();
        setVisible(true);
    }

    private void inEmployeeWindow(ShiftBoard sb, Scanner s, Employee employee) {
        // Implement your logic for going back to the employee window
    }

    // Define other methods and classes as necessary

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ShiftBoard sb = new ShiftBoard();
                Scanner s = new Scanner(System.in);
                Employee employee = new Employee();

                new ShiftSubmissionGUI(sb, s, employee);
            }
        });
    }
}
