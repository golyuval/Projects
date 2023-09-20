package Presentation.DeliveryUI.Forms_UI;

import Presentation.DeliveryUI.DeliverySystemUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormsMenuUI extends JFrame {
    private JPanel mainPanel;
    private JButton[] buttons;
    private final String[] buttonNames = {"Destination Form", "Delivery Form", "Overload Form", "Exit to Delivery Menu"};


    public FormsMenuUI() {
        // Set the frame properties
        this.setTitle("Forms System");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true); // Remove window decorations
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the main panel and set the layout
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(buttonNames.length + 1, 1));
        mainPanel.setBackground(Color.PINK);

        // Add the title
        JLabel titleLabel = new JLabel("Extract Forms", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        mainPanel.add(titleLabel);

        // Create the buttons and their panels
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
        }

        // Add action listeners to the buttons
        buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                Forms_1_UI forms1Ui = new Forms_1_UI();
                forms1Ui.setVisible(true);
            }
        });

        buttons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                Forms_2_UI forms2Ui = new Forms_2_UI();
                forms2Ui.setVisible(true);
            }
        });

        buttons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                Forms_3_UI forms3Ui = new Forms_3_UI();
                forms3Ui.setVisible(true);
            }
        });

        buttons[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new DeliverySystemUI(); // Open the DeliverySystemUI window
            }
        });

        // Add the main panel to the frame
        this.add(mainPanel);

        this.setVisible(true);
    }

}
