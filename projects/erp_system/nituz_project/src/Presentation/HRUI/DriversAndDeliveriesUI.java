package Presentation.HRUI;

import Presentation.HRUI.templates.BaseWindowUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DriversAndDeliveriesUI extends BaseWindowUI {
    private JTextArea driversTextArea;
    private JTextArea deliveriesTextArea;
    private JButton assignDriverButton;

    public DriversAndDeliveriesUI() {

        super("Drivers and Deliveries", true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

    }

    @Override
    protected JPanel createContentPanel() {
        JPanel panel = createGridPanel(1);

        driversTextArea = new JTextArea();
        JScrollPane driversScrollPane = new JScrollPane(driversTextArea);
        driversScrollPane.setPreferredSize(new Dimension(500, 200));
        panel.add(driversScrollPane);

        deliveriesTextArea = new JTextArea();
        JScrollPane deliveriesScrollPane = new JScrollPane(deliveriesTextArea);
        deliveriesScrollPane.setPreferredSize(new Dimension(500, 200));
        panel.add(deliveriesScrollPane);

        assignDriverButton = new JButton("Assign Driver to Delivery");
        addButton(assignDriverButton);
        assignDriverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add code to handle Assign Driver to Delivery button click
            }
        });

//        backButton = new JButton("Back to Menu");
//        addButton(backButton, panel);
//        backButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                dispose();
//                new ManagerWindowUI();
//            }
//        });

        return panel;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DriversAndDeliveriesUI();
            }
        });
    }
}
