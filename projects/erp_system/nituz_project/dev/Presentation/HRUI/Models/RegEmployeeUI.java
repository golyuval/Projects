package Presentation.HRUI.Models;

import Presentation.HRUI.templates.StyledButtonUI;
import defult.BusinessLayer.HRsystem.Driver;
import defult.BusinessLayer.HRsystem.Employee;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class RegEmployeeUI extends EmployeeUI {
    private JLabel nameLabel;
    private JLabel idLabel;
    private JDialog employeeDialog;

    public RegEmployeeUI(Employee employee) {
        super(employee, false);

    }


    @Override
    protected void showEmployeeDialog() {
        // Create the employee dialog
        employeeDialog = new JDialog();
        employeeDialog.setTitle("Employee Details");
        employeeDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        employeeDialog.setSize(500, 500);
        employeeDialog.setLocationRelativeTo(null);
        employeeDialog.getContentPane().setBackground(Color.PINK);
        employeeDialog.setLayout(new BorderLayout());

        // Create the content panel for the employee dialog
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setBackground(Color.PINK);

        // Add the employee details to the content panel
        JPanel detailsPanel = new JPanel(new GridLayout(5, 2, 5, 5));
//        detailsPanel.setBackground(Color.PINK);
        detailsPanel.add(new JLabel("Name: "));
        detailsPanel.add(new JLabel(employee.getName()));
        detailsPanel.add(new JLabel("ID: "));
        detailsPanel.add(new JLabel(String.valueOf(employee.getId())));
        detailsPanel.add(new JLabel("Salary: "));
        detailsPanel.add(new JLabel(String.valueOf(employee.getSalary())));
        detailsPanel.add(new JLabel("Special Info: "));
        detailsPanel.add(new JLabel(String.valueOf(employee.getSpecialInfo())));
        detailsPanel.add(new JLabel("Bank Info: "));
        detailsPanel.add(new JLabel(String.valueOf(employee.getBankInfo())));
        detailsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

//        detailsPanel.setBackground(Color.WHITE);
        contentPanel.add(detailsPanel);

        // Add salary and special info


        // Add terms
        JPanel termsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        termsPanel.setBackground(Color.PINK);
        termsPanel.setBorder(createTitledBorder("Terms"));
        for (String term : employee.getTerms()) {
            JLabel termLabel = new JLabel(term);
            termsPanel.add(termLabel);
        }
        contentPanel.add(termsPanel);

        // Add role tags
        JPanel rolesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        rolesPanel.setBackground(Color.PINK);
        rolesPanel.setBorder(createTitledBorder("Roles"));
        for (String role : employee.getRoles()) {
            JLabel roleLabel = new JLabel(role);
            rolesPanel.add(roleLabel);
        }
        contentPanel.add(rolesPanel);

        // Add availability shifts
        JPanel availabilityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        availabilityPanel.setBackground(Color.PINK);
        availabilityPanel.setBorder(createTitledBorder("Availability Shifts"));
        for (String shift : employee.getAvailability()) {
            JLabel shiftLabel = new JLabel(shift);
            availabilityPanel.add(shiftLabel);
        }
        contentPanel.add(availabilityPanel);

        // Add close button
        JButton closeButton = new JButton("Close");
        closeButton.setUI(new StyledButtonUI());
        closeButton.addActionListener(e -> employeeDialog.dispose());
        JPanel closeButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        closeButtonPanel.setBackground(Color.PINK);
        closeButtonPanel.add(closeButton);
        contentPanel.add(closeButtonPanel);
//        contentPanel.add(closeButton);

        employeeDialog.add(contentPanel, BorderLayout.CENTER);
        employeeDialog.setVisible(true);    }

    private Border createTitledBorder(String title) {
        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);
        Border emptyBorder = new EmptyBorder(5, 5, 5, 5);
        Border compoundBorder = new CompoundBorder(lineBorder, emptyBorder);
        return BorderFactory.createTitledBorder(compoundBorder, title, TitledBorder.LEFT, TitledBorder.TOP);
    }

    public static void main(String[] args) {
        // Example usage:
        Employee employee = new Employee("John Doe", 12345, 12345678, 5000,
                "Term 1, Term 2", LocalDate.now(),
                "Shift Manager, Storage", true,
                "2023-06-21,false, 2023-06-20,false",
                "Special Info", "2023-06-21,false, 2023-06-20,false",
                "password", "Week Shifts", "Bonuses", "Alert");

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(new RegEmployeeUI(employee));

        frame.add(panel);
        frame.setVisible(true);
    }
}
