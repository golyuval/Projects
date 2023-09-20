package Presentation.HRUI.templates.ScrollBar;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JScrollBar;

import static java.awt.SystemColor.scrollbar;

public class ScrollBarCustom extends JScrollBar {

    public ScrollBarCustom() {
        setUI(new ModernScrollBarUI());
        setPreferredSize(new Dimension(10, 10));
        setForeground(new Color(250, 200, 200));
        setBackground(Color.WHITE);
    }
}