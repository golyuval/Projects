package Presentation.HRUI;

import Presentation.HRUI.templates.BaseWindowUI;
import defult.BusinessLayer.Controllers.HR.ShiftBoard;
import defult.BusinessLayer.HRsystem.Employee;
import defult.Main;
import defult.ServiceLayer.Singletones.Validator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class EditEmployeeUI extends BaseWindowUI {

    private JTextField idField;
    private JTextField salaryField;
    private JTextField bankInfoField;
    private JTextField specialInfoField;
    private JTextField termsField;
    private boolean isValidId = false;
    private boolean isDriver;
    private JCheckBox shiftManagerCheckBox;
    private JCheckBox cashierCheckBox;
    private JCheckBox storageCheckBox;
    private JCheckBox securityCheckBox;
    private JCheckBox cleanerCheckBox;
    private JCheckBox organizerCheckBox;
    private JCheckBox generalCheckBox;
    private JButton editRolesButton;


    private ShiftBoard sb;
    private Scanner scanner;

    public EditEmployeeUI(ShiftBoard sb, Scanner scanner) {
        super("Edit Employee", true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        salaryField.setEnabled(isValidId);
//        bankInfoField.setEnabled(isValidId);
//        specialInfoField.setEnabled(isValidId);
//        termsField.setEnabled(isValidId);

        saveBtn.setEnabled(isValidId);
        editRolesButton.setEnabled(isValidId && !isDriver);
        this.sb = sb;
        this.scanner = scanner;
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                // Add code to handle back button click and return to the main menu
            }
        });
    }
    @Override
    protected JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel fieldPanel = createGridPanel(2);

        shiftManagerCheckBox = new JCheckBox();
        cashierCheckBox = new JCheckBox();
        storageCheckBox = new JCheckBox();
        securityCheckBox = new JCheckBox();
        cleanerCheckBox = new JCheckBox();
        organizerCheckBox = new JCheckBox();
        generalCheckBox = new JCheckBox();

        idField = new JTextField();
        salaryField = new JTextField();
        bankInfoField = new JTextField();
        specialInfoField = new JTextField();
        termsField = new JTextField();
        editRolesButton = new JButton("Edit Roles");

        idField.setName("idField"); // Set the name for idField
        salaryField.setName("salaryField"); // Set the name for salaryField
        bankInfoField.setName("bankInfoField"); // Set the name for bankInfoField
        specialInfoField.setName("specialInfoField"); // Set the name for specialInfoField
        termsField.setName("termsField"); // Set the name for termsField

        idField.addFocusListener(new TextFieldFocusListener());
        salaryField.addFocusListener(new TextFieldFocusListener());
        bankInfoField.addFocusListener(new TextFieldFocusListener());
        specialInfoField.addFocusListener(new TextFieldFocusListener());
        termsField.addFocusListener(new TextFieldFocusListener());

        fieldPanel.add(new JLabel("ID:"));
        fieldPanel.add(idField);
        fieldPanel.add(new JLabel("Salary:"));
        fieldPanel.add(salaryField);
        fieldPanel.add(new JLabel("Bank Info:"));
        fieldPanel.add(bankInfoField);
        fieldPanel.add(new JLabel("Special Info:"));
        fieldPanel.add(specialInfoField);
        fieldPanel.add(new JLabel("Terms:"));
        fieldPanel.add(termsField);

        editRolesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEditRolesDialog();
            }
        });

        idField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String text = idField.getText();
                if (text.matches("\\d+") && text.length() == 9 && !sb.EC.validateId(Integer.parseInt(text))) {
                    idField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                    isValidId = true;
                    if(sb.EC.getEmployeeById(Integer.parseInt(text)).isDriver(new LinkedHashMap<>())){
                        isDriver = true;
                    }else{
                        isDriver = false;
                    }
                } else {
                    int thickness = 2; // Set the desired thickness of the border
                    idField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    isValidId = false;
                }
                updateSaveButtonState();
            }

            @Override
            public void focusLost(FocusEvent e) {
                String text = idField.getText();
                if (text.matches("\\d+") && text.length() == 9 && !sb.EC.validateId(Integer.parseInt(text))) {
                    idField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                    isValidId = true;
                    if(sb.EC.getEmployeeById(Integer.parseInt(text)).isDriver(new LinkedHashMap<>())){
                        isDriver = true;
                    }else{
                        isDriver = false;
                    }

                } else {
                    int thickness = 2; // Set the desired thickness of the border
                    idField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    isValidId = false;
                }
                updateSaveButtonState();
            }
        });


        fieldPanel.add(editRolesButton);

        panel.add(fieldPanel);
        return panel;
    }

    public void backButtonFunc() {
        new ManagerWindowUI();
    }

    private class TextFieldFocusListener extends FocusAdapter {
        @Override
        public void focusGained(FocusEvent e) {
            JTextField textField = (JTextField) e.getSource();
            //textField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
            String text = textField.getText();
            if (text.length() != 0) {
                textField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));

            } else {
                int thickness = 2; // Set the desired thickness of the border
                textField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
            }
            updateSaveButtonState();
        }

        @Override
        public void focusLost(FocusEvent e) {
            JTextField textField = (JTextField) e.getSource();
            String text = textField.getText().trim();
            boolean isValid = false;

            switch (textField.getName()) {
                case "idField":
                    isValid = validateId(text);
                    break;
                case "specialInfoField":
                case "termsField":
                    isValid = validateName(text);
                    break;
                case "bankInfoField":
                    isValid = validateBankInfo(text);
                    break;
                case "salaryField":
                    isValid = validateSalary(text);
                    break;
                // Add other fields validation if needed
            }

            if (!isValid) {
                int thickness = 2; // Set the desired thickness of the border
                textField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
            } else {
                textField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
            }

            updateSaveButtonState();
        }

        private boolean validateId(String id) {
            return id.matches("\\d+") && id.length() == 9 && !sb.EC.validateId(Integer.parseInt(id));
        }

        private boolean validateName(String name) {
            return true;
        }

        private boolean validateBankInfo(String info) {
            return info.matches("\\d+") || info.isEmpty();
        }

        private boolean validateSalary(String info) {
            return info.matches("\\d+") || info.isEmpty();
        }
    }


    private void showEditRolesDialog() {
        int id = Integer.parseInt(idField.getText());
        Employee employee = sb.EC.getEmployeeById(id);
        if (employee == null) {
            JOptionPane.showMessageDialog(null, "Employee not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> employeeRoles = employee.getRoles();

        JDialog dialog = new JDialog();
        dialog.setTitle("Edit Roles");
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());

        JPanel addRolesPanel = new JPanel();
        addRolesPanel.setLayout(new BoxLayout(addRolesPanel, BoxLayout.Y_AXIS));
        addRolesPanel.setBorder(BorderFactory.createTitledBorder("Add Roles"));
        addRolesPanel.setPreferredSize(new Dimension(200, 300));

        JCheckBox addShiftManagerCheckBox = new JCheckBox("Shift Manager");
        JCheckBox addCashierCheckBox = new JCheckBox("Cashier");
        JCheckBox addStorageCheckBox = new JCheckBox("Storage");
        JCheckBox addSecurityCheckBox = new JCheckBox("Security");
        JCheckBox addCleanerCheckBox = new JCheckBox("Cleaner");
        JCheckBox addOrganizerCheckBox = new JCheckBox("Organizer");
        JCheckBox addGeneralCheckBox = new JCheckBox("General");

        addRolesPanel.add(Box.createVerticalGlue());
        addRolesPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Only add roles that the employee doesn't already have
        if (!employeeRoles.contains("Shift Manager")) {
            addRolesPanel.add(addShiftManagerCheckBox);
        }
        if (!employeeRoles.contains("Cashier")) {
            addRolesPanel.add(addCashierCheckBox);
        }
        if (!employeeRoles.contains("Storage")) {
            addRolesPanel.add(addStorageCheckBox);
        }
        if (!employeeRoles.contains("Security")) {
            addRolesPanel.add(addSecurityCheckBox);
        }
        if (!employeeRoles.contains("Cleaner")) {
            addRolesPanel.add(addCleanerCheckBox);
        }
        if (!employeeRoles.contains("Organizer")) {
            addRolesPanel.add(addOrganizerCheckBox);
        }
        if (!employeeRoles.contains("General")) {
            addRolesPanel.add(addGeneralCheckBox);
        }

        addRolesPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel removeRolesPanel = new JPanel();
        removeRolesPanel.setLayout(new BoxLayout(removeRolesPanel, BoxLayout.Y_AXIS));
        removeRolesPanel.setBorder(BorderFactory.createTitledBorder("Remove Roles"));
        removeRolesPanel.setPreferredSize(new Dimension(200, 300));

        JCheckBox removeShiftManagerCheckBox = new JCheckBox("Shift Manager");
        JCheckBox removeCashierCheckBox = new JCheckBox("Cashier");
        JCheckBox removeStorageCheckBox = new JCheckBox("Storage");
        JCheckBox removeSecurityCheckBox = new JCheckBox("Security");
        JCheckBox removeCleanerCheckBox = new JCheckBox("Cleaner");
        JCheckBox removeOrganizerCheckBox = new JCheckBox("Organizer");
        JCheckBox removeGeneralCheckBox = new JCheckBox("General");

        removeRolesPanel.add(Box.createVerticalGlue());
        removeRolesPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Only add roles that the employee already has
        if (employeeRoles.contains("Shift Manager")) {
            removeRolesPanel.add(removeShiftManagerCheckBox);
        }
        if (employeeRoles.contains("Cashier")) {
            removeRolesPanel.add(removeCashierCheckBox);
        }
        if (employeeRoles.contains("Storage")) {
            removeRolesPanel.add(removeStorageCheckBox);
        }
        if (employeeRoles.contains("Security")) {
            removeRolesPanel.add(removeSecurityCheckBox);
        }
        if (employeeRoles.contains("Cleaner")) {
            removeRolesPanel.add(removeCleanerCheckBox);
        }
        if (employeeRoles.contains("Organizer")) {
            removeRolesPanel.add(removeOrganizerCheckBox);
        }
        if (employeeRoles.contains("General")) {
            removeRolesPanel.add(removeGeneralCheckBox);
        }

        removeRolesPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel buttonsPanel = new JPanel();
        JButton addButton = new JButton("Add Roles");
        JButton removeButton = new JButton("Remove Roles");
        JButton finishButton = new JButton("Finish");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add the selected roles to the employee
                List<String> roles = getSelectedAddRoles(addShiftManagerCheckBox, addCashierCheckBox, addStorageCheckBox,
                        addSecurityCheckBox, addCleanerCheckBox, addOrganizerCheckBox, addGeneralCheckBox);
                try {
                    for (String role:
                         roles) {
                        sb.EC.addEmployeeRole(id, role);
                    }
                    //sb.EC.addEmployeeRoles(id, roles);
                    JOptionPane.showMessageDialog(null, "Roles added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Remove the selected roles from the employee
                List<String> roles = getSelectedRemoveRoles(removeShiftManagerCheckBox, removeCashierCheckBox, removeStorageCheckBox,
                        removeSecurityCheckBox, removeCleanerCheckBox, removeOrganizerCheckBox, removeGeneralCheckBox);
                try {
                    for (String role:
                            roles) {
                        sb.EC.deleteEmployeeRoleS(id, role);
                    }
                        JOptionPane.showMessageDialog(null, "Roles removed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        buttonsPanel.add(addButton);
        buttonsPanel.add(removeButton);
        buttonsPanel.add(finishButton);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(addRolesPanel, BorderLayout.WEST);
        contentPanel.add(removeRolesPanel, BorderLayout.EAST);
        contentPanel.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }



    private void updateSaveButtonState() {
        boolean isBankInfoValid = salaryField.getText().matches("\\d+") || salaryField.getText().isEmpty();
        boolean isSalaryValid = salaryField.getText().matches("\\d+") || salaryField.getText().isEmpty();

        saveBtn.setEnabled(isValidId && isBankInfoValid && isSalaryValid);
        editRolesButton.setEnabled(isValidId && !isDriver);
    }


    @Override
    protected JButton createSaveButton() {
        JButton saveButton = super.createSaveButton();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(idField.getText());
                int salary = salaryField.getText().isEmpty()? -1:Integer.parseInt(salaryField.getText());
                int bankInfo = bankInfoField.getText().isEmpty()? -1:Integer.parseInt(bankInfoField.getText());
                String specialInfo = specialInfoField.getText().isEmpty()? "noChange":specialInfoField.getText();
                String terms = termsField.getText().isEmpty()? "noChange":termsField.getText();
                List<String> roles = getSelectedRoles();

                try {
                    if(salary!=-1) sb.EC.editEmployeeSalary(id, salary) ;
                    if(bankInfo!=-1) sb.EC.editEmployeeBankInfo(id, bankInfo);
                    if(!specialInfo.equals("noChange")) sb.EC.editEmployeeSpecialInfo(id, specialInfo);
                    if(!terms.equals("noChange")) sb.EC.addEmployeeTerms(id, terms);
                    //if(!roles.isEmpty()) sb.EC.getEmployeeById(id).
                    JOptionPane.showMessageDialog(null, "Employee edited successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                new ManagerWindowUI();
                dispose();
                // Add code to handle saving the edited employee
            }
        });
        return saveButton;
    }

    private List<String> getSelectedRemoveRoles(JCheckBox shiftManagerCheckBox, JCheckBox cashierCheckBox,
                                                JCheckBox storageCheckBox, JCheckBox securityCheckBox, JCheckBox cleanerCheckBox,
                                                JCheckBox organizerCheckBox, JCheckBox generalCheckBox) {
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

        return roles;
    }

    private List<String> getSelectedAddRoles(JCheckBox shiftManagerCheckBox, JCheckBox cashierCheckBox,
                                             JCheckBox storageCheckBox, JCheckBox securityCheckBox, JCheckBox cleanerCheckBox,
                                             JCheckBox organizerCheckBox, JCheckBox generalCheckBox) {
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

        return roles;
    }


    private List<String> getSelectedRoles() {
        List<String> roles = new LinkedList<>();
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
        return roles;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Assuming you have an instance of ShiftBoard and Scanner
                ShiftBoard sb = ShiftBoard.getInstance();
                Scanner scanner = new Scanner(System.in);
                new EditEmployeeUI(sb, scanner);
            }
        });
    }
}
