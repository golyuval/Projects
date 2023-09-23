package Presentation.HRUI;

import Presentation.HRUI.Models.ShiftModelUI;
import Presentation.HRUI.templates.BaseWindowUI;
import Presentation.HRUI.templates.ScrollBar.ScrollBarCustom;
import Presentation.HRUI.templates.StyledButtonUI;
import defult.BusinessLayer.Controllers.HR.ShiftBoard;
import defult.BusinessLayer.HRsystem.Shift;
import defult.ServiceLayer.HR.ShiftManagerService;

import javax.print.DocFlavor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ImportShiftHistoryUI extends BaseWindowUI {
    private JTextField branchIdField;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextArea historyTextArea;
    private JButton importButton;

    public ImportShiftHistoryUI() {
        super("Import Shift History", false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

    }

    @Override
    protected JPanel createContentPanel() {
        JPanel bPanel = new JPanel();
        bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.Y_AXIS));
        bPanel.setBackground(Color.PINK);
        JPanel panel = createGridPanel(2);

        JLabel branchIdLabel = new JLabel("Enter Branch ID:");
        branchIdField = new JTextField();

        JLabel startDateLabel = new JLabel("Enter Start Date (yyyy-MM-dd):");
        startDateField = new JTextField();

        JLabel endDateLabel = new JLabel("Enter End Date (yyyy-MM-dd):");
        endDateField = new JTextField();

        JLabel historyLabel = new JLabel("Shift History:");
        historyTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(historyTextArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));


        panel.add(branchIdLabel);
        panel.add(branchIdField);
        panel.add(startDateLabel);
        panel.add(startDateField);
        panel.add(endDateLabel);
        panel.add(endDateField);
        bPanel.add(panel);

        importButton = new JButton("Import");
        bPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        addButton(importButton, bPanel);


        // Add constraint for branchIdField
        branchIdField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (branchIdField.getText().isEmpty()) {
                    int thickness = 2; // Set the desired thickness of the border
                    branchIdField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    importButton.setEnabled(false);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!branchIdField.getText().isEmpty()) {
                    branchIdField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                    importButton.setEnabled(true);
                } else {
                    int thickness = 2; // Set the desired thickness of the border
                    branchIdField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    importButton.setEnabled(false);
                }
            }
        });

        startDateField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (startDateField.getText().isEmpty() || !isValidDateFormat(startDateField.getText())) {
                    int thickness = 2; // Set the desired thickness of the border
                    startDateField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    importButton.setEnabled(false);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!startDateField.getText().isEmpty() && isValidDateFormat(startDateField.getText())) {
                    startDateField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                    importButton.setEnabled(true);
                } else {
                    int thickness = 2; // Set the desired thickness of the border
                    startDateField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    importButton.setEnabled(false);
                }
            }
        });

        endDateField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (endDateField.getText().isEmpty() || !isValidDateFormat(endDateField.getText())) {
                    int thickness = 2; // Set the desired thickness of the border
                    endDateField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    importButton.setEnabled(false);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!endDateField.getText().isEmpty() && isValidDateFormat(endDateField.getText())) {
                    endDateField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                    importButton.setEnabled(true);
                } else {
                    int thickness = 2; // Set the desired thickness of the border
                    endDateField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, thickness));
                    importButton.setEnabled(false);
                }
            }
        });


       // bPanel.add(importButton, gbc);
        // Center the import button


        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importShiftHistory();
            }
        });

        return bPanel;
    }

//    private void importShiftHistory() {
//        int branchId = Integer.parseInt(branchIdField.getText());
//        LocalDate startDate = LocalDate.parse(startDateField.getText());
//        LocalDate endDate = LocalDate.parse(endDateField.getText());
//
//        // Perform the import operation and get the shift history
//        List<String> history = performShiftHistoryImport(branchId, startDate, endDate);
//
//        // Clear the existing text
//        historyTextArea.setText("");
//
//        // Append the shift history to the text area
//        for (String shift : history) {
//            historyTextArea.append(shift + "\n");
//        }
//    }

    private void importShiftHistory() {
        int branchId = Integer.parseInt(branchIdField.getText());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate startDate = LocalDate.parse(startDateField.getText(), dateFormatter);
        LocalDate endDate = LocalDate.parse(endDateField.getText(), dateFormatter);

        // Perform the import operation and get the shift history
        List<Shift> history = performShiftHistoryImport(branchId, startDate, endDate);

        // Create the dialog
        JDialog historyDialog = new JDialog();
        historyDialog.setTitle("Shift History");
        historyDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        historyDialog.setSize(400, 400);
        historyDialog.setLocationRelativeTo(null);
        historyDialog.setLayout(new BorderLayout());

        // Create the content panel for the dialog
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the list panel
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(Color.PINK);

        JPanel scrollablePanel = new JPanel();
        scrollablePanel.setLayout(new BoxLayout(scrollablePanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(scrollablePanel);
        scrollPane.setVerticalScrollBar(new ScrollBarCustom());
        scrollPane.setHorizontalScrollBar(new ScrollBarCustom());
        ScrollBarCustom sp = new ScrollBarCustom();
        sp.setOrientation(JScrollBar.HORIZONTAL);
        scrollPane.setHorizontalScrollBar(sp);
        ScrollBarCustom spV = new ScrollBarCustom();
        spV.setOrientation(JScrollBar.VERTICAL);
        scrollPane.setVerticalScrollBar(spV);

        listPanel.add(scrollPane, BorderLayout.CENTER);

        // Create the close button
        JButton closeButton = new JButton("Close");
        closeButton.setUI(new StyledButtonUI());

        // Add action listener to close the dialog when the button is clicked
        closeButton.addActionListener(e -> historyDialog.dispose());

        // Add the list panel and the close button to the content panel
        contentPanel.add(listPanel, BorderLayout.CENTER);
        contentPanel.add(closeButton, BorderLayout.SOUTH);

        // Add the content panel to the dialog
        historyDialog.add(contentPanel);

        // Show the dialog
        historyDialog.setVisible(true);

        // Add spacing between ShiftModelUI objects
        int spacing = 10;
        for (Shift shift : history) {
            ShiftModelUI shiftModelUI = new ShiftModelUI(shift);
            scrollablePanel.add(Box.createVerticalStrut(spacing));
            scrollablePanel.add(shiftModelUI);
        }

        // Refresh the scroll pane to reflect the changes
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    public void backButtonFunc() {
        new ManagerWindowUI();
    }



    // Helper method to check if the date format is valid
    private boolean isValidDateFormat(String date) {
        String pattern = "^(0[1-9]|1\\d|2\\d|3[01])/(0[1-9]|1[0-2])/\\d{4}$";
        return date.matches(pattern);
    }
    private List<Shift> performShiftHistoryImport(int branchId, LocalDate startDate, LocalDate endDate) {
        // Implement the logic to import shift history based on the given parameters
        // This is just a placeholder method
        return ShiftBoard.getInstance().reportShifts(branchId, startDate, endDate);
        // Return a dummy list of shift history
        //return List.of("Shift 1", "Shift 2", "Shift 3");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ImportShiftHistoryUI();
            }
        });
    }
}
