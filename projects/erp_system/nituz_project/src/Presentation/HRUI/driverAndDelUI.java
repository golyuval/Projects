package Presentation.HRUI;

import Presentation.HRUI.Models.DriverUI;
import defult.BusinessLayer.Controllers.HR.ShiftBoard;
import defult.BusinessLayer.HRsystem.DeliveryTuple;
import defult.BusinessLayer.HRsystem.Driver;
import defult.BusinessLayer.HRsystem.Employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static defult.BusinessLayer.HRsystem.Role.DRIVER;
import static defult.ServiceLayer.HR.EmployeeService.removeSubmission;
import static defult.ServiceLayer.HR.HRService.validateIntInput;
import static defult.ServiceLayer.HR.ManagerHRService.unAvailableEmployee;

public class driverAndDelUI extends JFrame {
    private ShiftBoard sb;
    private Scanner s;
    private JTextField choiceField;
    private JButton removeButton;
    private JButton cancelButton;

    public driverAndDelUI(ShiftBoard sb, Scanner s) {
        this.sb = sb;
        this.s = s;

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.PINK);

        JLabel title = new JLabel("Choose driver to delivery");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Serif", Font.BOLD, 25));
        title.setForeground(Color.MAGENTA);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBackground(Color.PINK);
        Map<Integer, DeliveryTuple> deliveries = new LinkedHashMap<>();
        StringBuilder options = new StringBuilder();
        LocalDate now = LocalDate.now();
        LocalTime dayStart = LocalTime.of(6, 0);
        LocalTime dayEnd = LocalTime.of(16, 0);
        LocalTime nightStart = LocalTime.of(16, 0);
        LocalTime nightEnd = LocalTime.of(23, 59);
        int j = 1;
        for (int i = 0; i < 6; i++) {
            Map<Employee, List<String>> allAvailableMorning = sb.EC.whoIsAvailable(now.plusDays(i), false);
            Map<Employee, List<String>> allAvailableNight = sb.EC.whoIsAvailable(now.plusDays(i), true);

            List<DeliveryTuple> deliveriesOnShiftMorning = sb.isDeliveryOnShift(now.plusDays(i), dayStart, dayEnd, -1, false);
            List<DeliveryTuple> deliveriesOnShiftNight = sb.isDeliveryOnShift(now.plusDays(i), nightStart, nightEnd, -1, false);

            for (DeliveryTuple dt : deliveriesOnShiftMorning) {
                try {
                    if (sb.DC.getDeliveryByID(dt.getDeliveryId()).get_driverId() == -1) {
                        String delivery = sb.DC.getDeliveryByID(dt.getDeliveryId()).toStringForDrivers();
                        options.append(j).append(". ").append(delivery).append("\n");
                        deliveries.put(j, dt);
                        j++;
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            for (DeliveryTuple dt : deliveriesOnShiftNight) {
                try {
                    if (sb.DC.getDeliveryByID(dt.getDeliveryId()).get_driverId() == -1) {
                        String delivery = sb.DC.getDeliveryByID(dt.getDeliveryId()).toStringForDrivers();
                        options.append(j).append(". ").append(delivery).append("\n");
                        deliveries.put(j, dt);
                        j++;
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        JTextArea shiftsTextArea = new JTextArea(options.toString());
        shiftsTextArea.setEditable(false);
        shiftsTextArea.setFont(new Font("Serif", Font.PLAIN, 15));
        shiftsTextArea.setLineWrap(true);
        shiftsTextArea.setWrapStyleWord(true);
        shiftsTextArea.setBackground(Color.PINK);
        JScrollPane shiftsScrollPane = new JScrollPane(shiftsTextArea);
        shiftsScrollPane.setPreferredSize(new Dimension(300, 100));
        optionsPanel.add(shiftsScrollPane);

        JLabel choiceLabel = new JLabel("Choose delivery to schedule:");
        choiceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        choiceLabel.setForeground(Color.MAGENTA);
        choiceField = new JTextField(5);
        choiceField.setMaximumSize(choiceField.getPreferredSize());
        choiceField.setHorizontalAlignment(JTextField.LEFT);

        JPanel choicePanel = new JPanel();
        choicePanel.setLayout(new BoxLayout(choicePanel, BoxLayout.X_AXIS));
        choicePanel.setBackground(Color.PINK);
        choicePanel.add(choiceLabel);
        choicePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        choicePanel.add(choiceField);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.setBackground(Color.PINK);

        removeButton = new JButton("Choose");
        removeButton.setForeground(Color.MAGENTA);
        removeButton.setPreferredSize(new Dimension(100, 30));
        removeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (choiceField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter an option number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int choice = Integer.parseInt(choiceField.getText());
                if (deliveries.containsKey(choice)) {
                    DeliveryTuple deliveryTuple = deliveries.get(choice);
                    Map<Driver, List<String>> drivers = null;
                    try {
                        if (deliveryTuple.getDepratureTime().isBefore(LocalDateTime.of(deliveryTuple.getDepratureTime().toLocalDate(), LocalTime.of(16, 0))))
                            drivers = sb.AvailableDrivers(sb.EC.whoIsAvailable(deliveryTuple.getDepratureTime().toLocalDate(), false), deliveryTuple.getLicense());
                        if (deliveryTuple.getDepratureTime().isAfter(LocalDateTime.of(deliveryTuple.getDepratureTime().toLocalDate(), LocalTime.of(16, 0))))
                            drivers = sb.AvailableDrivers(sb.EC.whoIsAvailable(deliveryTuple.getDepratureTime().toLocalDate(), true), deliveryTuple.getLicense());
                    } catch (Exception exception) {
                        // Handle the exception if necessary
                    }

                    if (drivers != null) {
                        Driver selectedDriver = showDialogAndGetSelectedDriver(drivers, deliveryTuple);
                        if (selectedDriver != null) {
                            // Logic for handling the selected driver
                            sb.connectDriverToDelivery(selectedDriver, deliveryTuple.getDeliveryId());
                            unAvailableEmployee(selectedDriver, new LinkedHashMap<>(), new LinkedHashMap<>(), "DRIVER", deliveryTuple.getDepratureTime().toLocalDate(), !deliveryTuple.getDepratureTime().toLocalTime().isBefore(LocalTime.of(16,0)), sb, deliveryTuple.getBranchId());
                            // Display a dialog with the selected driver's details using DriverUI
                            JOptionPane.showMessageDialog(null, new DriverUI(selectedDriver), "Selected Driver successfully", JOptionPane.PLAIN_MESSAGE);
                        } else {
                            // Logic for no driver selected
                            JOptionPane.showMessageDialog(null, "No driver selected.", "No Driver", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Logic for no available drivers
                        JOptionPane.showMessageDialog(null, "No available drivers.", "No Drivers", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid option number. Please enter a valid option number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(100, 30));
        cancelButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        cancelButton.setForeground(Color.MAGENTA);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                driverAndDelUI window = new driverAndDelUI(sb, s);
                window.setVisible(true);
            }
        });

        buttonsPanel.add(removeButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonsPanel.add(cancelButton);

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(optionsPanel);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(choicePanel);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(buttonsPanel);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
        pack();
        setVisible(true);
    }

    private Driver showDialogAndGetSelectedDriver(Map<Driver, List<String>> drivers, DeliveryTuple deliveryTuple) {
        // Create the dialog
        final Driver[] driverRet = {null};

        JDialog dialog = new JDialog(this, "Select Driver", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Create the main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.PINK);

        // Create the top panel for showing DriverUI for each available driver
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.PINK);
        JScrollPane scrollPane = new JScrollPane(topPanel);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        // Create the bottom panel for radio buttons with driver IDs
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.PINK);

        // Add components to the main panel
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Create the group for radio buttons
        ButtonGroup buttonGroup = new ButtonGroup();

        // Create DriverUI for each available driver and add it to the top panel
        for (Driver driver : drivers.keySet()) {
            DriverUI driverUI = new DriverUI(driver);
            driverUI.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    buttonGroup.clearSelection(); // Clear the selection of radio buttons
                    driverUI.setSelected(true); // Set the driverUI as selected
                }
            });
            topPanel.add(driverUI);
            topPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            // Create a radio button with driver ID and add it to the bottom panel
            JRadioButton radioButton = new JRadioButton(String.valueOf(driver.getId()));
            radioButton.setBackground(Color.PINK);
            radioButton.setActionCommand(String.valueOf(driver.getId()));
            buttonGroup.add(radioButton);
            bottomPanel.add(radioButton);
            bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        }
        // Create the select button
        final JButton[] selectButton = {new JButton("Select")};
        selectButton[0].setForeground(Color.MAGENTA);
        selectButton[0].setPreferredSize(new Dimension(100, 30));
        selectButton[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected radio button's action command (driver ID)
                String selectedDriverId ="none";
                selectedDriverId = buttonGroup.getSelection().getActionCommand();

                // Find the selected driver from the map using the driver ID
                for (Driver driver : drivers.keySet()) {
                    if (String.valueOf(driver.getId()).equals(selectedDriverId)) {
                        dialog.dispose();
                        handleSelectedDriver(driver, deliveryTuple);
                        driverRet[0] = driver;
                        return;
                    }
                }

//                // Handle the case when no driver is selected
//                if(selectedDriverId.equals("none"))
//                    handleNoDriverSelected();
            }
        });

        // Add select button to the bottom panel
        bottomPanel.add(selectButton[0]);

        // Set the main panel as the content pane of the dialog
        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);

        // Return null if no driver is selected
        return driverRet[0];
    }

    private void handleSelectedDriver(Driver driver, DeliveryTuple deliveryTuple) {
        // TODO: Implement the logic for the case when a driver is selected
//        System.out.println("Selected Driver: " + driver.getName());
//        ShiftBoard.getInstance().connectDriverToDelivery(driver, deliveryTuple.getDeliveryId());
//        JOptionPane.showMessageDialog(null, new DriverUI(driver), "Selected Driver", JOptionPane.PLAIN_MESSAGE);

    }

//    private void handleNoDriverSelected() {
//        // TODO: Implement the logic for the case when no driver is selected
//        JOptionPane.showMessageDialog(null, "No available drivers.", "No Drivers", JOptionPane.ERROR_MESSAGE);
//    }
}
