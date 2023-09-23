package Presentation.HRUI;

import Presentation.HRUI.templates.BaseWindowUI;
import defult.BusinessLayer.Controllers.HR.ShiftBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class EditEmployeesUI extends BaseWindowUI {
    private JButton addEmployeeButton;
    private JButton editEmployeeButton;
    private JButton deleteEmployeeButton;

    public EditEmployeesUI() {
        super("Edit Employees", false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

    }
    public void backButtonFunc() {
        new ManagerWindowUI();
    }
    @Override
    protected JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.PINK);

        addEmployeeButton = new JButton("Add New Employee");
        editEmployeeButton = new JButton("Edit Existing Employee");
        deleteEmployeeButton = new JButton("Delete Employee");

        addButton(addEmployeeButton, panel);
        addButton(editEmployeeButton, panel);
        addButton(deleteEmployeeButton, panel);


//        backButton = new JButton("Back to Menu");
//        addButton(backButton, panel);
//        backButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                dispose();
//                new ManagerWindowUI();
//            }
//        });

        addEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AddNewEmployeeUI(ShiftBoard.getInstance(), new Scanner(System.in));
            }
        });

        editEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new EditEmployeeUI(ShiftBoard.getInstance(), new Scanner(System.in));
            }
        });

        deleteEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create the DeleteEmployeeUI dialog
                JDialog deleteDialog = new JDialog();
                deleteDialog.setTitle("Delete Employee");
                deleteDialog.setModal(true);
                deleteDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                // Create the DeleteEmployeeUI panel and add it to the dialog
                DeleteEmployeeUI deleteEmployeeUI = new DeleteEmployeeUI(ShiftBoard.getInstance(), new Scanner(System.in));
                deleteDialog.add(deleteEmployeeUI);

                // Set the dialog properties and make it visible
                deleteDialog.pack();
                deleteDialog.setLocationRelativeTo(null);
                deleteDialog.setVisible(true);
            }
        });
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EditEmployeesUI();
            }
        });
    }
}
