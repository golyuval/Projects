package Presentation.HRUI.Models;

import Presentation.HRUI.templates.PanelRound;
import defult.BusinessLayer.HRsystem.Employee;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class EmployeeUI extends PanelRound {
    Employee employee;
    private JLabel driverLabel;
    private JLabel nameLabel;
    private JLabel idLabel;
    JDialog employeeDialog;
    private boolean selected;

    public EmployeeUI(Employee employee, boolean driver) {
        this.employee = employee;
        setRoundTopRight(30);
        setRoundTopLeft(30);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(100, 120));
        setBackground(Color.LIGHT_GRAY);


        ImageIcon userIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/user.png")));
        Image resizedImage = userIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        JLabel photoLabel = new JLabel(resizedIcon);
        photoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        driverLabel = new JLabel("DRIVER");
        nameLabel = new JLabel(employee.getName());
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        idLabel = new JLabel(String.valueOf(employee.getId()));
        idLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel infoPanel = new JPanel();

        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        infoPanel.setOpaque(false);
        infoPanel.add(photoLabel);
//        infoPanel.add(nameLabel);

//        infoPanel.add(idLabel);
        if(driver) addLabel(driverLabel, infoPanel);
        addLabel(nameLabel, infoPanel);
        addLabel(idLabel, infoPanel);
        add(infoPanel, BorderLayout.CENTER);

        // Add mouse listener to open detailed employee dialog on click
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showEmployeeDialog();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Color.PINK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Color.LIGHT_GRAY);
            }
        });


    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    void addLabel(JLabel label, JPanel panel){
       // label.setForeground(new Color(0x33, 0x35, 0x36));
        label.setForeground(new Color(77, 80, 82));
        label.setFont(Font.getFont("Calibri"));
        panel.add(label);

    }

    protected abstract void showEmployeeDialog();


    private Border createTitledBorder(String title) {
        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);
        Border emptyBorder = new EmptyBorder(5, 5, 5, 5);
        Border compoundBorder = new CompoundBorder(lineBorder, emptyBorder);
        return BorderFactory.createTitledBorder(compoundBorder, title, TitledBorder.LEFT, TitledBorder.TOP);
    }

    public static void main(String[] args) {
        // Example usage:
        Employee employee = new Employee("John Doe", 12345, 12345678, 5000,
                "Term 1, Term 2", LocalDate.now(),
                "Shift Manager, Storage", true,
                "2023-06-21,false, 2023-06-20,false",
                "Special Info", "2023-06-21,false, 2023-06-20,false",
                "password", "Week Shifts", "Bonuses", "Alert");

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        //panel.add(new EmployeeUI(employee));

        frame.add(panel);
        frame.setVisible(true);
    }


    public Employee getEmployee() {
        return employee;
    }
}
