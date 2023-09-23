package Presentation.HRUI;

import Presentation.HRUI.templates.StyledButtonUI;
import defult.BusinessLayer.Controllers.HR.ShiftBoard;
import defult.BusinessLayer.HRsystem.Employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class employeeServiceGUI extends JFrame {
    private ShiftBoard sb;
    private Scanner s;
    private JTextField idField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public employeeServiceGUI(ShiftBoard sb, Scanner s) {
        this.sb = sb;
        this.s = s;

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.PINK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Employee Login");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 25));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.PINK);

        JLabel idLabel = new JLabel("ID:");
        idField = new JTextField(10);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(10);
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(200, 50));
        loginButton.setUI(new StyledButtonUI());

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(idField.getText());
                String password = new String(passwordField.getPassword());

                if (sb.EC.validateId(id)) {
                    JOptionPane.showMessageDialog(null, "ID does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (sb.EC.validateStartDate(id)) {
                    JOptionPane.showMessageDialog(null, "Your contract has not yet begun.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!sb.EC.getEmployeeById(id).getPassword().equals(password)) {
                    JOptionPane.showMessageDialog(null, "Incorrect password.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    Employee employee = sb.EC.getEmployeeById(id);
                    dispose();
                    EmployeeServiceUI window = new EmployeeServiceUI(sb, s, employee);
                    window.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    window.setVisible(true);
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(idField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(50, 0, 20, 0);
        mainPanel.add(titleLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(formPanel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        mainPanel.add(loginButton, gbc);

        add(mainPanel);
        pack();
        setVisible(true);
        centerFrame();
    }

    private void centerFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        setLocation(x, y);
    }

    private void inEmployeeWindow(ShiftBoard sb, Scanner s, Employee employee) {
        // Implement your logic for going to the employee window
    }

    // Define other methods and classes as necessary

    //    public static void main(String[] args) {
    //        SwingUtilities.invokeLater(new Runnable() {
    //            @Override
    //            public void run() {
    //                ShiftBoard sb = new ShiftBoard();
    //                Scanner s = new Scanner(System.in);
    //
    //                new EmployeeServiceGUI(sb, s);
    //            }
    //        });
    //    }
}
