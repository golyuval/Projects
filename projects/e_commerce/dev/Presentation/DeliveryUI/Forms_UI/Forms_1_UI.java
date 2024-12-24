package Presentation.DeliveryUI.Forms_UI;

import defult.BusinessLayer.Controllers.Delivery.Forms_Controller;
import defult.BusinessLayer.DeliverySystem.Forms.DestForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class Forms_1_UI extends JFrame {

    JButton byID, allForms, exitButton;
    JTextField inputTextField;

    public Forms_1_UI() {

        // Set the frame to full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        // Create a panel with pink color
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.PINK);

        // Create title and align it center
        JLabel title = new JLabel("Destination Forms");
        title.setFont(new Font("Serif", Font.BOLD, 25));

        // Create buttons
        byID = new JButton("Destination by ID");
        allForms = new JButton("All destination forms");
        exitButton = new JButton("Exit");

        // Create the input text box
        inputTextField = new JTextField(10);

        // Set GridBagConstraints for the title
        GridBagConstraints gbcTitle = new GridBagConstraints();
        gbcTitle.gridx = 0;
        gbcTitle.gridy = 0;
        gbcTitle.gridwidth = 2;
        gbcTitle.insets = new Insets(100, 0, 50, 0); // Adjust the insets as desired
        gbcTitle.anchor = GridBagConstraints.CENTER;

        // Set GridBagConstraints for the input text box
        GridBagConstraints gbcInput = new GridBagConstraints();
        gbcInput.gridx = 1;
        gbcInput.gridy = 1;
        gbcInput.insets = new Insets(0, 10, 20, 0); // Adjust the insets as desired
        gbcInput.anchor = GridBagConstraints.LINE_END;

        // Set GridBagConstraints for the byID button
        GridBagConstraints gbcByID = new GridBagConstraints();
        gbcByID.gridx = 0;
        gbcByID.gridy = 1;
        gbcByID.insets = new Insets(0, 0, 20, 10); // Adjust the insets as desired
        gbcByID.anchor = GridBagConstraints.LINE_START;

        // Set GridBagConstraints for the allForms button
        GridBagConstraints gbcAllForms = new GridBagConstraints();
        gbcAllForms.gridx = 0;
        gbcAllForms.gridy = 2;
        gbcAllForms.gridwidth = 2;
        gbcAllForms.insets = new Insets(0, 0, 20, 0); // Adjust the insets as desired
        gbcAllForms.anchor = GridBagConstraints.CENTER;

        // Set GridBagConstraints for the exit button
        GridBagConstraints gbcExitButton = new GridBagConstraints();
        gbcExitButton.gridx = 0;
        gbcExitButton.gridy = 3;
        gbcExitButton.gridwidth = 2;
        gbcExitButton.anchor = GridBagConstraints.CENTER;

        // Add components to the main panel using GridBagConstraints
        mainPanel.add(title, gbcTitle);
        mainPanel.add(inputTextField, gbcInput);
        mainPanel.add(byID, gbcByID);
        mainPanel.add(allForms, gbcAllForms);
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

        // Set ActionListener for allForms button
        allForms.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayDestinationForms(Forms_Controller.getInstance().getDestForms());
            }
        });

        // Set ActionListener for byID button
        byID.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Retrieve the input from the text box
                String input = inputTextField.getText();

                // Convert the input to an integer (assuming it contains a valid integer)
                int givenID = Integer.parseInt(input);
                displayDestinationForms(Forms_Controller.getInstance().getDestFormByDeliveryID(givenID));
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void displayDestinationForms(LinkedList<DestForm> destForms) {
        // Check if the destForms list is empty
        if (destForms.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No destination forms found for the given ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a new window to display the table
        JFrame tableFrame = new JFrame("Destination Forms");
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableFrame.setLayout(new BorderLayout());

        // Create a table model
        String[] columnNames = {"Form", "Delivery", "Destination", "Items"};
        int rows = countRows(destForms);
        Object[][] data = new Object[rows][4];

        int rowIndex = 0;
        for (DestForm destForm : destForms) {
            String[] items = destForm.getIQ_string().split("\n");
            for (int i = 0; i < items.length; i++) {
                if (i == 0) {
                    data[rowIndex][0] = destForm.getDestFormID();
                    data[rowIndex][1] = destForm.getDelivery_ID();
                    data[rowIndex][2] = destForm.getSite().getSiteID();
                }
                data[rowIndex][3] = items[i];
                rowIndex++;
            }
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


    private int countRows(LinkedList<DestForm> destForms) {
        int count = 0;
        for (DestForm destForm : destForms) {
            String[] items = destForm.getIQ_string().split("\n");
            count += items.length;
        }
        return count;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Forms_1_UI::new);
    }
}
