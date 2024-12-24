package Presentation.HRUI.templates;

import defult.BusinessLayer.Controllers.HR.ShiftBoard;
import defult.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.Arrays;

public class BaseWindowUI extends JFrame {
    protected JButton backButton = new JButton("Back to Menu");;
    protected JButton saveBtn;

    public BaseWindowUI(String title, boolean saveButton) {
        setTitle(title);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.PINK);
        setLayout(new BorderLayout());

        JPanel mainPanel = createMainPanel(saveButton);
        add(mainPanel);

        pack();
        //setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createMainPanel(boolean saveButton) {
        Main.initData();
        //System.out.println(ShiftBoard.getInstance().EC.getEmployees().size()+": "+ Arrays.toString(ShiftBoard.getInstance().EC.getEmployees().keySet().toArray()));
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.PINK);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel titleLabel = new JLabel(getTitle());
        titleLabel.setFont(new Font("Serif", Font.BOLD, 25));
        mainPanel.add(titleLabel, gbc);

        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, gbc);

        if(saveButton) {
            saveBtn = createSaveButton();
            mainPanel.add(saveBtn, gbc);

        }
        backButton = new JButton("Back to Menu");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setPreferredSize(new Dimension(200, 50));
        backButton.setMaximumSize(new Dimension(200, 50));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backButtonFunc();
                dispose();

                // Add code to handle back button click and return to the main menu
            }
        });
        backButton.setUI(new StyledButtonUI());
        mainPanel.add(backButton, gbc);

//        saveBtn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                saveBtnFunc();
//                // Add code to handle back button click and return to the main menu
//            }
//        });

        return mainPanel;
    }

    public void backButtonFunc() {

    }

    void saveBtnFunc() {
    }

    protected JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.PINK);
        return panel;
    }

    protected void addButton(JButton button) {
        button.setUI(new StyledButtonUI());

        button.setPreferredSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(button);
        add(Box.createRigidArea(new Dimension(0, 10)));
    }


    protected void addTextField(JTextField textField) {
        textField.setPreferredSize(new Dimension(200, 30));
        textField.setMaximumSize(new Dimension(200, 30));
        add(textField);
        add(Box.createRigidArea(new Dimension(0, 10)));
    }

    protected void addComboField(JComboBox textField) {
        textField.setPreferredSize(new Dimension(200, 30));
        textField.setMaximumSize(new Dimension(200, 30));
        add(textField);
        add(Box.createRigidArea(new Dimension(0, 10)));
    }

    protected void addButton(JButton button, JPanel panel) {
        addButton(button);
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    protected void addTextField(JTextField textField, JPanel panel) {
        textField.setPreferredSize(new Dimension(200, 30));
        textField.setMaximumSize(new Dimension(200, 30));
        add(textField);
        add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(textField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    protected void addScrollPane(JScrollPane scrollPane, JPanel panel) {
        scrollPane.setPreferredSize(new Dimension(200, 30));
        scrollPane.setMaximumSize(new Dimension(200, 30));
        add(scrollPane);
        add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(scrollPane);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }


    protected JButton createSaveButton() {
        JButton saveButton = new JButton("Save");
        addButton(saveButton);
        return saveButton;
    }

    protected JPanel createGridPanel(int columns) {
        JPanel panel = new JPanel(new GridLayout(0, columns, 10, 10));
        panel.setBackground(Color.PINK);
        return panel;
    }
}
