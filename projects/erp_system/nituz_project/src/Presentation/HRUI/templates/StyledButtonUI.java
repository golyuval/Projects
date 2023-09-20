package Presentation.HRUI.templates;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class StyledButtonUI extends BasicButtonUI {

    @Override
    public void installUI (JComponent c) {
        super.installUI(c);
        AbstractButton button = (AbstractButton) c;
        button.setOpaque(false);
        button.setBorder(new EmptyBorder(5, 15, 5, 15));
        button.setFont(new Font("Calibri", Font.PLAIN, 14));
        button.setBackground(new Color(0xC8EEFA));
        button.setForeground(Color.BLACK);
    }

    @Override
    public void paint (Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        paintBackground(g, b, b.getModel().isPressed() ? 2 : 0);
        super.paint(g, c);
    }

    private void paintBackground (Graphics g, JComponent c, int yOffset) {
        Dimension size = c.getSize();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(c.getBackground().darker());
        g.fillRoundRect(0, yOffset, size.width, size.height - yOffset, 10, 10);
        g.setColor(c.getBackground());
        g.fillRoundRect(0, yOffset, size.width, size.height + yOffset - 5, 10, 10);
    }

    public static void main (String[] args) {
        JFrame f = new JFrame("Button UI Test");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel p = new JPanel();
        p.setBackground(Color.white);
        f.setContentPane(p);
        p.setLayout(new FlowLayout());
        p.setBorder(new EmptyBorder(10, 10, 10, 10));

        for (int i = 1; i <= 5; i++) {
            final JButton button = new JButton("Button #" + i);

            // customize the button with your own look
            button.setUI(new StyledButtonUI());
            p.add(button);
        }

        f.pack();
        f.setLocation(500, 500);
        f.setVisible(true);
    }
}
