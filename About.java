import javax.swing.*;
import java.awt.*;

public class About extends JPanel {
    public About() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.white);
        JButton b1 = new JButton("button 1");
        JButton b2 = new JButton("button 2");
        JButton b3 = new JButton("button 3");
        JButton b4 = new JButton("button 4");
        JButton b5 = new JButton("button 5");
        JButton b6 = new JButton("button 6");

        
        Dimension buttonSize = new Dimension(Integer.MAX_VALUE, b1.getPreferredSize().height);

        b1.setMaximumSize(buttonSize);
        b2.setMaximumSize(buttonSize);
        b3.setMaximumSize(buttonSize);
        b4.setMaximumSize(buttonSize);
        b5.setMaximumSize(buttonSize);
        b6.setMaximumSize(buttonSize);

        add(b1);
        add(b2);
        add(b3);
        add(b4);
        add(b5);
        add(b6);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();

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
        frame.setJMenuBar(menuBar);

        // Create and add an instance of the About class
        About aboutPanel = new About();
        frame.getContentPane().add(aboutPanel);

        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
