package Presentation.HRUI.Models;

import Presentation.HRUI.templates.ScrollBar.ScrollBarCustom;
import Presentation.HRUI.templates.StyledButtonUI;
import defult.BusinessLayer.HRsystem.Employee;
import defult.BusinessLayer.HRsystem.Shift;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Map;

public class ShiftModelUI extends JPanel {
    private Shift shift;

    public ShiftModelUI(Shift shift) {
        this.shift = shift;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(100, 120));
        setBackground(Color.LIGHT_GRAY);

        JLabel shiftLabel = new JLabel("Shift");

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        infoPanel.setOpaque(false);
        infoPanel.add(shiftLabel);

        JLabel dateLabel = new JLabel("Date: " + shift.getDate());
        infoPanel.add(dateLabel);

        JLabel branchLabel = new JLabel("Branch: " + shift.getBranch());
        infoPanel.add(branchLabel);
        add(infoPanel, BorderLayout.CENTER);

        JButton detailsButton = new JButton("Details");
        detailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showShiftDialog();
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        buttonPanel.add(detailsButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void showShiftDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Shift Details");
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLayout(new BorderLayout());

        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel();

        dialog.add(leftPanel, BorderLayout.WEST);
        dialog.add(rightPanel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        JPanel closeButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        closeButtonPanel.setBackground(Color.WHITE);
        closeButtonPanel.add(closeButton);
        closeButton.setUI(new StyledButtonUI());
        dialog.add(closeButtonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(200, 0));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        JLabel dateLabel = new JLabel("Date: " + shift.getDate());
        panel.add(dateLabel);

        JLabel branchLabel = new JLabel("Branch: " + shift.getBranch());
        panel.add(branchLabel);

        return panel;
    }


    private JPanel createRightPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel employeesLabel = new JLabel("Employees");
        employeesLabel.setFont(employeesLabel.getFont().deriveFont(Font.BOLD));

        // Top Section - Manager
        JPanel managerPanel = new JPanel();
        managerPanel.setLayout(new BoxLayout(managerPanel, BoxLayout.Y_AXIS));
        managerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        managerPanel.add(employeesLabel);

        EmployeeUI managerUI = new RegEmployeeUI(shift.getManager());
        JPanel managerUIWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        managerUIWrapper.add(managerUI);

        JLabel managerLabel = new JLabel("Manager");
        JPanel managerLabelWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        managerLabel.setHorizontalAlignment(SwingConstants.LEFT);
        managerLabel.setForeground(new Color(77, 80, 82));
        managerLabelWrapper.add(managerLabel);

        managerPanel.add(managerUIWrapper);
        managerPanel.add(managerLabelWrapper);
        managerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Bottom Section - Employees
        JPanel employeesPanel = new JPanel();
        employeesPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        employeesPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JScrollPane scrollPane = new JScrollPane(employeesPanel);
        scrollPane.setVerticalScrollBar(new ScrollBarCustom());
        scrollPane.setHorizontalScrollBar(new ScrollBarCustom());
        ScrollBarCustom sp = new ScrollBarCustom();
        sp.setOrientation(JScrollBar.HORIZONTAL);
        scrollPane.setHorizontalScrollBar(sp);
        ScrollBarCustom spV = new ScrollBarCustom();
        spV.setOrientation(JScrollBar.VERTICAL);
        scrollPane.setVerticalScrollBar(spV);

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panel.add(managerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add EmployeeUI objects for each employee (except the manager)
        Map<Employee, String> employees = shift.getFinalEmployees();
        for (Map.Entry<Employee, String> entry : employees.entrySet()) {
            if (!entry.getKey().equals(shift.getManager())) {
                EmployeeUI employeeUI = new RegEmployeeUI(entry.getKey());
                employeesPanel.add(employeeUI);
            }
        }

        return panel;
    }

    public void setSelected(boolean selected) {
        if (selected) {
            setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2)); // Visual indication of selection
        } else {
            setBorder(BorderFactory.createEmptyBorder()); // Reset the border
        }
    }




    public static void main(String[] args) {
        Employee manager = new Employee("John Doe", 12345, 12345678, 5000,
                "Term 1, Term 2", LocalDate.now(),
                "Shift Manager, Storage", true,
                "2023-06-21,false, 2023-06-20,false",
                "Special Info", "2023-06-21,false, 2023-06-20,false",
                "password", "Week Shifts", "Bonuses", "Alert");

        Map<Employee, String> employees = Map.of(
                new Employee("Jane Smith", 54321, 87654321, 4000,
                        "Term 3, Term 4", LocalDate.now(),
                        "Storage", true,
                        "2023-06-21,false, 2023-06-20,false",
                        "Special Info", "2023-06-21,false, 2023-06-20,false",
                        "password", "Week Shifts", "Bonuses", "Alert"), "Storage",
                new Employee("Mike Johnson", 67890, 98765432, 4500,
                        "Term 5, Term 6", LocalDate.now(),
                        "Cashier", true,
                        "2023-06-21,false, 2023-06-20,false",
                        "Special Info", "2023-06-21,false, 2023-06-20,false",
                        "password", "Week Shifts", "Bonuses", "Alert"), "Cashier",
                new Employee("Sarah Davis", 13579, 24681357, 4200,
                        "Term 7, Term 8", LocalDate.now(),
                        "Sales", false,
                        "2023-06-21,false, 2023-06-20,false",
                        "Special Info", "2023-06-21,false, 2023-06-20,false",
                        "password", "Week Shifts", "Bonuses", "Alert"), "Sales",
                new Employee("Robert Wilson", 24680, 13579246, 3800,
                        "Term 9, Term 10", LocalDate.now(),
                        "Cashier", true,
                        "2023-06-21,false, 2023-06-20,false",
                        "Special Info", "2023-06-21,false, 2023-06-20,false",
                        "password", "Week Shifts", "Bonuses", "Alert"), "Cashier",
                new Employee("Emily Thompson", 97531, 86420975, 4400,
                        "Term 11, Term 12", LocalDate.now(),
                        "Storage", true,
                        "2023-06-21,false, 2023-06-20,false",
                        "Special Info", "2023-06-21,false, 2023-06-20,false",
                        "password", "Week Shifts", "Bonuses", "Alert"), "Storage"
        );

        Shift shift = new Shift(LocalDate.now(), true, manager, employees, 1, null, null);

        ShiftModelUI shiftModelUI = new ShiftModelUI(shift);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(shiftModelUI);

        frame.add(panel);
        frame.setVisible(true);
    }

    public Shift getShift() {
        return shift;
    }
}
