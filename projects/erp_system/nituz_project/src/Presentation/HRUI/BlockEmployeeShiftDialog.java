package Presentation.HRUI;

import defult.BusinessLayer.Controllers.HR.ShiftBoard;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BlockEmployeeShiftDialog extends JDialog {
    private JTextField employeeIdField;
    private JTextField dateField;
    private JRadioButton dayShiftRadioButton;
    private JRadioButton nightShiftRadioButton;
    private JButton blockButton;

    public BlockEmployeeShiftDialog(JFrame parentFrame) {
        super(parentFrame, "Block Employee's Shift", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 250);
        setLocationRelativeTo(parentFrame);
        UIManager.put("RadioButton.background", new ColorUIResource(Color.PINK));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.PINK); // Set the background color to pink

        employeeIdField = new JTextField();
        dateField = new JTextField();

        dayShiftRadioButton = new JRadioButton("Day Shift");
        nightShiftRadioButton = new JRadioButton("Night Shift");

        ButtonGroup shiftTypeGroup = new ButtonGroup();
        shiftTypeGroup.add(dayShiftRadioButton);
        shiftTypeGroup.add(nightShiftRadioButton);

        blockButton = new JButton("Block Shift");

        panel.add(new JLabel("Employee ID:"));
        panel.add(employeeIdField);

        panel.add(new JLabel("Date (dd/MM/yyyy):"));
        panel.add(dateField);

        panel.add(new JLabel("Shift Type:"));
        panel.add(dayShiftRadioButton);
        panel.add(nightShiftRadioButton);

        panel.add(blockButton);

        add(panel);

        // Apply constraints to the employeeIdField text field
        employeeIdField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String text = employeeIdField.getText();
                if (!text.matches("\\d+") || text.length() != 9) {
                    int thickness = 2; // Set the desired thickness of the border
                    employeeIdField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    blockButton.setEnabled(false);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String text = employeeIdField.getText();
                if (text.matches("\\d+") && text.length() == 9) {
                    employeeIdField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border")); // Revert to the default border style
                    updateBlockButtonState();
                } else {
                    int thickness = 2; // Set the desired thickness of the border
                    employeeIdField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    blockButton.setEnabled(false);
                }
            }
        });

        // Apply constraints to the dateField text field
        dateField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String text = dateField.getText();
                if (!isValidDateFormat(text)) {
                    int thickness = 2; // Set the desired thickness of the border
                    dateField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    blockButton.setEnabled(false);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String text = dateField.getText();
                if (isValidDateFormat(text)) {
                    dateField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border")); // Revert to the default border style
                    updateBlockButtonState();
                } else {
                    int thickness = 2; // Set the desired thickness of the border
                    dateField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    blockButton.setEnabled(false);
                }
            }
        });

        blockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int employeeId = Integer.parseInt(employeeIdField.getText());
                String dateText = dateField.getText();
                boolean isNightShift = nightShiftRadioButton.isSelected();

                // Perform the validation check
                if (!isNightShift && !dayShiftRadioButton.isSelected()) {
                    UIManager.put("OptionPane.background", Color.PINK);
                    UIManager.put("Panel.background", Color.PINK);
                    JOptionPane.showMessageDialog(BlockEmployeeShiftDialog.this,
                            "Please select a shift type (Day Shift or Night Shift).",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Perform the block shift operation
                LocalDate date = null;
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    date = LocalDate.parse(dateText, formatter);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(BlockEmployeeShiftDialog.this,
                            "Invalid date format. Please enter date in dd/MM/yyyy format.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Add code to handle the block shift operation
                if(ShiftBoard.getInstance().EC.validateId(employeeId)){
                    UIManager.put("OptionPane.background", Color.PINK);
                    UIManager.put("Panel.background", Color.PINK);
                    JOptionPane.showMessageDialog(BlockEmployeeShiftDialog.this,
                            "Employee with id "+employeeId+" dose not exist",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }else {
                    try {
                        ShiftBoard.getInstance().EC.validateCanBlock(employeeId, date, isNightShift);
                        ShiftBoard.getInstance().EC.blockEmployee(employeeId, date + "," + isNightShift);
                        UIManager.put("OptionPane.background", Color.PINK);
                        UIManager.put("Panel.background", Color.PINK);
                        JOptionPane.showMessageDialog(null, "blocked successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

                    }catch (Exception ex){
                        UIManager.put("OptionPane.background", Color.PINK);
                        UIManager.put("Panel.background", Color.PINK);
                        JOptionPane.showMessageDialog(BlockEmployeeShiftDialog.this,
                                ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }
                dispose();
            }
        });

    }

    private boolean isValidDateFormat(String dateText) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(dateText, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void updateBlockButtonState() {
        boolean isEmployeeIdValid = employeeIdField.getText().matches("\\d+") && employeeIdField.getText().length() == 9;
        boolean isDateValid = isValidDateFormat(dateField.getText());
        blockButton.setEnabled(isEmployeeIdValid && isDateValid);
    }
}
