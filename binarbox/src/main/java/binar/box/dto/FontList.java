package binar.box.dto;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * An applet that displays the standard fonts and styles available in Java 1.1
 */
public class FontList extends JPanel {
    // The available font families
    String[] families = { "Serif", // "TimesRoman" in Java 1.0
            "SansSerif", // "Helvetica" in Java 1.0
            "Monospaced" }; // "Courier" in Java 1.0

    // The available font styles and names for each one
    int[] styles = { Font.PLAIN, Font.ITALIC, Font.BOLD,
            Font.ITALIC + Font.BOLD };

    String[] stylenames = { "Plain", "Italic", "Bold", "Bold Italic" };

    // Draw the applet.
    public void paint(Graphics g) {
        Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();

        int y=20;
        int x=20;
        for (Font font : fonts)
        {
            if( y % 600 == 0) {
                x += 170;
                y=0;
            }
            g.setFont(new Font(font.getName(), Font.BOLD, 11));
            g.drawString(font.getFontName(), x, y += 20); // display name
        }

    }

    public static void main(String[] a) {
        JFrame f = new JFrame();
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.setContentPane(new FontList());
        f.setSize(1400,700);
        f.setVisible(true);
    }
}