package Presentation.HRUI;

import Presentation.HRUI.templates.StyledButtonUI;
import defult.BusinessLayer.Controllers.HR.ShiftBoard;
import defult.BusinessLayer.HRsystem.Employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import static defult.ServiceLayer.HR.EmployeeService.removeSubmission;

public class RemoveSubmissionGUI extends JFrame {
    private ShiftBoard sb;
    private Scanner s;
    private Employee employee;
    private JTextField choiceField;
    private JButton removeButton;
    private JButton cancelButton;

    public RemoveSubmissionGUI(ShiftBoard sb, Scanner s, Employee employee) {
        this.sb = sb;
        this.s = s;
        this.employee = employee;

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.PINK);

        JLabel title = new JLabel("Remove Submission");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Serif", Font.BOLD, 25));
        title.setForeground(Color.MAGENTA);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBackground(Color.PINK);
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(200, 570, 450, 570));
        Map<Integer, String> submittedShifts = new LinkedHashMap<>();
        StringBuilder options = new StringBuilder();
        int i = 1;
        if (!employee.getAvailability().isEmpty()) {
            for (String shift : employee.getAvailability()) {
                String date = shift.split(",")[0];
                String type = Boolean.parseBoolean(shift.split(",")[1]) ? "night" : "morning";
                options.append(i).append(". ").append(date).append(" ,").append(type).append("\n");
                submittedShifts.put(i, shift);
                i++;
            }
            JTextArea shiftsTextArea = new JTextArea(options.toString());
            shiftsTextArea.setEditable(false);
            shiftsTextArea.setFont(new Font("Serif", Font.PLAIN, 15));
            shiftsTextArea.setLineWrap(true);
            shiftsTextArea.setWrapStyleWord(true);
            shiftsTextArea.setBackground(Color.PINK);
            JScrollPane shiftsScrollPane = new JScrollPane(shiftsTextArea);
            shiftsScrollPane.setPreferredSize(new Dimension(300, 100));
            optionsPanel.add(shiftsScrollPane);
        } else {
            JLabel noShiftsLabel = new JLabel("No submitted shifts to remove");
            noShiftsLabel.setForeground(Color.MAGENTA);
            noShiftsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            optionsPanel.add(noShiftsLabel);
        }

        JLabel choiceLabel = new JLabel("Enter option number to remove submission for that shift:");
        choiceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        choiceLabel.setForeground(Color.MAGENTA);
        choiceField = new JTextField(5);
        choiceField.setMaximumSize(choiceField.getPreferredSize());
        choiceField.setHorizontalAlignment(JTextField.LEFT);

        JPanel choicePanel = new JPanel();
        choicePanel.setLayout(new BoxLayout(choicePanel, BoxLayout.X_AXIS));
        choicePanel.setBackground(Color.PINK);
        choicePanel.add(choiceLabel);
        choicePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        choicePanel.add(choiceField);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.setBackground(Color.PINK);

        removeButton = new JButton("Remove");
        removeButton.setForeground(Color.MAGENTA);
        removeButton.setPreferredSize(new Dimension(100, 30));
        removeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        removeButton.setUI(new StyledButtonUI());
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (choiceField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter an option number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Stop execution if the field is empty
                }
                int choice = Integer.parseInt(choiceField.getText());
                if (submittedShifts.containsKey(choice)) {
                    String dateP = submittedShifts.get(choice).split(",")[0];
                    LocalDate date = LocalDate.parse(dateP, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    boolean typeB = Boolean.parseBoolean(submittedShifts.get(choice).split(",")[1]);
                    sb.removeSubmit(employee, date, typeB);
                    JOptionPane.showMessageDialog(null, "Shift was removed successfully", "Success", JOptionPane.PLAIN_MESSAGE);
                    dispose();
                    RemoveSubmissionGUI window = new RemoveSubmissionGUI(sb, s, employee);
                    window.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid option number. Please enter a valid option number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(100, 30));
        cancelButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        cancelButton.setForeground(Color.MAGENTA);
        cancelButton.setUI(new StyledButtonUI());
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                EmployeeServiceUI window = new EmployeeServiceUI(sb, s, employee);
                window.setVisible(true);
            }
        });

        buttonsPanel.add(removeButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonsPanel.add(cancelButton);

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(optionsPanel);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(choicePanel);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(buttonsPanel);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
        pack();
        setVisible(true);
        centerFrame();
    }

    private void centerFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        setLocation(x, y);
    }

    private void inEmployeeWindow(ShiftBoard sb, Scanner s, Employee employee) {
        // Implement your logic for going back to the employee window
    }

    // Define other methods and classes as necessary

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ShiftBoard sb = new ShiftBoard();
                Scanner s = new Scanner(System.in);
                Employee employee = new Employee();

                new RemoveSubmissionGUI(sb, s, employee);
            }
        });
    }
}
