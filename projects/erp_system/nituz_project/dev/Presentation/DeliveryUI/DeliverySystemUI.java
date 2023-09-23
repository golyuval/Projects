package Presentation.DeliveryUI;

import Presentation.DeliveryUI.Forms_UI.FormsMenuUI;
import Presentation.DeliveryUI.Trucks_UI.TruckMenuUI;
import Presentation.MainUI;
import Presentation.DeliveryUI.Manager_UI.ManagerUI;
import defult.BusinessLayer.Controllers.Delivery.Delivery_Controller;
import defult.BusinessLayer.DeliverySystem.Structures.Order;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class DeliverySystemUI extends JFrame {
    private JPanel mainPanel;
    private JButton[] buttons;
    private final String[] buttonNames = {"Truck menu", "Forms menu", "Schedule new delivery", "Show pending orders", "Manager menu", "Exit to Main Menu"};

    public void disableButtons()
    {
        for(JButton b:buttons)
            b.setEnabled(false);
    }

    public void enableButtons()
    {
        for(JButton b:buttons)
            b.setEnabled(true);
    }

    public DeliverySystemUI() {
        // Set the frame properties
        this.setTitle("Delivery System");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //this.setUndecorated(true); // Remove window decorations
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the main panel and set the layout
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(buttonNames.length + 1, 1));
        mainPanel.setBackground(Color.PINK);

        // Add the title
        JLabel titleLabel = new JLabel("Delivery System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        mainPanel.add(titleLabel);

        // Create the buttons and their panels
        buttons = new JButton[buttonNames.length];
        for(int i=0; i<buttons.length; i++) {
            buttons[i] = new JButton(buttonNames[i]);
            buttons[i].setPreferredSize(new Dimension(200, 50));

            // Create a panel for each button
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setBackground(Color.PINK);
            buttonPanel.add(buttons[i]);

            // Add the panel to the main panel
            mainPanel.add(buttonPanel);
        }

        // Add action listeners to the buttons
        buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new TruckMenuUI(); // Open the TruckMenuUI window
            }
        });

        buttons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new FormsMenuUI(); // Open the FormsMenuUI window
            }
        });

        buttons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new ScheduleDeliveryUI(); // Open the ScheduleDeliveryUI window
            }
        });

        buttons[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayPendingOrders();
            }
        });

        DeliverySystemUI frame = this;

        buttons[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current window
                //dispose();
                frame.disableButtons();


                // Create and show the ManagerUI window
                JFrame managerWindow = new JFrame("Manager Menu");
              //  managerWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
               // managerWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the current window

                // Create components for password input
                JLabel passwordLabel = new JLabel("Please enter manager password:");
                JTextField passwordField = new JTextField(20);
                JButton submitButton = new JButton("Submit");
                JButton backButton = new JButton("Back");

                // Add action listener to the submit button
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Check if the entered password is correct
                        String enteredPassword = passwordField.getText();
                        if (enteredPassword.equals("123123")) {
                            frame.dispose();
                            managerWindow.dispose(); // Close the ManagerUI window

                            new ManagerUI(); // Open the ManagerMenuUI window
                        } else {
                            // Display error message in a new window
                            JOptionPane.showMessageDialog(managerWindow, "The password is wrong, bitch", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        frame.enableButtons();
                    }
                });

                // Add action listener to the back button
                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        managerWindow.dispose(); // Close the ManagerUI window
                        frame.enableButtons();
                    }
                });

                // Set layout for the manager window
                managerWindow.setLayout(new BorderLayout());
                JPanel centerPanel = new JPanel(new FlowLayout());
                centerPanel.add(passwordLabel);
                centerPanel.add(passwordField);
                centerPanel.add(submitButton);
                centerPanel.add(backButton);
                managerWindow.add(centerPanel, BorderLayout.CENTER);

                // Pack and display the manager window
                managerWindow.pack();
                managerWindow.setLocationRelativeTo(null); // Center the window on the screen
                managerWindow.setVisible(true);
            }
        });
        buttons[5].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new MainUI(); // Open the MainUI window
            }
        });

        // Add the main panel to the frame
        this.add(mainPanel);

        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DeliverySystemUI::new);
    }

    public void displayPendingOrders() {
        Delivery_Controller _dc = Delivery_Controller.getInstance();
        LinkedList<Order> requestedOrders = _dc.get_requestedOrders();

        // Create a new window to display the table
        JFrame tableFrame = new JFrame("Pending Orders");
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableFrame.setLayout(new BorderLayout());

        // Create a table model
        String[] columnNames = {"Order ID", "Supplier", "Branch", "Items"};
        Object[][] data = new Object[requestedOrders.size()][4];

        for (int i = 0; i < requestedOrders.size(); i++) {
            Order order = requestedOrders.get(i);
            data[i][0] = order.getDo_id();
            data[i][1] = order.get_supplier().getSiteName();
            data[i][2] = order.get_branch().getSiteName();
            data[i][3] = order.get_order().toString();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the table to the frame
        tableFrame.add(scrollPane, BorderLayout.CENTER);

        // Create an exit button
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableFrame.dispose(); // Close the table window
            }
        });

        // Add the exit button to the frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exitButton);
        tableFrame.add(buttonPanel, BorderLayout.SOUTH);

        tableFrame.pack();
        tableFrame.setVisible(true);
    }

}
