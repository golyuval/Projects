package Presentation.HRUI.Models;

import Presentation.HRUI.templates.PanelRound;
import Presentation.HRUI.templates.StyledButtonUI;
import defult.BusinessLayer.HRsystem.Driver;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

public class DriverUI extends EmployeeUI {
    private Driver driver;
    private JLabel nameLabel;
    private JLabel idLabel;
    private JDialog driverDialog;

    public DriverUI(Driver driver) {
        super(driver, true);
        this.driver = driver;

    }

    private void showDriverDialog() {
        // Create the driver dialog
        driverDialog = new JDialog();
        driverDialog.setTitle("Driver Details");
        driverDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        driverDialog.setSize(500, 500);
        driverDialog.setLocationRelativeTo(null);
        driverDialog.getContentPane().setBackground(Color.PINK);
        driverDialog.setLayout(new BorderLayout());

        // Create the content panel for the driver dialog
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setBackground(Color.PINK);

        // Add the driver details to the content panel
        JPanel detailsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        detailsPanel.setBackground(Color.PINK);
        detailsPanel.add(new JLabel("Name: "));
        detailsPanel.add(new JLabel(driver.getName()));
        detailsPanel.add(new JLabel("ID: "));
        detailsPanel.add(new JLabel(String.valueOf(driver.getId())));
        detailsPanel.add(new JLabel("Salary: "));
        detailsPanel.add(new JLabel(String.valueOf(driver.getSalary())));
        detailsPanel.add(new JLabel("Special Info: "));
        detailsPanel.add(new JLabel(String.valueOf(driver.getSpecialInfo())));
        detailsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        contentPanel.add(detailsPanel);

        // Add licenses
        JPanel licensesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        licensesPanel.setBackground(Color.PINK);
        licensesPanel.setBorder(createTitledBorder("Licenses"));
        List<String> licenses = driver.getLicenses();
        for (String license : licenses) {
            JLabel licenseLabel = new JLabel(license);
            licensesPanel.add(licenseLabel);
        }
        contentPanel.add(licensesPanel);

        // Add close button
        JButton closeButton = new JButton("Close");
        closeButton.setUI(new StyledButtonUI());
        JPanel closeButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        closeButtonPanel.setBackground(Color.PINK);
        closeButtonPanel.add(closeButton);
        contentPanel.add(closeButtonPanel);

        closeButton.addActionListener(e -> driverDialog.dispose());

        driverDialog.add(contentPanel, BorderLayout.CENTER);
        driverDialog.setVisible(true);
    }

    @Override
    protected void showEmployeeDialog() {
        showDriverDialog();
    }

    private Border createTitledBorder(String title) {
        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);
        Border emptyBorder = new EmptyBorder(5, 5, 5, 5);
        Border compoundBorder = new CompoundBorder(lineBorder, emptyBorder);
        return BorderFactory.createTitledBorder(compoundBorder, title, TitledBorder.LEFT, TitledBorder.TOP);
    }

    public static void main(String[] args) {
        // Example usage:
        Driver driver = new Driver("John Doe", 12345, 12345678, 5000,
                "Term 1, Term 2", LocalDate.now(),
                "Driver", false,
                "2023-06-21,false, 2023-06-20,false",
                "Special Info", "2023-06-21,false, 2023-06-20,false",
                "password", "Week Shifts", "Bonuses", "Alert", "A1, B1");

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(new DriverUI(driver));

        frame.add(panel);
        frame.setVisible(true);
    }
}
