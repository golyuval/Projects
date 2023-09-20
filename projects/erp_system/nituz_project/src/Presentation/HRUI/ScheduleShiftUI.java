package Presentation.HRUI;

import Presentation.HRUI.Models.EmployeeUI;
import Presentation.HRUI.Models.RegEmployeeUI;
import Presentation.HRUI.templates.BaseWindowUI;
import defult.BusinessLayer.Controllers.HR.ShiftBoard;
import defult.BusinessLayer.HRsystem.DeliveryTuple;
import defult.BusinessLayer.HRsystem.Employee;
import defult.BusinessLayer.HRsystem.Role;
//import defult.BusinessLayer.HRsystem.*;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
//import java.awt.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import static defult.BusinessLayer.HRsystem.Role.*;
import static defult.ServiceLayer.HR.ManagerHRService.unAvailableEmployee;

public class ScheduleShiftUI extends BaseWindowUI {
    private JTextField dateField;
    private JRadioButton dayShiftRadioButton;
    private JRadioButton nightShiftRadioButton;
    private JTextField branchIdField;
    private JTextField startShiftField;
    private JTextField endShiftField;
    private JButton selectEmployeesButton;
    private JButton savButton;
    Map<Employee, String> stuff = new HashMap<>();

    public ScheduleShiftUI() {
        super("Schedule Shift", false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    @Override
    protected JPanel createContentPanel() {
        JPanel panel = createGridPanel(2);

        JLabel dateLabel = new JLabel("Enter Date (dd/MM/yyyy):");
        dateField = new JTextField();

        JLabel shiftTypeLabel = new JLabel("Select Shift Type:");
        JPanel shiftTypePanel = createGridPanel(2);
        dayShiftRadioButton = new JRadioButton("Day Shift");
        nightShiftRadioButton = new JRadioButton("Night Shift");
        ButtonGroup shiftTypeGroup = new ButtonGroup();
        shiftTypeGroup.add(dayShiftRadioButton);
        shiftTypeGroup.add(nightShiftRadioButton);
        shiftTypePanel.add(dayShiftRadioButton);
        shiftTypePanel.add(nightShiftRadioButton);

        JLabel branchIdLabel = new JLabel("Enter Branch ID:");
        branchIdField = new JTextField();

        JLabel startShiftLabel = new JLabel("Enter Shift Start Time (HH:mm):");
        startShiftField = new JTextField();

        JLabel endShiftLabel = new JLabel("Enter Shift End Time (HH:mm):");
        endShiftField = new JTextField();

        panel.add(dateLabel);
        panel.add(dateField);
        panel.add(shiftTypeLabel);
        panel.add(shiftTypePanel);
        panel.add(branchIdLabel);
        panel.add(branchIdField);
        panel.add(startShiftLabel);
        panel.add(startShiftField);
        panel.add(endShiftLabel);
        panel.add(endShiftField);

        selectEmployeesButton = new JButton("Select Employees");
        savButton = new JButton("Save");
        savButton.setEnabled(false);
        selectEmployeesButton.setEnabled(false); // Disable the button initially


        addButton(selectEmployeesButton);
        addButton(savButton);

        savButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate date = LocalDate.parse(dateField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                boolean type = dayShiftRadioButton.isSelected();
                LocalTime start = LocalTime.parse(startShiftField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime end = LocalTime.parse(endShiftField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
                int branchId = Integer.parseInt(branchIdField.getText());

                try {
                    ShiftBoard.getInstance().validateSchedulePossible(date, type, branchId);
                    ShiftBoard.getInstance().SC.createShift(date, type, stuff, branchId, start, end, false);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        selectEmployeesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                LocalDate date = LocalDate.parse(dateField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                boolean type = dayShiftRadioButton.isSelected();
                LocalTime start = LocalTime.parse(startShiftField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime end = LocalTime.parse(endShiftField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
                int branchId = Integer.parseInt(branchIdField.getText());

                List<DeliveryTuple> deliveriesOnShiftS = ShiftBoard.getInstance().isDeliveryOnShift(date, start, end, branchId, true);


                Map<Employee, List<String>> allAvailable = ShiftBoard.getInstance().EC.whoIsAvailable(date, type);
                try {
                    ShiftBoard.getInstance().validateSchedulePossible(date, type, branchId);

//                    List<DeliveryTuple> deliveriesOnShiftD = ShiftBoard.getInstance().isDeliveryOnShift(date, start, end, branchId, false);

                    Map<Employee, List<String>> managers = ShiftBoard.getInstance().AvailableManagers(allAvailable);

                    // Create a list of EmployeeUI objects for managers
                    List<EmployeeUI> managerUIList = new ArrayList<>();
                    for (Employee manager : managers.keySet()) {
                        EmployeeUI managerUI = new RegEmployeeUI(manager);
                        managerUIList.add(managerUI);
                    }

                    // Create a selection dialog for managers
                    List<EmployeeUI> selectedEmployees  = showEmployeeSelectionDialog("Select Manager", managerUIList, true);
                    for (EmployeeUI emp:
                         selectedEmployees) {
                        unAvailableEmployee(emp.getEmployee(), allAvailable, stuff, SHIFT_MANAGER.getRoleName(), date, type, ShiftBoard.getInstance(), branchId);
                    }


                        if (deliveriesOnShiftS.isEmpty()) {
                            try {
                                Map<Employee, List<String>> storages = ShiftBoard.getInstance().AvailableStorage(allAvailable);

// Create a list of EmployeeUI objects for storages
                                List<EmployeeUI> storageUIList = new ArrayList<>();
                                for (Employee storage : storages.keySet()) {
                                    EmployeeUI storageUI = new RegEmployeeUI(storage);
                                    storageUIList.add(storageUI);
                                }
// Create a selection dialog for storages
                                if (!storages.isEmpty()) {
                                    selectedEmployees = showEmployeeSelectionDialog("Select Storage", storageUIList, false);
                                    for (EmployeeUI emp : selectedEmployees) {
                                        unAvailableEmployee(emp.getEmployee(), allAvailable, stuff, STORAGE.getRoleName(), date, type, ShiftBoard.getInstance(), branchId);
                                    }
                                }
                            }catch (Exception ex){}
// Handle the selected storages
                        } else {
                            Map<Employee, List<String>> storages = ShiftBoard.getInstance().AvailableStorage(allAvailable);

// Create a list of EmployeeUI objects for storages
                            List<EmployeeUI> storageUIList = new ArrayList<>();
                            for (Employee storage : storages.keySet()) {
                                EmployeeUI storageUI = new RegEmployeeUI(storage);
                                storageUIList.add(storageUI);
                            }
                            selectedEmployees = showEmployeeSelectionDialog("Select Storage", storageUIList, true);
                            for (EmployeeUI emp : selectedEmployees) {
                                unAvailableEmployee(emp.getEmployee(), allAvailable, stuff, STORAGE.getRoleName(), date, type, ShiftBoard.getInstance(), branchId);
                            }


                        }

                        try {
                            Map<Employee, List<String>> securities = ShiftBoard.getInstance().AvailableSecurity(allAvailable);

// Create a list of EmployeeUI objects for securities
                            List<EmployeeUI> securityUIList = new ArrayList<>();
                            if (!securities.isEmpty()) {
                                for (Employee security : securities.keySet()) {
                                    EmployeeUI securityUI = new RegEmployeeUI(security);
                                    securityUIList.add(securityUI);
                                }

// Create a selection dialog for securities
                                selectedEmployees = showEmployeeSelectionDialog("Select Security", securityUIList, false);
                                for (EmployeeUI emp : selectedEmployees) {
                                    unAvailableEmployee(emp.getEmployee(), allAvailable, stuff, SECURITY.getRoleName(), date, type, ShiftBoard.getInstance(), branchId);
                                }
                            }
// Handle the selected securities
                        }catch (Exception ex){}

                        try {
                            Map<Employee, List<String>> cleaners = ShiftBoard.getInstance().AvailableCleaner(allAvailable);

// Create a list of EmployeeUI objects for cleaners
                            List<EmployeeUI> cleanerUIList = new ArrayList<>();
                            if (!cleaners.isEmpty()) {
                                for (Employee cleaner : cleaners.keySet()) {
                                    EmployeeUI cleanerUI = new RegEmployeeUI(cleaner);
                                    cleanerUIList.add(cleanerUI);
                                }

// Create a selection dialog for cleaners
                                selectedEmployees = showEmployeeSelectionDialog("Select Cleaner", cleanerUIList, false);
                                for (EmployeeUI emp : selectedEmployees) {
                                    unAvailableEmployee(emp.getEmployee(), allAvailable, stuff, CLEANER.getRoleName(), date, type, ShiftBoard.getInstance(), branchId);
                                }
                            }
// Handle the selected cleaners
                        }catch (Exception ex){}

                        try{
                            Map<Employee, List<String>> organizers = ShiftBoard.getInstance().AvailableOrganizer(allAvailable);

    // Create a list of EmployeeUI objects for organizers
                            List<EmployeeUI> organizerUIList = new ArrayList<>();
                            if (!organizers.isEmpty()) {

                                for (Employee organizer : organizers.keySet()) {
                                    EmployeeUI organizerUI = new RegEmployeeUI(organizer);
                                    organizerUIList.add(organizerUI);
                                }

    // Create a selection dialog for organizers
                                selectedEmployees = showEmployeeSelectionDialog("Select Organizer", organizerUIList, false);
                                for (EmployeeUI emp : selectedEmployees) {
                                    unAvailableEmployee(emp.getEmployee(), allAvailable, stuff, ORGANIZER.getRoleName(), date, type, ShiftBoard.getInstance(), branchId);
                                }
                            }
                        }catch (Exception ex){}
//// Handle the selected organizers
//
//                    Map<Driver, List<String>> drivers = ShiftBoard.getInstance().AvailableDrivers(allAvailable, dt.getLicense());
//
//// Create a list of EmployeeUI objects for drivers
//                    List<EmployeeUI> driverUIList = new ArrayList<>();
//                    for (Driver driver : drivers.keySet()) {
//                        EmployeeUI driverUI = new RegEmployeeUI(driver);
//                        driverUIList.add(driverUI);
//                    }
//
//// Create a selection dialog for drivers
//                    selectedEmployees = showEmployeeSelectionDialog("Select Driver", driverUIList, false);
//                    for (EmployeeUI emp : selectedEmployees) {
//                        unAvailableEmployee(emp.getEmployee(), allAvailable, stuff, DRIVER.getRoleName(), date, type, ShiftBoard.getInstance(), branchId);
//                        ShiftBoard.getInstance().connectDriverToDelivery((Driver) emp.getEmployee(), dt.getDeliveryId());
//                    }
//// Handle the selected drivers
                        try{
                            Map<Employee, List<String>> generals = ShiftBoard.getInstance().AvailableGeneral(allAvailable);

    // Create a list of EmployeeUI objects for generals
                            List<EmployeeUI> generalUIList = new ArrayList<>();
                            if (!generals.isEmpty()) {

                                for (Employee general : generals.keySet()) {
                                    EmployeeUI generalUI = new RegEmployeeUI(general);
                                    generalUIList.add(generalUI);
                                }

    // Create a selection dialog for generals
                                selectedEmployees = showEmployeeSelectionDialog("Select General", generalUIList, false);
                                for (EmployeeUI emp : selectedEmployees) {
                                    unAvailableEmployee(emp.getEmployee(), allAvailable, stuff, GENERAL.getRoleName(), date, type, ShiftBoard.getInstance(), branchId);
                                }
                            }
                        }catch (Exception ex){}

                    ShiftBoard.getInstance().SC.createShift(date, type, stuff, branchId, start, end, !deliveriesOnShiftS.isEmpty() );
// Create a custom UIManager with pink background color
                    UIManager.put("OptionPane.background", new ColorUIResource(Color.PINK));

// Show dialog: shift created successfully
                    JOptionPane.showMessageDialog(null, "Shift created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {

                    // Create a custom panel with pink background color
                    JPanel panel = new JPanel();
                    panel.setBackground(Color.PINK);

                    // Create a label for the error message
                    JLabel errorMessage = new JLabel("An error occurred: " + ex.getMessage());

                    // Add the label to the custom panel
                    panel.add(errorMessage);

                    // Show error message dialog with custom panel
                    JOptionPane.showMessageDialog(null, panel, "Error", JOptionPane.ERROR_MESSAGE);
//                    ex.printStackTrace();
                }
                new ManagerWindowUI();
                dispose();
            }
        });



        // Add field constraints using FocusListener
        dateField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                // Validate dateField when focus is lost
                if (!isValidDate(dateField.getText())) {
                    dateField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2));
                } else {
                    dateField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                }
                enableSelectEmployeesButton();

            }
        });

        branchIdField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                // Validate branchIdField when focus is lost
                if (!isValidBranchId(branchIdField.getText())) {
                    branchIdField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2));
                } else {
                    branchIdField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                }
            }
        });

        startShiftField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                // Validate startShiftField when focus is lost
                if (!isValidTime(startShiftField.getText())) {
                    startShiftField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2));
                } else {
                    startShiftField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                }
                enableSelectEmployeesButton();

            }
        });

        endShiftField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                // Validate endShiftField when focus is lost
                if (!isValidTime(endShiftField.getText())) {
                    endShiftField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2));
                } else {
                    endShiftField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                }
                enableSelectEmployeesButton();

            }
        });

        panel.add(selectEmployeesButton);
        panel.add(savButton);
        return panel;
    }

    private boolean isValidDateFormat(String date) {
        String pattern = "^(0[1-9]|1\\d|2\\d|3[01])/(0[1-9]|1[0-2])/\\d{4}$";
        return date.matches(pattern);
    }

    private boolean isValidDate(String input) {
        // Add your date validation logic here
        // For example, you can use SimpleDateFormat to parse the input and check if it's a valid date
        return isValidDateFormat(input);
    }

    private boolean isValidBranchId(String input) {
        // Check if the branch ID is within the valid range of 2-10
        try {
            int branchId = Integer.parseInt(input);
            return branchId >= 2 && branchId <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private void enableSelectEmployeesButton() {
        // Enable selectEmployeesButton if all fields are valid
        if (isValidDate(dateField.getText()) && isValidBranchId(branchIdField.getText()) &&
                isValidTime(startShiftField.getText()) && isValidTime(endShiftField.getText())) {
            selectEmployeesButton.setEnabled(true);
        } else {
            selectEmployeesButton.setEnabled(false);
        }
        enableSaveButton();
    }

    private void enableSaveButton() {
        // Enable saveButton if the stuff map is not empty
        if (!stuff.isEmpty()) {
            savButton.setEnabled(true);
        } else {
            savButton.setEnabled(false);
        }
    }

    public void backButtonFunc() {
        new ManagerWindowUI();
    }

    private List<EmployeeUI> showEmployeeSelectionDialog(String title, List<EmployeeUI> options, boolean single) {
        // Create a panel to hold the components
        JPanel panel = new JPanel(new BorderLayout());
        JPanel managerPanel = new JPanel(new GridLayout(options.size(), 1));
        JPanel checkboxPanel = new JPanel(new GridLayout(options.size(), 1));

        // Add manager UIs and checkboxes to the panels
        for (EmployeeUI option : options) {
            JCheckBox checkBox = new JCheckBox(String.valueOf(option.getEmployee().getId()));
            checkBox.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (single) {
                        // Uncheck other checkboxes if single selection mode
                        for (Component comp : checkboxPanel.getComponents()) {
                            if (comp instanceof JCheckBox && comp != checkBox) {
                                ((JCheckBox) comp).setSelected(false);
                            }
                        }
                    }
                    option.setSelected(true);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    option.setSelected(false);
                }
            });
            managerPanel.add(option);
            checkboxPanel.add(checkBox);
        }

        // Add the panels to the main panel
        panel.add(managerPanel, BorderLayout.WEST);
        panel.add(checkboxPanel, BorderLayout.EAST);

        // Show the dialog and get the user's selection
        int option = JOptionPane.showOptionDialog(null, panel, title, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        List<EmployeeUI> ret = new LinkedList<>();
        // Handle the selected option
        if (option == JOptionPane.OK_OPTION) {
            // Find the selected manager UI
            for (EmployeeUI managerUI : options) {
                if (managerUI.isSelected()) {
                    ret.add(managerUI);
                }
            }
        }

        return ret;
    }



    private boolean isValidTime(String input) {
        // Check if the time is in the format hh:mm
        String timePattern = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";
        return input.matches(timePattern);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ScheduleShiftUI();
            }
        });
    }
}
