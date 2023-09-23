package Presentation.HRUI;

import Presentation.HRUI.templates.BaseWindowUI;
import Presentation.HRUI.templates.EventSwitchSelected;
import Presentation.HRUI.templates.SwitchButton;
import defult.BusinessLayer.Controllers.HR.ShiftBoard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class AddNewEmployeeUI extends BaseWindowUI {
    private JTextField nameField;
    private JTextField idField;
    private JPasswordField passwordField;
    private SwitchButton showPasswordToggle;
    private JTextField bankInfoField;
    private JTextField salaryField;
    private JTextField startDateField;
    private JTextField termsField;
    private JCheckBox shiftManagerCheckBox;
    private JCheckBox cashierCheckBox;
    private JCheckBox storageCheckBox;
    private JCheckBox securityCheckBox;
    private JCheckBox cleanerCheckBox;
    private JCheckBox organizerCheckBox;
    private JCheckBox generalCheckBox;
    private JTextField specialInfoField;
    private ButtonGroup driverGroup;
    private JRadioButton driverRadioButton;
    private JRadioButton nonDriverRadioButton;

    ShiftBoard sb;

    public AddNewEmployeeUI(ShiftBoard sb, Scanner s) {

        super("Add New Employee", true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        updateSaveButtonState();

        this.sb = sb;


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                // Add code to handle back button click and return to the main menu
            }
        });
    }
    public void backButtonFunc() {
        new ManagerWindowUI();
    }
    // Helper method to check if the date format is valid
    private boolean isValidDateFormat(String date) {
        String pattern = "^(0[1-9]|1\\d|2\\d|3[01])/(0[1-9]|1[0-2])/\\d{4}$";
        return date.matches(pattern);
    }

    private void updateSaveButtonState() {
        boolean isNameValid = !nameField.getText().isEmpty();
        boolean isPasswordValid = passwordField.getPassword().length != 0;
        boolean isIdValid = idField.getText().matches("\\d+") && idField.getText().length() == 9;
        boolean isBankInfoValid = !bankInfoField.getText().isEmpty();
        boolean isSalaryValid = salaryField.getText().matches("\\d+") && !salaryField.getText().isEmpty();
        boolean isStartDateValid = isValidDateFormat(startDateField.getText());
        boolean isRadioButtonSelected = driverRadioButton.isSelected() || nonDriverRadioButton.isSelected();

        saveBtn.setEnabled(isNameValid && isPasswordValid && isIdValid && isBankInfoValid && isSalaryValid && isStartDateValid && isRadioButtonSelected);
    }


    @Override
    protected JPanel createContentPanel() {


        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel fieldPanel = createGridPanel(2);

        driverRadioButton = new JRadioButton("Driver");
        nonDriverRadioButton = new JRadioButton("Non-Driver");
        driverGroup = new ButtonGroup();
        driverGroup.add(driverRadioButton);
        driverGroup.add(nonDriverRadioButton);
        nameField = new JTextField();
        idField = new JTextField();
        passwordField = new JPasswordField();
        showPasswordToggle = new SwitchButton();
        showPasswordToggle.addEventSelected(new EventSwitchSelected() {
            @Override
            public void onSelected(boolean selected) {
                char echoChar;
                if (selected) {
                    echoChar = '\0';
                } else{
                    echoChar = '*';
                }
                passwordField.setEchoChar(echoChar);
            }
        });


        JPanel passPanel = new JPanel();
        passPanel.setLayout(new BorderLayout());
        passPanel.setBorder(new EmptyBorder(0, 0, 0, 10)); // Add right padding

// Add the passwordField to the left side
        passPanel.add(passwordField, BorderLayout.CENTER);

// Create a wrapper panel for the toggle button
        JPanel togglePanel = new JPanel();
        togglePanel.setPreferredSize(new Dimension(50, 35)); // Adjust the height to accommodate the switch button
        togglePanel.setMaximumSize(new Dimension(50, 35));
        togglePanel.add(showPasswordToggle);

// Add the togglePanel to the right side
        passPanel.add(togglePanel, BorderLayout.EAST);

        bankInfoField = new JTextField();
        salaryField = new JTextField();
        startDateField = new JTextField();
        termsField = new JTextField();
        specialInfoField = new JTextField();

        driverRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSaveButtonState();
            }
        });

        nonDriverRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSaveButtonState();
            }
        });


        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameField.getText().isEmpty()) {
                    int thickness = 2; // Set the desired thickness of the border
                    nameField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    saveBtn.setEnabled(false);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!nameField.getText().isEmpty()) {
                    nameField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                    updateSaveButtonState();
                }else{
                    int thickness = 2; // Set the desired thickness of the border
                    idField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    saveBtn.setEnabled(false);
                }
            }
        });

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (passwordField.getPassword().length==0) {
                    int thickness = 2; // Set the desired thickness of the border
                    passwordField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    saveBtn.setEnabled(false);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getPassword().length!=0) {
                    passwordField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                    updateSaveButtonState();
                }else{
                    int thickness = 2; // Set the desired thickness of the border
                    passwordField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    saveBtn.setEnabled(false);
                }
            }
        });

        idField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String text = idField.getText();
                if (!text.matches("\\d+") || text.length() != 9) {
                    int thickness = 2; // Set the desired thickness of the border
                    idField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    saveBtn.setEnabled(false);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String text = idField.getText();
                if (text.matches("\\d+") && text.length() == 9) {
                    idField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border")); // Revert to the default border style
                    updateSaveButtonState();
                }else{
                    int thickness = 2; // Set the desired thickness of the border
                    idField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    saveBtn.setEnabled(false);
                }
            }
        });

        bankInfoField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (bankInfoField.getText().isEmpty() || !bankInfoField.getText().matches("\\d+")) {
                    int thickness = 2; // Set the desired thickness of the border
                    bankInfoField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    saveBtn.setEnabled(false);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!bankInfoField.getText().isEmpty() && bankInfoField.getText().matches("\\d+")) {
                    bankInfoField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                    updateSaveButtonState();
                }else{
                    int thickness = 2; // Set the desired thickness of the border
                    bankInfoField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    saveBtn.setEnabled(false);
                }
            }
        });



        salaryField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String text = salaryField.getText();
                if (!text.matches("\\d+") || text.isEmpty()) {
                    int thickness = 2; // Set the desired thickness of the border
                    salaryField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    saveBtn.setEnabled(false);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String text = salaryField.getText();
                if (text.matches("\\d+") && !text.isEmpty()) {
                    salaryField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border")); // Revert to the default border style
                    updateSaveButtonState();
                }else{
                    int thickness = 2; // Set the desired thickness of the border
                    salaryField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    saveBtn.setEnabled(false);
                }
            }
        });

        startDateField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String text = startDateField.getText();
                if (!isValidDateFormat(text)) {
                    int thickness = 2; // Set the desired thickness of the border
                    startDateField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    saveBtn.setEnabled(false);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String text = startDateField.getText();
                if (isValidDateFormat(text)) {
                    startDateField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border")); // Revert to the default border style
                    updateSaveButtonState();
                } else {
                    int thickness = 2; // Set the desired thickness of the border
                    startDateField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    saveBtn.setEnabled(false);
                }
            }

        });



        fieldPanel.add(new JLabel("Driver:"));
        JPanel radioPanel = createGridPanel(2);
        radioPanel.add(driverRadioButton);
        radioPanel.add(nonDriverRadioButton);
        fieldPanel.add(radioPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        fieldPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        fieldPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        fieldPanel.add(new JLabel("ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        fieldPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        fieldPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        fieldPanel.add(passPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        fieldPanel.add(new JLabel("Bank Info:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        fieldPanel.add(bankInfoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        fieldPanel.add(new JLabel("Salary:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        fieldPanel.add(salaryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        fieldPanel.add(new JLabel("Start Date:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        fieldPanel.add(startDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        fieldPanel.add(new JLabel("Terms:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        fieldPanel.add(termsField, gbc);
        panel.add(fieldPanel);


        //addTextField(new JLabel("Special Info:"), specialInfoField, panel);

        add(panel);
        return panel;
    }


    public void addButton(JButton button, JPanel panel) {
        addButton(button);
        panel.add(button);
    }

    public void addScrollPane(JScrollPane scrollPane, JPanel panel) {
        panel.add(scrollPane);
    }

    public JButton createSaveButton() {
        JButton saveButton = new JButton("Save");
        addButton(saveButton);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve input values
                String name = nameField.getText();
                int id = Integer.parseInt(idField.getText());
                String password = passwordField.getText();
                int bankInfo = Integer.parseInt(bankInfoField.getText());
                int salary = Integer.parseInt(salaryField.getText());
                String startDateText = startDateField.getText();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate startDate = LocalDate.parse(startDateText, formatter);
                List<String> terms = Arrays.asList(termsField.getText().split(","));
                boolean isDriver = driverRadioButton.isSelected();
                String specialInfo = specialInfoField.getText();
                List<String> roles = new LinkedList<>();
                List<String> licenses = new LinkedList<>();

                // Save new employee
                //sb.EC.newEmployee(name, id, bankInfo, salary, startDate, terms, roles, specialInfo, password);
                //System.out.println("Employee was added successfully");
                if(isDriver) {
                    roles.add("Driver");

                    // Create custom dialog for non-driver roles
                    JDialog dialog = new JDialog();
                    dialog.setTitle("Select Licenses");
                    dialog.setModal(true);
                    dialog.setLayout(new BorderLayout());

                    JPanel licensePanel = new JPanel();
                    licensePanel.setLayout(new BoxLayout(licensePanel, BoxLayout.Y_AXIS));
                    licensePanel.setPreferredSize(new Dimension(200, 100));

// Add checkboxes for roles
                    JCheckBox A1CheckBox = new JCheckBox("A1");
                    JCheckBox B1CheckBox = new JCheckBox("B1");
                    JCheckBox C1CheckBox = new JCheckBox("C1");

                    licensePanel.add(Box.createVerticalGlue()); // Add space at the top
                    licensePanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space between checkboxes

                    licensePanel.add(A1CheckBox);
                    licensePanel.add(B1CheckBox);
                    licensePanel.add(C1CheckBox);

                    licensePanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space underneath checkboxes

                    JButton saveLicensesButton = new JButton("Save Licenses");
                    saveLicensesButton.setPreferredSize(new Dimension(200, 50));
                    saveLicensesButton.setMaximumSize(new Dimension(200, 50));

                    saveLicensesButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Get selected roles
                            if (A1CheckBox.isSelected()) {
                                licenses.add("A1");
                            }
                            if (B1CheckBox.isSelected()) {
                                licenses.add("B1");
                            }
                            if (C1CheckBox.isSelected()) {
                                licenses.add("C1");
                            }


                            // Perform the save operation with selected roles
                            //sb.EC.newEmployee(name, id, bankInfo, salary, startDate, terms, roles, specialInfo, password);
                            try {
                                // Set the desired color for the message dialog
                                if(sb.EC.validateId(id)) {
                                    sb.EC.newDriver(name, id, bankInfo, salary, startDate, terms, roles, false, specialInfo, password, licenses);
                                    UIManager.put("OptionPane.background", Color.PINK);
                                    UIManager.put("Panel.background", Color.PINK);

                                    // Show the message dialog
                                    JOptionPane.showMessageDialog(null, "Saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                                }else{
                                    JOptionPane.showMessageDialog(null, "employee with this id already exists", "Fail", JOptionPane.INFORMATION_MESSAGE);
                                }

                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, ex.getMessage(), "Fail", JOptionPane.INFORMATION_MESSAGE);
                            }


                            dialog.dispose();
                        }
                    });
                    // Add the licensePanel and saveLicensesButton to the dialog
                    dialog.add(licensePanel, BorderLayout.CENTER);
                    dialog.add(saveLicensesButton, BorderLayout.SOUTH);

                    // Set the dialog properties and make it visible
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                    //JOptionPane.showMessageDialog(null, "Saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    // Create custom dialog for non-driver roles
                    JDialog dialog = new JDialog();
                    dialog.setTitle("Select Roles");
                    dialog.setModal(true);
                    dialog.setLayout(new BorderLayout());

                    JPanel rolesPanel = new JPanel();
                    rolesPanel.setLayout(new BoxLayout(rolesPanel, BoxLayout.Y_AXIS));
                    rolesPanel.setPreferredSize(new Dimension(200, 300));

// Add checkboxes for roles
                    JCheckBox shiftManagerCheckBox = new JCheckBox("Shift Manager");
                    JCheckBox cashierCheckBox = new JCheckBox("Cashier");
                    JCheckBox storageCheckBox = new JCheckBox("Storage");
                    JCheckBox securityCheckBox = new JCheckBox("Security");
                    JCheckBox cleanerCheckBox = new JCheckBox("Cleaner");
                    JCheckBox organizerCheckBox = new JCheckBox("Organizer");
                    JCheckBox generalCheckBox = new JCheckBox("General");

                    rolesPanel.add(Box.createVerticalGlue()); // Add space at the top
                    rolesPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space between checkboxes

                    rolesPanel.add(shiftManagerCheckBox);
                    rolesPanel.add(cashierCheckBox);
                    rolesPanel.add(storageCheckBox);
                    rolesPanel.add(securityCheckBox);
                    rolesPanel.add(cleanerCheckBox);
                    rolesPanel.add(organizerCheckBox);
                    rolesPanel.add(generalCheckBox);

                    rolesPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space underneath checkboxes

                    JButton saveRolesButton = new JButton("Save Roles");
                    saveRolesButton.setPreferredSize(new Dimension(200, 50));
                    saveRolesButton.setMaximumSize(new Dimension(200, 50));

                    saveRolesButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Get selected roles
                            List<String> roles = new ArrayList<>();
                            if (shiftManagerCheckBox.isSelected()) {
                                roles.add("Shift Manager");
                            }
                            if (cashierCheckBox.isSelected()) {
                                roles.add("Cashier");
                            }
                            if (storageCheckBox.isSelected()) {
                                roles.add("Storage");
                            }
                            if (securityCheckBox.isSelected()) {
                                roles.add("Security");
                            }
                            if (cleanerCheckBox.isSelected()) {
                                roles.add("Cleaner");
                            }
                            if (organizerCheckBox.isSelected()) {
                                roles.add("Organizer");
                            }
                            if (generalCheckBox.isSelected()) {
                                roles.add("General");
                            }

                            // Perform the save operation with selected roles
                            //sb.EC.newEmployee(name, id, bankInfo, salary, startDate, terms, roles, specialInfo, password);
                            try {
                                if(sb.EC.validateId(id)) {
                                    // Set the desired color for the message dialog
                                    sb.EC.newEmployee(name, id, bankInfo, salary, startDate, terms, roles, roles.contains("Manager"), specialInfo, password);
                                    UIManager.put("OptionPane.background", Color.PINK);
                                    UIManager.put("Panel.background", Color.PINK);

                                    // Show the message dialog
                                    JOptionPane.showMessageDialog(null, "Saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                                }else{
                                    JOptionPane.showMessageDialog(null, "employee with this id already exists", "Fail", JOptionPane.INFORMATION_MESSAGE);

                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            dialog.dispose();
                        }
                    });

                    rolesPanel.add(saveRolesButton);
                    rolesPanel.add(Box.createVerticalGlue()); // Add space at the bottom

                    dialog.add(rolesPanel, BorderLayout.CENTER);
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);

                }
                new ManagerWindowUI();
                dispose();
                // Add code to handle saving the new employee
            }
        });
        return saveButton;
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AddNewEmployeeUI(ShiftBoard.getInstance(), new Scanner(System.in));
            }
        });
    }


}
