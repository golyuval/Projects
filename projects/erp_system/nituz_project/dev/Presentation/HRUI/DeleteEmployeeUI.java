package Presentation.HRUI;

import defult.BusinessLayer.Controllers.HR.ShiftBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class DeleteEmployeeUI extends JPanel {
    private ShiftBoard shiftBoard;
    private Scanner scanner;
    private JLabel employeeIdLabel;
    private JTextField employeeIdTextField;
    private JButton deleteButton;
    private JButton refreshBtn;
    private boolean validId = false;

    public DeleteEmployeeUI(ShiftBoard shiftBoard, Scanner scanner) {
        this.shiftBoard = shiftBoard;
        this.scanner = scanner;
        setLayout(new BorderLayout());
        setBackground(Color.PINK);
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(Color.PINK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        employeeIdLabel = new JLabel("Employee ID:");
        employeeIdTextField = new JTextField(10);
        deleteButton = new JButton("Delete");
        refreshBtn = new JButton("check");

        updateDeleteButtonState();

        employeeIdTextField.addFocusListener(new FocusAdapter() {
             @Override
             public void focusGained(FocusEvent e) {

                 String text = employeeIdTextField.getText();
                 if (text.matches("\\d+") && text.length() == 9 && !shiftBoard.EC.validateId(Integer.parseInt(text))) {
                     employeeIdTextField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                     validId = true;
                 } else {
                     int thickness = 2; // Set the desired thickness of the border
                     employeeIdTextField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                     validId = false;
                 }
                 updateDeleteButtonState();
             }

            @Override
            public void focusLost(FocusEvent e) {
                String text = employeeIdTextField.getText();
                if (text.matches("\\d+") && text.length() == 9 && !shiftBoard.EC.validateId(Integer.parseInt(text))) {
                    employeeIdTextField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                    validId = true;

                } else {
                    int thickness = 2; // Set the desired thickness of the border
                    employeeIdTextField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    validId = false;
                }
                updateDeleteButtonState();
             }
        });


        contentPanel.add(employeeIdLabel, gbc);

        gbc.gridx++;
        contentPanel.add(employeeIdTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(deleteButton, gbc);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeId = employeeIdTextField.getText();
                int id = Integer.parseInt(employeeId);
                try {
                    UIManager.put("OptionPane.background", Color.PINK);
                    UIManager.put("Panel.background", Color.PINK);
                    shiftBoard.SC.canDeleteEmployee(shiftBoard.EC.getEmployeeById(id));
                    shiftBoard.EC.removeEmployee(id);
                    JOptionPane.showMessageDialog(null, "Employee with ID " + employeeId + " has been deleted.");

                }catch (Exception ex){
                    UIManager.put("OptionPane.background", Color.PINK);
                    UIManager.put("Panel.background", Color.PINK);
                    JOptionPane.showMessageDialog(null, "Error: "+ex.getMessage());

                }
                // Perform the delete operation using ShiftBoard or any other relevant logic
                // ...
                // Display a message or perform any required actions after deletion
                employeeIdTextField.setText("");
                updateDeleteButtonState();
            }
        });
        gbc.gridy++;
        contentPanel.add(refreshBtn, gbc);

        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDeleteButtonState();

            }
        });

        add(contentPanel, BorderLayout.CENTER);
    }

    void backButtonFunc() {
        new ManagerWindowUI();
    }

    private void updateDeleteButtonState() {
        deleteButton.setEnabled(validId);
    }
}
