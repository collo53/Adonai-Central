import javax.swing.*;
import java.awt.*;

class About extends JFrame {
    About() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JButton b1 = new JButton("CURRICULUM");
        add(b1);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Home");
        JMenuItem menuItem1 = new JMenuItem("relationships");
        menu.add(menuItem1);
        JMenuItem menuItem2 = new JMenuItem("physical education");
        menu.add(menuItem2);
        JMenuItem menuItem3 = new JMenuItem("Development");
        menu.add(menuItem3);
        JMenuItem menuItem4 = new JMenuItem("language skills");
        menu.add(menuItem4);
        menuBar.add(menu);

        setJMenuBar(menuBar);

        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new About());
    }
}
