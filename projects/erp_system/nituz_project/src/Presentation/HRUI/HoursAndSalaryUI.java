package Presentation.HRUI;

import Presentation.HRUI.templates.BaseWindowUI;
import Presentation.HRUI.templates.StyledButtonUI;
import defult.BusinessLayer.Controllers.HR.ShiftBoard;
import defult.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.YearMonth;

public class HoursAndSalaryUI extends BaseWindowUI {
    private JTextField employeeIdTextField;
    private JButton calculateButton;
    JComboBox<String> monthComboBox;
    JTextField yearTextField;
    public HoursAndSalaryUI() {
        super("Hours and Salary", false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);


    }


    public void backButtonFunc() {
        new ManagerWindowUI();
    }

    @Override
    protected JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.PINK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel employeeIdLabel = new JLabel("Employee's ID:");
        employeeIdTextField = new JTextField();
        addTextField(employeeIdTextField);

        JLabel monthLabel = new JLabel("Month:");
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthComboBox = new JComboBox<>(months);
        addComboField(monthComboBox);

        JLabel yearLabel = new JLabel("Year:");
        yearTextField = new JTextField();
        addTextField(yearTextField);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(employeeIdLabel, gbc);

        gbc.gridy = 1;
        panel.add(monthLabel, gbc);

        gbc.gridy = 2;
        panel.add(yearLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(employeeIdTextField, gbc);

        gbc.gridy = 1;
        panel.add(monthComboBox, gbc);

        gbc.gridy = 2;
        panel.add(yearTextField, gbc);

        calculateButton = new JButton("Calculate");
        addButton(calculateButton);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.PINK);
        buttonPanel.add(calculateButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0.0;
        panel.add(buttonPanel, gbc);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int employeeId = Integer.parseInt(employeeIdTextField.getText());

                // Mapping the selected month to its corresponding value of "01" to "12"
                int month = monthComboBox.getSelectedIndex() + 1;
                int year = Integer.parseInt(yearTextField.getText());

                // Add code to handle Calculate button click
                double salary = ShiftBoard.getInstance().calcSalary(ShiftBoard.getInstance().EC.getEmployeeById(employeeId), YearMonth.of(year, month));
//                System.out.println(salary);
                // Display the calculated salary in a message dialog
                JOptionPane.showMessageDialog(null, "Salary for Employee ID " + employeeId + " in " + YearMonth.of(year, month) + ": $" + salary);
            }
        });


        return panel;
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HoursAndSalaryUI();
            }
        });
    }
}
