package Presentation.DeliveryUI.Forms_UI;

import defult.BusinessLayer.Controllers.Delivery.Forms_Controller;
import defult.BusinessLayer.DeliverySystem.Forms.Delivery;
import defult.BusinessLayer.DeliverySystem.Structures.Order;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class Forms_2_UI extends JFrame {

    JButton byID, scheduled, progress, done, exitButton;
    JTextField inputTextField;

    public Forms_2_UI() {

        // Set the frame to full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        // Create a panel with pink color
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.PINK);

        // Create title and align it center
        JLabel title = new JLabel("Deliveries");
        title.setFont(new Font("Serif", Font.BOLD, 25));

        // Create buttons
        byID = new JButton("Delivery by ID");
        scheduled = new JButton("All scheduled deliveries");
        progress = new JButton("All progress deliveries");
        done = new JButton("All done deliveries");
        exitButton = new JButton("Exit");

        // Create the input text box
        inputTextField = new JTextField(10);

        // Set GridBagConstraints for the title
        GridBagConstraints gbcTitle = new GridBagConstraints();
        gbcTitle.gridx = 1;
        gbcTitle.gridy = 0;
        gbcTitle.gridwidth = 2;
        gbcTitle.insets = new Insets(100, 0, 50, 0); // Adjust the insets as desired
        gbcTitle.anchor = GridBagConstraints.CENTER;

        // Set GridBagConstraints for the input text box
        GridBagConstraints gbcInput = new GridBagConstraints();
        gbcInput.gridx = 2; // Updated
        gbcInput.gridy = 1;
        gbcInput.gridwidth = 1; // Updated
        gbcInput.insets = new Insets(0, 0, 20, 20);
        gbcInput.anchor = GridBagConstraints.LINE_START;

    // Set GridBagConstraints for the byID button
        GridBagConstraints gbcByID = new GridBagConstraints();
        gbcByID.gridx = 1; // Updated
        gbcByID.gridy = 1;
        gbcByID.gridwidth = 1; // Updated
        gbcByID.insets = new Insets(0, 0, 20, 20);
        //gbcByID.anchor = GridBagConstraints.LINE_END;
        gbcByID.anchor = GridBagConstraints.LINE_START;

        // Set GridBagConstraints for the scheduled button
        GridBagConstraints gbcScheduled = new GridBagConstraints();
        gbcScheduled.gridx = 1;
        gbcScheduled.gridy = 2;
        gbcScheduled.gridwidth = 2;
        gbcScheduled.insets = new Insets(0, 0, 20, 0); // Adjust the insets as desired
        gbcScheduled.anchor = GridBagConstraints.CENTER;

        // Set GridBagConstraints for the progress button
        GridBagConstraints gbcProgress = new GridBagConstraints();
        gbcProgress.gridx = 1;
        gbcProgress.gridy = 3;
        gbcProgress.gridwidth = 2;
        gbcProgress.insets = new Insets(0, 0, 20, 0); // Adjust the insets as desired
        gbcProgress.anchor = GridBagConstraints.CENTER;

        // Set GridBagConstraints for the done button
        GridBagConstraints gbcDone = new GridBagConstraints();
        gbcDone.gridx = 1;
        gbcDone.gridy = 4;
        gbcDone.gridwidth = 2;
        gbcDone.insets = new Insets(0, 0, 20, 0); // Adjust the insets as desired
        gbcDone.anchor = GridBagConstraints.CENTER;

        // Set GridBagConstraints for the exit button
        GridBagConstraints gbcExitButton = new GridBagConstraints();
        gbcExitButton.gridx = 1;
        gbcExitButton.gridy = 5;
        gbcExitButton.gridwidth = 2;
        gbcExitButton.anchor = GridBagConstraints.CENTER;

        // Add components to the main panel using GridBagConstraints
        mainPanel.add(title, gbcTitle);
        mainPanel.add(inputTextField, gbcInput);
        mainPanel.add(byID, gbcByID);
        mainPanel.add(scheduled, gbcScheduled);
        mainPanel.add(progress, gbcProgress);
        mainPanel.add(done, gbcDone);
        mainPanel.add(exitButton, gbcExitButton);

        // Add mainPanel to the frame
        add(mainPanel);

        // Set ActionListener for exit button
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                FormsMenuUI formsMenu = new FormsMenuUI();
                formsMenu.setVisible(true);
            }
        });

        // Set ActionListener for scheduled button
        scheduled.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayDestinationForms(Forms_Controller.getInstance().getScheduledDeliveries());
            }
        });

        // Set ActionListener for progress button
        progress.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayDestinationForms(Forms_Controller.getInstance().getInProgressDeliveries());
            }
        });

        // Set ActionListener for done button
        done.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayDestinationForms(Forms_Controller.getInstance().getShippedDeliveries());
            }
        });

        // Set ActionListener for byID button
        byID.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Retrieve the input from the text box
                String input = inputTextField.getText();

                // Convert the input to an integer (assuming it contains a valid integer)
                int givenID = Integer.parseInt(input);
                LinkedList<Delivery> del = new LinkedList<Delivery>();
                Delivery d = Forms_Controller.getInstance().getDeliveryByID(givenID);
                if (d != null)
                    del.add(d);
                displayDestinationForms(del);
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void displayDestinationForms(LinkedList<Delivery> deliveries) {
        // Check if the deliveries list is empty
        if (deliveries.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No delivery was found for the given ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a new window to display the table
        JFrame tableFrame = new JFrame("Destination Forms");
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableFrame.setLayout(new BorderLayout());

        // Create a table model
        String[] columnNames = {"Delivery", "Date", "Time", "Driver", "Overloads", "Order ID", "Supplier", "Branch", "Items"};
        int rows = countRows(deliveries);
        Object[][] data = new Object[rows][9];

        int rowIndex = 0;
        for (Delivery delivery : deliveries) {
            data[rowIndex][0] = delivery.get_delivery_id();
            data[rowIndex][1] = delivery.getDeparture_date();
            data[rowIndex][2] = delivery.get_arrival_time();
            data[rowIndex][3] = delivery.get_driver_name();
            data[rowIndex][4] = delivery.s_overloads();

            for (Order order : delivery.get_orders()) {
                data[rowIndex][5] = order.getOrderID();
                data[rowIndex][6] = order.get_supplier().getSiteName();
                data[rowIndex][7] = order.get_branch().getSiteName();

                String[] items = order.getIQ_string().split("\n");
                for (String item : items) {
                    data[rowIndex][8] = item;
                    rowIndex++;
                }
            }
        }

        JTable table = new JTable(data, columnNames) {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return getPreferredSize().width < getParent().getWidth();
            }
        };
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(8).setPreferredWidth(300); // Adjust the width as needed

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
        tableFrame.setLocationRelativeTo(null); // Center the frame on the screen
        tableFrame.setVisible(true);
    }

    private int countRows(LinkedList<Delivery> deliveries) {
        int count = 0;
        for (Delivery delivery : deliveries) {
            for (Order order : delivery.get_orders()) {
                String[] items = order.getIQ_string().split("\n");
                count += items.length;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Forms_1_UI::new);
    }
}
