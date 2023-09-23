package Presentation.DeliveryUI.Manager_UI;

import defult.BusinessLayer.DeliverySystem.Forms.Delivery;
import defult.ServiceLayer.Delivery.FormService;
import defult.ServiceLayer.Delivery.TruckService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SendDelivery_M_UI extends JFrame {

    JButton exitButton;

    public SendDelivery_M_UI() {

        // Set the frame to full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        // Create a panel with pink color
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.PINK);

        // Create title and align it center
        JLabel title = new JLabel("Manager Menu");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Serif", Font.BOLD, 25));

        // Create a panel for the subframe
        JPanel subframePanel = new JPanel(new BorderLayout());
        subframePanel.setBackground(Color.PINK);

        // Create a table model to hold the data
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Delivery ID");
        tableModel.addColumn("Supplier");
        tableModel.addColumn("Branch");
        tableModel.addColumn("Date");
        tableModel.addColumn("Items");
        tableModel.addColumn("Status");

        // Add rows to the table model (sample data)
        FormService fs = FormService.getInstance();
        for(Delivery d:fs.fc.getScheduledDeliveries())
            tableModel.addRow(new Object[]{d.get_delivery_id(),d.get_orders().get(0).get_supplier().getSiteName(),
                    d.get_orders().get(0).get_branch().getSiteName(),d.getDeparture_date(),d.get_orders().get(0).getIQ_string(),"<html><font color='red'><b>scheduled</b></font></html>"});

        // Create a table with the table model
        JTable table = new JTable(tableModel);

        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the scroll pane to the subframe panel
        subframePanel.add(scrollPane, BorderLayout.CENTER);

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Color.PINK);

        // Create "Send Delivery" button
        JButton sendDeliveryButton = new JButton("Send Delivery");
        sendDeliveryButton.setPreferredSize(new Dimension(200, 50));

        // Add action listener to the send delivery button
        sendDeliveryButton.addActionListener(e -> {
            // Get the selected row from the table
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                // Get the delivery ID from the selected row and invoke sendDelivery method
                int id = (int) table.getValueAt(selectedRow, 0);
                sendDelivery(id);

                // Set the value of the "Status" column to "In Progress" in green bold font
                table.setValueAt("<html><font color='green'><b>In Progress</b></font></html>", selectedRow, 5);
            } else {
                JOptionPane.showMessageDialog(SendDelivery_M_UI.this,
                        "Please select a delivery from the table.",
                        "No Delivery Selected",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        // Create exit button
        exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(200, 50));

        // Add action listener to the exit button
        exitButton.addActionListener(e -> {
            dispose(); // Close the current window
            new ManagerUI(); // Open the DeliverySystemUI window
        });

        // Add components to the button panel
        buttonPanel.add(sendDeliveryButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Adds spacing between buttons
        buttonPanel.add(exitButton);

        // Add components to the main panel
        mainPanel.add(Box.createVerticalGlue());  // Adds vertical glue for centering
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalGlue());  // Adds vertical glue for centering
        mainPanel.add(subframePanel);
        mainPanel.add(Box.createVerticalGlue());  // Adds vertical glue for centering
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalGlue());  // Adds vertical glue for centering

        // Add mainPanel to the frame
        add(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void sendDelivery(int id) {
        FormService _fs = FormService.getInstance();
        _fs.fc.sendDelivery(id);
        int truckID = _fs.fc.getDeliveryByID(id).get_truck_ID();
        TruckService.getInstance().getTruckController().truckGoesOnDelivery(truckID);

        System.out.println("Sending delivery with ID: " + id);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SendDelivery_M_UI::new);
    }
}
