package Presentation.DeliveryUI;

import Presentation.DeliveryUI.DeliverySystemUI;
import defult.BusinessLayer.Controllers.Delivery.Delivery_Controller;
import defult.BusinessLayer.DeliverySystem.Structures.Order;
import defult.ServiceLayer.Delivery.TruckService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScheduleDeliveryUI extends JFrame {
    private static final Set<String> validLicenses = new HashSet<>(Arrays.asList("A1", "B1", "C1"));
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final Pattern timePattern = Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");

    JTextField tempTextField, licenseTextField, destTextField, dateTextField, departureTextField, arrivalTextField, weightTextField;
    JButton scheduleButton, exitButton;

    public ScheduleDeliveryUI() {

        // Set the frame to full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setUndecorated(true);

        // Create a panel with pink color
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.PINK);

        // Create title and align it center
        JLabel title = new JLabel("Schedule Delivery");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Serif", Font.BOLD, 25));

        // Create labels and text fields
        JLabel tempLabel = new JLabel("Temperature:");
        tempTextField = new JTextField(10);
        tempTextField.setFont(tempTextField.getFont().deriveFont(21f));

        JLabel licenseLabel = new JLabel("Driver's License:");
        licenseTextField = new JTextField(10);
        licenseTextField.setFont(licenseTextField.getFont().deriveFont(21f));

        JLabel destLabel = new JLabel("Destination:");
        destTextField = new JTextField(10);
        destTextField.setFont(destTextField.getFont().deriveFont(21f));

        JLabel dateLabel = new JLabel("Departure Date:");
        dateTextField = new JTextField(10);
        dateTextField.setFont(dateTextField.getFont().deriveFont(21f));

        JLabel departureLabel = new JLabel("Departure Time:");
        departureTextField = new JTextField(10);
        departureTextField.setFont(departureTextField.getFont().deriveFont(21f));

        JLabel arrivalLabel = new JLabel("Arrival Time:");
        arrivalTextField = new JTextField(10);
        arrivalTextField.setFont(arrivalTextField.getFont().deriveFont(21f));

        JLabel weightLabel = new JLabel("Truck's Weight:");
        weightTextField = new JTextField(10);
        weightTextField.setFont(weightTextField.getFont().deriveFont(21f));

        // Create buttons
        scheduleButton = new JButton("Schedule Delivery");
        exitButton = new JButton("Exit");

        // Set button sizes
        scheduleButton.setPreferredSize(new Dimension(300, 50));
        exitButton.setPreferredSize(new Dimension(300, 50));

        scheduleButton.setMaximumSize(scheduleButton.getPreferredSize());
        exitButton.setMaximumSize(exitButton.getPreferredSize());

        // Center align the buttons
        scheduleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add action listeners to buttons
        scheduleButton.addActionListener(e -> {
            // TODO: Replace with your logic to handle the scheduled delivery
            try {
                // Check if temperature is a valid double
                Double.parseDouble(tempTextField.getText());

                // Check if license is valid
                if (!validLicenses.contains(licenseTextField.getText())) {
                    throw new IllegalArgumentException("Invalid Driver's License");
                }

                // Check if destination is an integer between 1-9
                int destination = Integer.parseInt(destTextField.getText());
                if (destination < 1 || destination > 9) {
                    throw new IllegalArgumentException("Invalid Destination");
                }

                // Check if date is in valid format
                dateFormat.setLenient(false);
                dateFormat.parse(dateTextField.getText());

                // Check if departureTime and arrivalTime are in valid format
                Matcher departureMatcher = timePattern.matcher(departureTextField.getText());
                Matcher arrivalMatcher = timePattern.matcher(arrivalTextField.getText());

                if (!departureMatcher.matches() || !arrivalMatcher.matches()) {
                    throw new IllegalArgumentException("Invalid Time Format");
                }

                // Check if truck weight is a valid double
                Double.parseDouble(weightTextField.getText());

                // If all validations pass, you can proceed with the scheduling
                scheduleDelivery();
            } catch (Exception ex) {
                // pop up warning message
                JOptionPane.showMessageDialog(null, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new DeliverySystemUI(); // Open the DeliverySystemUI window
            }
        });

        // Create input panel and align its components center
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBackground(Color.PINK);
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create panels for each input field and align them center
        JPanel tempPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tempPanel.setBackground(Color.PINK);
        tempPanel.add(tempLabel);
        tempPanel.add(tempTextField);
        inputPanel.add(tempPanel);

        JPanel licensePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        licensePanel.setBackground(Color.PINK);
        licensePanel.add(licenseLabel);
        licensePanel.add(licenseTextField);
        inputPanel.add(licensePanel);

        JPanel destPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        destPanel.setBackground(Color.PINK);
        destPanel.add(destLabel);
        destPanel.add(destTextField);
        inputPanel.add(destPanel);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        datePanel.setBackground(Color.PINK);
        datePanel.add(dateLabel);
        datePanel.add(dateTextField);
        inputPanel.add(datePanel);

        JPanel departurePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        departurePanel.setBackground(Color.PINK);
        departurePanel.add(departureLabel);
        departurePanel.add(departureTextField);
        inputPanel.add(departurePanel);

        JPanel arrivalTPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        arrivalTPanel.setBackground(Color.PINK);
        arrivalTPanel.add(arrivalLabel);
        arrivalTPanel.add(arrivalTextField);
        inputPanel.add(arrivalTPanel);

        JPanel weightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        weightPanel.setBackground(Color.PINK);
        weightPanel.add(weightLabel);
        weightPanel.add(weightTextField);
        inputPanel.add(weightPanel);

        // Add components to the main panel
        mainPanel.add(Box.createRigidArea(new Dimension(0, 100)));  // Creates empty space
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));  // Creates empty space
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));  // Creates empty space
        mainPanel.add(scheduleButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));  // Creates empty space
        mainPanel.add(exitButton);

        // Add mainPanel to the frame
        this.add(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void scheduleDelivery () {
        int current_ID = -1;
        Delivery_Controller _dc = Delivery_Controller.getInstance();
        TruckService _ts = TruckService.getInstance();
        LinkedList<Order> orders = _dc.gatherOrdersToDelivery();
        double temp = Double.parseDouble(tempTextField.getText());
        String licence = licenseTextField.getText().strip();
        int truckID = _ts.getTruckController().getNewTruck(temp, licence);
        double truckWeight = Double.parseDouble(weightTextField.getText());
        LinkedList<Integer> overloads = new LinkedList<>();
        LocalDate currD = LocalDate.parse(dateTextField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalTime currT = LocalTime.parse(departureTextField.getText());
        LocalTime arrivalT = LocalTime.parse(arrivalTextField.getText());
        try {
            current_ID = _dc.tryScheduleDeliveryForUI(  orders, truckID, truckWeight, overloads,
                    currD, currT,arrivalT);
            if (current_ID  == -1) {
                JFrame errorFrame = new JFrame();
                errorFrame.setSize(500, 300);
                errorFrame.setLocationRelativeTo(null); // center the window
                errorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel errorPanel = new JPanel();
                errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));
                errorPanel.setBackground(Color.PINK);

                // Create title and align it center
                JLabel titleLabel = new JLabel("<html><div style='text-align: center;'>The selected truck weigh more than it's capability, please choose a solution</div></html>");
                titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                titleLabel.setFont(new Font("Serif", Font.BOLD, 18));

                JButton changeTruckButton = new JButton("Change truck");
                JButton removeProductButton = new JButton("Remove a product");
                JButton removeDestinationButton = new JButton("Remove a destination");

                // Set button sizes
                changeTruckButton.setPreferredSize(new Dimension(300, 50));
                removeProductButton.setPreferredSize(new Dimension(300, 50));
                removeDestinationButton.setPreferredSize(new Dimension(300, 50));

                changeTruckButton.setMaximumSize(changeTruckButton.getPreferredSize());
                removeProductButton.setMaximumSize(removeProductButton.getPreferredSize());
                removeDestinationButton.setMaximumSize(removeDestinationButton.getPreferredSize());

                // Center align the buttons
                changeTruckButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                removeProductButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                removeDestinationButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                //action buttons:
                changeTruckButton.addActionListener(e -> {
                    if (!_ts.getTruckController().canReplaceTruck(truckWeight, temp, licence))
                        JOptionPane.showMessageDialog(null, "Changing truck is not available, the order weigh too much", "Error", JOptionPane.ERROR_MESSAGE);
                    else {
                        overloads.add(_dc.recordOverLoad(1));
                        _dc.handleOption1(truckWeight, temp, licence, orders, overloads, currD, currT,arrivalT);
                        errorFrame.dispose();
                        dispose();
                        DeliverySystemUI dui = new DeliverySystemUI();
                        dui.setVisible(true);
                    }
                });

                removeProductButton.addActionListener(e -> {
                    JFrame ordersFrame = new JFrame();
                    ordersFrame.setSize(600, 400);
                    ordersFrame.setLocationRelativeTo(null); // center the window
                    ordersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    // Convert the orders into a 2D array for the JTable
                    String[][] data = new String[orders.size()][];
                    for (int i = 0; i < orders.size(); i++) {
                        Order curr = orders.get(i);
                        data[i] = new String[]{
                                "Order ID: " + curr.getDo_id(),
                                curr.get_supplier().getSiteName() + " (supplier)",
                                curr.get_branch().getSiteName() + " (branch)",
                                curr.get_order().toString() // assuming this returns the product name
                        };
                    }

                    // Column names for the table
                    String[] columnNames = {"Order ID", "Supplier", "Branch", "Products in the order"};

                    JTable ordersTable = new JTable(data, columnNames);
                    JScrollPane ordersScroll = new JScrollPane(ordersTable);

                    // TextField for user to enter product name
                    JTextField productField = new JTextField("Insert product name here");
                    productField.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked ( MouseEvent e ) {
                            productField.setText("");
                        }
                    });

                    // OK and Cancel Buttons
                    JButton okButton = new JButton("OK");
                    JButton cancelButton = new JButton("Cancel");

                    okButton.addActionListener(okEvent -> {
                        String productName = productField.getText().toLowerCase();
                        boolean isProductInTable = false;

                        // check if product is in the table
                        for (int i = 0; i < ordersTable.getRowCount(); i++) {
                            if (ordersTable.getValueAt(i, 3).toString().toLowerCase().contains(" " + productName + " ")) {
                                isProductInTable = true;
                                break;
                            }
                        }

                        if (!isProductInTable) {
                            JOptionPane.showMessageDialog(null, "Choose an item from the list", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            try {
                                overloads.add(_dc.recordOverLoad(2));
                                _dc.handleOption2(productName, orders);
                            } catch (NoSuchElementException ex) {
                                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }

                            // Close the window
                            ordersFrame.dispose();

                        }
                    });
                    cancelButton.addActionListener(cancelEvent -> {
                        ordersFrame.dispose();
                    });

                    JPanel bottomPanel = new JPanel();
                    bottomPanel.add(productField);
                    bottomPanel.add(okButton);
                    bottomPanel.add(cancelButton);

                    ordersFrame.add(ordersScroll, BorderLayout.CENTER);
                    ordersFrame.add(bottomPanel, BorderLayout.PAGE_END);

                    ordersFrame.setVisible(true);
                });

                removeDestinationButton.addActionListener(e -> {
                    JFrame ordersFrame = new JFrame();
                    ordersFrame.setSize(600, 400);
                    ordersFrame.setLocationRelativeTo(null); // center the window
                    ordersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    // Convert the orders into a 2D array for the JTable
                    String[][] data = new String[orders.size()][];
                    for (int i = 0; i < orders.size(); i++) {
                        Order curr = orders.get(i);
                        data[i] = new String[]{
                                "Order ID: " + curr.getDo_id(),
                                curr.get_supplier().getSiteName() + " (supplier)",
                                curr.get_branch().getSiteName() + " (branch)",
                                curr.get_order().toString() // this should return the product name
                        };
                    }

                    // Column names for the table
                    String[] columnNames = {"Order ID", "Supplier", "Branch", "Products in the order"};

                    JTable ordersTable = new JTable(data, columnNames);
                    JScrollPane ordersScroll = new JScrollPane(ordersTable);

                    // TextField for user to enter branch name
                    JTextField branchField = new JTextField("Insert branch name here");
                    branchField.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked ( MouseEvent e ) {
                            branchField.setText("");
                        }
                    });

                    // OK and Cancel Buttons
                    JButton okButton = new JButton("OK");
                    JButton cancelButton = new JButton("Cancel");

                    okButton.addActionListener(okEvent -> {
                        String branchName = branchField.getText().toLowerCase();
                        boolean isBranchInTable = false;

                        // check if branch is in the table
                        for (int i = 0; i < ordersTable.getRowCount(); i++) {
                            if (ordersTable.getValueAt(i, 2).toString().toLowerCase().contains(branchName.toLowerCase())) {
                                isBranchInTable = true;
                                break;
                            }
                        }

                        if (!isBranchInTable) {
                            JOptionPane.showMessageDialog(null, "Choose a branch from the list", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            try {
                                overloads.add(_dc.recordOverLoad(3));
                                _dc.handleOption3(branchName, orders);
                            } catch (NoSuchElementException ex) {
                                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }

                            // Close the window
                            ordersFrame.dispose();
                        }
                    });

                    cancelButton.addActionListener(cancelEvent -> {
                        ordersFrame.dispose();
                    });

                    JPanel bottomPanel = new JPanel();
                    bottomPanel.add(branchField);
                    bottomPanel.add(okButton);
                    bottomPanel.add(cancelButton);

                    ordersFrame.add(ordersScroll, BorderLayout.CENTER);
                    ordersFrame.add(bottomPanel, BorderLayout.PAGE_END);

                    ordersFrame.setVisible(true);
                });



                // Add components to the panel
                errorPanel.add(Box.createRigidArea(new Dimension(0, 20)));  // Creates empty space
                errorPanel.add(titleLabel);
                errorPanel.add(Box.createRigidArea(new Dimension(0, 40)));  // Creates double space
                errorPanel.add(changeTruckButton);
                errorPanel.add(Box.createRigidArea(new Dimension(0, 20)));  // Creates empty space
                errorPanel.add(removeProductButton);
                errorPanel.add(Box.createRigidArea(new Dimension(0, 20)));  // Creates empty space
                errorPanel.add(removeDestinationButton);

                errorFrame.getContentPane().add(errorPanel);
                errorFrame.setVisible(true);
            }
            else
            {
                int branchID = Integer.parseInt(destTextField.getText());
                _dc.saveDelivery(currD,currT,arrivalT,branchID,licence, current_ID);
                dispose(); // Close the current window
                new DeliverySystemUI(); // Open the DeliverySystemUI window
            }

        }
        catch (HeadlessException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ScheduleDeliveryUI());
    }
}
