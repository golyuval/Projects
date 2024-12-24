package Presentation.DeliveryUI.Trucks_UI;

import Presentation.DeliveryUI.DeliverySystemUI;
import defult.BusinessLayer.Controllers.Delivery.Truck_Controller;
import defult.BusinessLayer.DeliverySystem.Structures.Truck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TruckMenuUI extends JFrame {

    private static JLabel messageLabel;

    private JPanel mainPanel;
    private JButton[] buttons;
    private final String[] buttonNames = {"Add Truck", "Display Available Trucks", "Display Unavailable Trucks", "Exit to Delivery Menu"};

    public TruckMenuUI() {
        
        // Set the frame to full screen
        this.setResizable(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);


        // Create the main panel and set the layout
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.PINK);

        // Create title and align it center
        JLabel title = new JLabel("Truck Menu");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Serif", Font.BOLD, 24));


        // Add space before the title
        mainPanel.add(Box.createVerticalGlue());
        // Add the title to the main panel
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 200))); // Create 30 of space between each button


        // Create a new panel for the buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(Color.PINK);

        // Create the buttons and add them to the buttons panel
        buttons = new JButton[buttonNames.length];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(buttonNames[i]);
            buttons[i].setPreferredSize(new Dimension(200, 50));

            // Create a panel for each button
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setBackground(Color.PINK);
            buttonPanel.add(buttons[i]);

            // Add the panel to the main panel
            mainPanel.add(buttonPanel);
            if (i < buttons.length - 1) {
                buttonsPanel.add(Box.createRigidArea(new Dimension(300, 25))); // Create 30 of space between each button
            }
        }

        // Add the buttons panel to the main panel
        mainPanel.add(buttonsPanel);


        // Add action listeners to the buttons
        buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new AddNewTruckUI(); // Open the AddNewTruckUI window
            }
        });


        // Add action listeners to the buttons
        buttons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAvailableTrucks();
            }
        });

        // Add action listeners to the buttons
        buttons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayUnAvailableTrucks();
            }
        });

        buttons[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new DeliverySystemUI(); // Open the DeliverySystemUI window
            }
        });


        //fading message
        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Serif", Font.BOLD, 20)); // Adjust the font size here
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center align the label
        messageLabel.setOpaque(true);  // This line is necessary to make the background visible
        messageLabel.setBackground(Color.PINK); // Set the background color to pink

        // Add the message label to the main panel
        mainPanel.add(messageLabel);

        // Add space after the message label
        mainPanel.add(Box.createVerticalGlue());

        // Set up the rest of the frame
        this.add(mainPanel);
        this.pack();
        this.setResizable(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }

    public static void showMessageAndOpen(String message, Runnable nextWindow) {
        TruckMenuUI instance = new TruckMenuUI();

        // Set the message text and start the fading effect
        messageLabel.setText(message);
        messageLabel.setForeground(new Color(0, 255, 0, 0)); // Start with green color (full opacity)

        Timer timer = new Timer(100, null);
        timer.addActionListener(new ActionListener() {
            private int opacity = 255; // Start with full opacity

            @Override
            public void actionPerformed(ActionEvent evt) {
                opacity -= 5; // Decrease opacity
                if (opacity <= 0) {
                    opacity = 0;
                    timer.stop();
                }
                messageLabel.setForeground(new Color(0, 0, 0, opacity));
            }
        });
        timer.start();

    }

        public static void main(String[] args) {
        SwingUtilities.invokeLater(TruckMenuUI::new);
    }

    public void AddTruck() {}

    public void displayAvailableTrucks() {
        Truck_Controller _tc= Truck_Controller.getInstance();
        List<Truck> availableTrucks = _tc.getAvailable_trucks();

        // Create a new window to display the table
        JFrame tableFrame = new JFrame("Available Trucks");
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableFrame.setLayout(new BorderLayout());

        // Create a table model
        String[] columnNames = {"Truck ID", "Weight", "Cooling", "Licence"};
        Object[][] data = new Object[availableTrucks.size()][4];

        for (int i = 0; i < availableTrucks.size(); i++) {
            Truck truck = availableTrucks.get(i);
            data[i][0] = truck.getId();
            data[i][1] = truck.getMax_weight();
            data[i][2] = truck.getCooling_capability();
            data[i][3] = truck.getNeeded_licence().toString();
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


    public void displayUnAvailableTrucks() {
        Truck_Controller _tc= Truck_Controller.getInstance();
        List<Truck> unAvailableTrucks = _tc.getInDelivery_trucks();

        // Create a new window to display the table
        JFrame tableFrame = new JFrame("Unavailable Trucks");
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableFrame.setLayout(new BorderLayout());

        // Create a table model
        String[] columnNames = {"Truck ID", "Weight", "Cooling", "Licence"};
        Object[][] data = new Object[unAvailableTrucks.size()][4];

        for (int i = 0; i < unAvailableTrucks.size(); i++) {
            Truck truck = unAvailableTrucks.get(i);
            data[i][0] = truck.getId();
            data[i][1] = truck.getMax_weight();
            data[i][2] = truck.getCooling_capability();
            data[i][3] = truck.getNeeded_licence().toString();
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
