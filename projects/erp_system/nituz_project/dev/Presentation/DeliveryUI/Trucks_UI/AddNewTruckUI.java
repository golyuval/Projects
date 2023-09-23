package Presentation.DeliveryUI.Trucks_UI;

import defult.BusinessLayer.Controllers.Delivery.Truck_Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AddNewTruckUI extends JFrame {
    private JPanel mainPanel;
    private JTextField licencesField;
    private JTextField coolingField;
    private JTextField maxWeightField;

    public AddNewTruckUI() {

        // Set the frame to full screen
        this.setResizable(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);

        // Set the frame properties
        this.setTitle("Add New Truck");
        this.setSize(500, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the main panel and set the layout
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 1));
        mainPanel.setBackground(Color.PINK);


        // Add text fields and labels
        licencesField = addTextField("Enter truck's licences (comma-separated): ", 200);
        coolingField = addTextField("Enter truck's cooling capability: ", 200);
        maxWeightField = addTextField("Enter truck's max weight: ", 200);

        // Create submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(100, 50));
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateLicences(licencesField.getText())) {
                    createNewTruck();

                    // Return to TruckMenuUI with a message
                    dispose();
                    TruckMenuUI.showMessageAndOpen("New truck added successfully!",TruckMenuUI::new);
                } else {
                    JOptionPane.showMessageDialog(null, "Licences are not OK!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Create exit button
        JButton exitButton = new JButton("Exit");
        submitButton.setPreferredSize(new Dimension(100, 50));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new TruckMenuUI(); // Open the TruckMenuUI window
            }
        });

        mainPanel.add(submitButton);
        mainPanel.add(exitButton);

        // Add the main panel to the frame
        this.add(mainPanel);
        this.setLocationRelativeTo(null);  // Center the frame

        this.setVisible(true);
    }

    // Helper method to create a labeled text field and add it to the main panel
    private JTextField addTextField(String labelText, int width) {
        JLabel label = new JLabel(labelText, SwingConstants.CENTER);
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(width, 20));
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        fieldPanel.setBackground(Color.PINK);
        fieldPanel.add(label);
        fieldPanel.add(textField);
        mainPanel.add(fieldPanel);
        return textField;
    }

    // Method to validate the licences
    private boolean validateLicences(String licences) {
        List<String> validLicences = Arrays.asList("A1", "B1", "C1");
        List<String> enteredLicences = Arrays.asList(licences.split(","));

        for (String licence : enteredLicences) {
            licence = licence.trim();  // Remove leading and trailing whitespace
            if (!validLicences.contains(licence)) {
                return false;
            }
        }

        return true;
    }

    // Method to be implemented with your business logic for creating and storing a new truck
    private void createNewTruck() {
        Truck_Controller _tc= Truck_Controller.getInstance();
        String[] licencesArray = licencesField.getText().split(",");
        LinkedList<String> licences = new LinkedList<>();
        for(String s: licencesArray)
            licences.add(s);
        Double coolingCapability = Double.parseDouble(coolingField.getText());
        Double maxWeight = Double.parseDouble(maxWeightField.getText());

        _tc.createTruck(500, maxWeight, coolingCapability, licences);
        // Here, add your code to create and store a new truck using these values
    }
}
