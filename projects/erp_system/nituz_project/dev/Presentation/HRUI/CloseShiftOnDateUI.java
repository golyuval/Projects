package Presentation.HRUI;

import Presentation.HRUI.templates.BaseWindowUI;
import defult.BusinessLayer.Controllers.HR.ShiftBoard;
import defult.BusinessLayer.HRsystem.Shift;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CloseShiftOnDateUI extends BaseWindowUI {
    private JTextField dateField;
    private JRadioButton dayShiftRadioButton;
    private JRadioButton nightShiftRadioButton;
    private JTextField branchIdField;

    LocalDate date;
    boolean type;
    int branchId;

    public CloseShiftOnDateUI() {
        super("Close Shift on Date", false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

    }

    @Override
    protected JPanel createContentPanel() {
        JPanel panel = createGridPanel(2);

        JLabel dateLabel = new JLabel("Enter date to close on (dd/MM/yyyy):");
        dateField = new JTextField();

        JLabel shiftTypeLabel = new JLabel("Select shift type:");
        JPanel shiftTypePanel = createGridPanel(2); // Modified to have two columns
        dayShiftRadioButton = new JRadioButton("Day Shift");
        nightShiftRadioButton = new JRadioButton("Night Shift");
        ButtonGroup shiftTypeGroup = new ButtonGroup();
        shiftTypeGroup.add(dayShiftRadioButton);
        shiftTypeGroup.add(nightShiftRadioButton);
        shiftTypePanel.add(dayShiftRadioButton);
        shiftTypePanel.add(nightShiftRadioButton);
        saveBtn = new JButton("close shift");
        JLabel branchIdLabel = new JLabel("Enter branch ID (or 0 to close all branches):");
        branchIdField = new JTextField();

        panel.add(dateLabel);
        panel.add(dateField);
        panel.add(shiftTypeLabel);
        panel.add(shiftTypePanel);
        panel.add(branchIdLabel);
        panel.add(branchIdField);
        addButton(saveBtn, panel);
//        backButton = new JButton("Back to Menu");
//        addButton(backButton, panel);



        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                saveBtnFunc();
                new ManagerWindowUI();
                dispose();
            }
        });

        return panel;
    }
    public void backButtonFunc() {
        new ManagerWindowUI();
    }
    void saveBtnFunc() {
        date = LocalDate.parse(dateField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        type = dayShiftRadioButton.isSelected();
        branchId = Integer.parseInt(branchIdField.getText());
        ShiftBoard.getInstance().addToCloseShift(date, type, branchId);
        UIManager.put("OptionPane.background", new ColorUIResource(Color.PINK));
        JOptionPane.showMessageDialog(null, "Shift closed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CloseShiftOnDateUI();
            }
        });
    }
}
