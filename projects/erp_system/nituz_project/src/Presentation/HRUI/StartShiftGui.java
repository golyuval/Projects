package Presentation.HRUI;

import defult.BusinessLayer.Controllers.HR.ShiftBoard;
import defult.BusinessLayer.HRsystem.Shift;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Scanner;

import static defult.ServiceLayer.HR.HRService.HR_Main;
import static defult.ServiceLayer.HR.ShiftManagerService.shiftManagerWindow;

public class StartShiftGui extends JFrame {
    private ShiftBoard sb;
    private Scanner s;
    private JTextField branchIDField;
    private JRadioButton morningShiftButton;
    private JRadioButton nightShiftButton;

    public StartShiftGui(ShiftBoard sb, Scanner s) {
        this.sb = sb;
        this.s = s;

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.PINK);
        JLabel titleLabel = new JLabel("Enter Shift Type and Branch ID");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(Color.MAGENTA);

        JPanel shiftTypePanel = new JPanel();
        shiftTypePanel.setLayout(new FlowLayout());
        shiftTypePanel.setBackground(Color.PINK);

        JLabel shiftTypeLabel = new JLabel("Shift Type:");
        shiftTypeLabel.setForeground(Color.magenta);
        morningShiftButton = new JRadioButton("Morning");
        nightShiftButton = new JRadioButton("Night");
        morningShiftButton.setForeground(Color.magenta);
        nightShiftButton.setForeground(Color.magenta);
        ButtonGroup shiftTypeGroup = new ButtonGroup();
        shiftTypeGroup.add(morningShiftButton);
        shiftTypeGroup.add(nightShiftButton);
        nightShiftButton.setBackground(Color.PINK);
        morningShiftButton.setBackground(Color.PINK);
        shiftTypePanel.add(shiftTypeLabel);
        shiftTypePanel.add(morningShiftButton);
        shiftTypePanel.add(nightShiftButton);
        //mainPanel.setBorder(BorderFactory.createEmptyBorder(100, 600, 400, 600));
        JPanel branchIDPanel = new JPanel();
        branchIDPanel.setLayout(new FlowLayout());
        branchIDPanel.setBackground(Color.PINK);

        JLabel branchIDLabel = new JLabel("Branch ID:");
        branchIDField = new JTextField(10);
        branchIDLabel.setForeground(Color.magenta);
        branchIDPanel.add(branchIDLabel);
        branchIDPanel.add(branchIDField);

        JButton submitButton = new JButton("Enter Shift");
        submitButton.setForeground(Color.magenta);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean type = nightShiftButton.isSelected();
                int branchID = Integer.parseInt(branchIDField.getText());
                if (!morningShiftButton.isSelected() && !nightShiftButton.isSelected()) {
                    JOptionPane.showMessageDialog(null, "You have to choose Shift type", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (branchID <= 1 || branchID >= 11) {
                    JOptionPane.showMessageDialog(null, "Invalid branch ID. Please enter a value between 2 and 10.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    LocalDate testDateParsed = LocalDate.now();
                    Shift shift = sb.SC.getShiftByDateType(testDateParsed, type, branchID);

                    if (shift == null) {
                        try {
                            sb.isShiftIsClose(LocalDate.now(), type, branchID);
                            JOptionPane.showMessageDialog(null, "There is no shift scheduled.");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                        }
                        //dispose();
                        //HR_Main(sb, new Scanner(System.in));
                    } else {
//                        try {
//                            sb.SC.validateShiftStart(shift);
//                        } catch (Exception ex) {
//                            JOptionPane.showMessageDialog(null, ex.getMessage());
//                            dispose();
//                            shiftManagerWindow(sb, new Scanner(System.in));
//                        }
                        boolean flag = false;
                        String password ="";
                        while (!flag) {
                            password = JOptionPane.showInputDialog(null, "Enter Shift Manager password:", "Success", JOptionPane.QUESTION_MESSAGE);
                            if (password == null) {
                                dispose();
                                StartShiftGui window = new StartShiftGui(sb, s);
                                window.setExtendedState(JFrame.MAXIMIZED_BOTH);
                                window.setVisible(true);
                                break;
                            }

                            try {
                                sb.SC.validateShiftManager(shift, password);
                                flag = true;
                            } catch (Exception eX) {
                                JOptionPane.showMessageDialog(null, "Incorrect password, please enter again", "Error", JOptionPane.ERROR_MESSAGE);
                                //password = JOptionPane.showInputDialog(null, "Enter Shift Manager password:", "Success", JOptionPane.QUESTION_MESSAGE);
                            }
                        }
                            if (flag) {
                                if (shift.isInShift()) {
                                    dispose();
                                    InShiftGUI window = new InShiftGUI(sb, s, shift);
                                    window.setVisible(true);
                                } else {
                                    sb.SC.logStartShift(shift);
                                    JOptionPane.showMessageDialog(null, "" +
                                            "Shift Started successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                                    dispose();
                                    InShiftGUI window = new InShiftGUI(sb, s, shift);
                                    window.setVisible(true);

                                }
                            }




                        //dispose();
                        // Call the next window or perform further actions
                    }
                }
            }
        });

        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));  // Creates empty space
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 200)));  // Creates empty space
        mainPanel.add(shiftTypePanel);
        mainPanel.add(branchIDPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 200)));  // Creates empty space
        mainPanel.add(submitButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 300)));  // Creates empty space
        add(mainPanel);
        pack();
        setVisible(true);
    }

    // Define other methods and classes as necessary

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                ShiftBoard shiftBoard = ShiftBoard.getInstance();
//                new StartShiftGui(shiftBoard);
//            }
//        });
//    }
}
