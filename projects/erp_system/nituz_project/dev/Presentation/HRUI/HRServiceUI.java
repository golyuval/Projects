package Presentation.HRUI;

import Presentation.HRUI.templates.BaseWindowUI;
import Presentation.MainUI;
import defult.BusinessLayer.Controllers.HR.ShiftBoard;
import defult.BusinessLayer.HRsystem.Employee;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import java.util.concurrent.SynchronousQueue;

public class HRServiceUI extends BaseWindowUI {
    private ShiftBoard shiftBoard;
    private Scanner scanner;

    public HRServiceUI(ShiftBoard shiftBoard, Scanner scanner) {
        super("HR MAIN", false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.shiftBoard = shiftBoard;
        this.scanner = scanner;
    }

    @Override
    protected JPanel createContentPanel() {
        JPanel panel = createGridPanel(1);

        JButton managerButton = new JButton("Manager HR");
        addButton(managerButton);
        JButton employeeButton = new JButton("Employee");
        addButton(employeeButton);
        JButton shiftManagerButton = new JButton("Shift Manager");
        addButton(shiftManagerButton);
        JButton exitButton = new JButton("Exit");
        addButton(exitButton);


        managerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                managerWindow();
            }
        });

        employeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                employeeWindow();
            }
        });

        shiftManagerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shiftManagerWindow();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                systemsMenu();
            }
        });

        panel.add(managerButton);
        panel.add(employeeButton);
        panel.add(shiftManagerButton);
        panel.add(exitButton);

        return panel;
    }

    @Override
    public void backButtonFunc() {
        new MainUI();
    }

    private void managerWindow() {
        // Add your implementation for the Manager HR window
        dispose();
        new ManagerWindowUI();
    }

    private void employeeWindow() {
        // Add your implementation for the Employee window
        dispose();
        employeeServiceGUI window = new employeeServiceGUI(ShiftBoard.getInstance(), new Scanner(System.in));
        window.setExtendedState(MAXIMIZED_BOTH);
        window.setVisible(true);
    }

    private void shiftManagerWindow() {
        // Add your implementation for the Shift Manager window
        dispose();
        new ShiftManagerUI(ShiftBoard.getInstance(), new Scanner(System.in));
    }

    private void systemsMenu() {
        // Add your implementation for the systems menu
        dispose();
        new MainUI();
    }

    public static int validateIntInput(Scanner s) {
        while (true) {
            if (s.hasNextInt()) {
                int input = s.nextInt();
                s.nextLine(); // consume the remaining newline character
                return input;
            } else {
                System.out.println("Invalid input. Please enter an integer.");
                s.nextLine(); // consume the invalid input
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Scanner scanner = new Scanner(System.in);
                ShiftBoard shiftBoard = new ShiftBoard();
                HRServiceUI hrServiceUI = new HRServiceUI(shiftBoard, scanner);
                hrServiceUI.setVisible(true);
            }
        });
    }
}
