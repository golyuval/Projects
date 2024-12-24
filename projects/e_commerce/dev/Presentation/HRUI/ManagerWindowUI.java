package Presentation.HRUI;

import Presentation.HRUI.templates.BaseWindowUI;
import defult.BusinessLayer.Controllers.HR.ShiftBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class ManagerWindowUI extends BaseWindowUI {
    private JButton editEmployeesButton;
    private JButton scheduleShiftButton;
    private JButton driversAndDeliveriesButton;
    private JButton blockEmployeeShiftButton;
    private JButton closeBranchButton;
    private JButton editShiftButton;
    private JButton hoursAndSalaryButton;
    private JButton shiftHistoryButton;

    public ManagerWindowUI() {
        super("Manager Window", false);
        super.backButton.setVisible(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }



    @Override
    protected JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.PINK);
        editEmployeesButton = new JButton("Add/Edit/Delete Employee");
        scheduleShiftButton = new JButton("Schedule Shift");
        driversAndDeliveriesButton = new JButton("Schedule Drivers to Deliveries");
        blockEmployeeShiftButton = new JButton("Block Employee's Shift");
        closeBranchButton = new JButton("Close Branch on Occasion");
        editShiftButton = new JButton("Edit Shift");
        hoursAndSalaryButton = new JButton("Get Employee's Hours and Salary");
        shiftHistoryButton = new JButton("Get Shift History");

        addButton(editEmployeesButton, panel);
        addButton(scheduleShiftButton, panel);
        addButton(driversAndDeliveriesButton, panel);
        addButton(blockEmployeeShiftButton, panel);
        addButton(closeBranchButton, panel);
        addButton(editShiftButton, panel);
        addButton(hoursAndSalaryButton, panel);
        addButton(shiftHistoryButton, panel);

        backButton = new JButton("Back to Menu");
        addButton(backButton, panel);

        editEmployeesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new EditEmployeesUI();
            }
        });

        scheduleShiftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ScheduleShiftUI();
            }
        });

        driversAndDeliveriesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                driverAndDelUI window = new driverAndDelUI(ShiftBoard.getInstance(), new Scanner(System.in));
                window.setExtendedState(MAXIMIZED_BOTH);
                window.setVisible(true);
            }
        });

        blockEmployeeShiftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BlockEmployeeShiftDialog dialog = new BlockEmployeeShiftDialog(ManagerWindowUI.this);
                dialog.setVisible(true);
            }
        });


        closeBranchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CloseShiftOnDateUI();
            }
        });

        editShiftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new EditShiftUI();
            }
        });

        hoursAndSalaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new HoursAndSalaryUI();
            }
        });

        shiftHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ImportShiftHistoryUI();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new HRServiceUI(ShiftBoard.getInstance(), new Scanner(System.in));
            }
        });

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ManagerWindowUI();
            }
        });
    }
}
