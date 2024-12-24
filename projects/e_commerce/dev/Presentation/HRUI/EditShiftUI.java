package Presentation.HRUI;

import Presentation.HRUI.Models.EmployeeUI;
import Presentation.HRUI.Models.RegEmployeeUI;
import Presentation.HRUI.Models.ShiftModelUI;
import Presentation.HRUI.templates.BaseWindowUI;
import defult.BusinessLayer.Controllers.HR.ShiftBoard;
import defult.BusinessLayer.HRsystem.DeliveryTuple;
import defult.BusinessLayer.HRsystem.Employee;
import defult.BusinessLayer.HRsystem.Shift;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static defult.BusinessLayer.HRsystem.Role.*;
import static defult.BusinessLayer.HRsystem.Role.GENERAL;
import static defult.ServiceLayer.HR.ManagerHRService.unAvailableEmployee;


public class EditShiftUI extends BaseWindowUI {
    private JComboBox<String> shiftComboBox;
    private JButton addButton;
    private JButton removeButton;
    private JButton doneButton;
    private ShiftModelUI selectedShiftUI;
    private JLabel warningLabel;

    ActionListener oldActionListenerAdd = null;
    ActionListener oldActionListenerRem = null;

    public EditShiftUI() {
        super("Edit Shift", false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

    }

    @Override
    protected JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.PINK);
        // Create a panel to display the list of ShiftModelUI
        JPanel shiftPanel = new JPanel();
        shiftPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        shiftPanel.setBackground(Color.WHITE);

        // Get the list of available shifts
        List<Shift> shifts = ShiftBoard.getInstance().SC.getShifts();

        // Add ShiftModelUI objects to the shiftPanel
        for (Shift shift : shifts) {
            ShiftModelUI shiftUI = new ShiftModelUI(shift);
            shiftUI.setPreferredSize(new Dimension(150, 120)); // Set the preferred size for ShiftModelUI
            shiftPanel.add(shiftUI);

            for (Shift shifto : shifts) {
                ShiftModelUI shiftUIo = new ShiftModelUI(shifto);
                shiftUIo.setPreferredSize(new Dimension(150, 120));

                shiftUI.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (selectedShiftUI != null) {
                            selectedShiftUI.setSelected(false); // Deselect the previously selected shift
                        }
                        selectedShiftUI = shiftUI;
                        selectedShiftUI.setSelected(true); // Select the clicked shift
                    }
                });

                shiftUI.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (selectedShiftUI != null) {
                            selectedShiftUI.setSelected(false); // Deselect the previously selected shift
                        }
                        selectedShiftUI = shiftUI;
                        selectedShiftUI.setSelected(true); // Select the clicked shift

                        // Enable/disable the buttons based on the selection
                        addButton.setEnabled(true);
                        removeButton.setEnabled(true);

                        if(oldActionListenerAdd == null) {

                            oldActionListenerAdd = new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    employeeChoosing();
                                    // Add code to handle back button click and return to the main menu
                                }
                            };

                            addButton.addActionListener(oldActionListenerAdd);
                        }



                        if(oldActionListenerRem == null) {

                            oldActionListenerRem = new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    employeeRemoving();
                                    // Add code to handle back button click and return to the main menu
                                }
                            };

                            removeButton.addActionListener(oldActionListenerRem);
                        }
                    }
                });

                shiftPanel.add(shiftUI);
            }


        }

        // Create a scroll pane and add the shiftPanel to it
        JScrollPane scrollPane = new JScrollPane(shiftPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panel.add(scrollPane);
        panel.add(Box.createRigidArea(new Dimension(0,20)));

        addButton = new JButton("Add Employee");
        addButton(addButton, panel);

        removeButton = new JButton("Remove Employee");
        addButton(removeButton, panel);

        addButton.setEnabled(false);
        removeButton.setEnabled(false);


        doneButton = new JButton("Done");
        addButton(doneButton, panel);
        warningLabel = new JLabel("If no available employees, the Add button will not work");
        warningLabel.setForeground(Color.RED);
        warningLabel.setVisible(true);
        panel.add(warningLabel);




        return panel;
    }
    public void backButtonFunc() {
        new ManagerWindowUI();
    }
    private void deselectShift() {
        if (selectedShiftUI != null) {
            selectedShiftUI.setSelected(false);
            selectedShiftUI = null;
        }
        addButton.setEnabled(false);
        removeButton.setEnabled(false);
    }

    void employeeRemoving() {
        Shift shift = selectedShiftUI.getShift();
//        ShiftBoard.getInstance().SC.getShiftByDateType(shift.getDate(), shift.isShiftType(), shift.getBranch());
        LocalDate date = shift.getDate();
        LocalTime end = shift.getShiftEnd();
        LocalTime start = shift.getShiftStart();
        int branchId = shift.getBranch();
        boolean type = shift.isShiftType();
        Map<Employee, String> stuff = shift.getFinalEmployees();
        stuff.keySet().removeIf((e) -> shift.getFinalEmployees().get(e)==SHIFT_MANAGER.getRoleName());
        List<DeliveryTuple> deliveriesOnShiftS = ShiftBoard.getInstance().isDeliveryOnShift(date, start, end, branchId, true);

        // Create a list of EmployeeUI objects for the employees
        List<EmployeeUI> employeeUIList = new ArrayList<>();

        for (Employee employee : stuff.keySet()) {
            EmployeeUI employeeUI = new RegEmployeeUI(employee);
            employeeUIList.add(employeeUI);
        }

        // Create a selection dialog for employees
        List<EmployeeUI> selectedEmployees = showEmployeeSelectionDialog("Select Employee", employeeUIList, false);

        // Handle the selected employees
        for (EmployeeUI employeeUI : selectedEmployees) {
            Employee selectedEmployee = employeeUI.getEmployee();
            String role = stuff.get(selectedEmployee);
            ShiftBoard.getInstance().removeEmployeeFromShift(selectedEmployee, ShiftBoard.getInstance().SC.getShiftByDateType(date, type, branchId));
//            shift.removeFromFinalEmployees(selectedEmployee);
//            shiftV.removeFromFinalEmployees(selectedEmployee);
        }
    }

//    private List<EmployeeUI> showEmployeeSelectionDialog2(String title, List<EmployeeUI> options, boolean single) {
//        // Create the dialog
//        JDialog dialog = new JDialog();
//        dialog.setTitle(title);
//        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//        dialog.setLayout(new BorderLayout());
//
//        // Create the panel to hold the components
//        JPanel panel = new JPanel(new BorderLayout());
//
//        // Create the manager panel and checkbox panel
//        JPanel managerPanel = new JPanel(new GridLayout(options.size(), 1));
//        JPanel checkboxPanel = new JPanel(new GridLayout(options.size(), 1));
//
//        // Create the "OK" button
//        JButton okButton = new JButton("OK");
//        okButton.setEnabled(false); // Disable the button initially
//
//        // Create a list to hold the checkboxes
//        List<JCheckBox> checkboxes = new ArrayList<>();
//
//        // Add manager UIs and checkboxes to the panels
//        for (EmployeeUI option : options) {
//            JCheckBox checkBox = new JCheckBox(String.valueOf(option.getEmployee().getId()));
//            checkBox.addItemListener(e -> {
//                boolean selectionMade = false;
//                for (JCheckBox checkbox : checkboxes) {
//                    if (checkbox.isSelected()) {
//                        selectionMade = true;
//                        break;
//                    }
//                }
//                okButton.setEnabled(selectionMade);
//            });
//            checkboxes.add(checkBox);
//            managerPanel.add(option);
//            checkboxPanel.add(checkBox);
//        }
//
//        // Add the panels to the main panel
//        panel.add(managerPanel, BorderLayout.WEST);
//        panel.add(checkboxPanel, BorderLayout.EAST);
//
//        // Add the "OK" button to the panel
//        panel.add(okButton, BorderLayout.SOUTH);
//
//        // Add an ActionListener to the "OK" button
//        okButton.addActionListener(e -> {
//            dialog.dispose(); // Close the dialog
//        });
//
//        // Add the panel to the dialog
//        dialog.add(panel);
//
//        // Set the size and make the dialog visible
//        dialog.setSize(400, 300);
//        dialog.setVisible(true);
//
//        List<EmployeeUI> ret = new LinkedList<>();
//        // Handle the selected options
//        for (int i = 0; i < checkboxes.size(); i++) {
//            if (checkboxes.get(i).isSelected()) {
//                ret.add(options.get(i));
//            }
//        }
//
//        return ret;
//    }




    void employeeChoosing(){
        Shift shift = selectedShiftUI.getShift();
        LocalDate date = shift.getDate();
        LocalTime end = shift.getShiftEnd();
        LocalTime start = shift.getShiftStart();
        int branchId = shift.getBranch();
        boolean type = shift.isShiftType();
        Map<Employee, String> stuff = shift.getFinalEmployees();
        int stuffLen = stuff.size();
        Map<Employee, List<String>> allAvailable = ShiftBoard.getInstance().EC.whoIsAvailable(date, type);
        try {

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
                        List<EmployeeUI> selectedEmployees = showEmployeeSelectionDialog("Select Storage", storageUIList, false);
                        for (EmployeeUI emp : selectedEmployees) {
                            unAvailableEmployee(emp.getEmployee(), allAvailable, stuff, STORAGE.getRoleName(), date, type, ShiftBoard.getInstance(), branchId);
                        }
                    }
                }catch (Exception ex){}
// Handle the selected storages
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
                    List<EmployeeUI> selectedEmployees = showEmployeeSelectionDialog("Select Security", securityUIList, false);
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
                    List<EmployeeUI> selectedEmployees = showEmployeeSelectionDialog("Select Cleaner", cleanerUIList, false);
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
                    List<EmployeeUI> selectedEmployees = showEmployeeSelectionDialog("Select Organizer", organizerUIList, false);
                    for (EmployeeUI emp : selectedEmployees) {
                        unAvailableEmployee(emp.getEmployee(), allAvailable, stuff, ORGANIZER.getRoleName(), date, type, ShiftBoard.getInstance(), branchId);
                    }
                }
            }catch (Exception ex){}

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
                    List<EmployeeUI> selectedEmployees = showEmployeeSelectionDialog("Select General", generalUIList, false);
                    for (EmployeeUI emp : selectedEmployees) {
                        unAvailableEmployee(emp.getEmployee(), allAvailable, stuff, GENERAL.getRoleName(), date, type, ShiftBoard.getInstance(), branchId);
                    }
                }
            }catch (Exception ex){}
            // Create a custom UIManager with pink background color
            UIManager.put("OptionPane.background", new ColorUIResource(Color.PINK));
// Show dialog: shift created successfully
            if(stuffLen!=stuff.size())
                JOptionPane.showMessageDialog(null, "employees added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

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


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EditShiftUI();
            }
        });
    }
}
