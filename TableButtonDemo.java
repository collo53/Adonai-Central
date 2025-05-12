import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TableButtonDemo {

    public static void main(String[] args) {
        JFrame frame = new JFrame("TableButton Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Define column names and table data
        String[] columnNames = {"Name", "Action"};
        Object[][] data = {
                {"Item 1", "Click Me"},
                {"Item 2", "Click Me"},
                {"Item 3", "Click Me"}
        };

        // Create table model
        DefaultTableModel model = new DefaultTableModel(data, columnNames);

        // Create table
        JTable table = new JTable(model);

        // Create a custom button for the "Action" column
        TableButton button = new TableButton("Click Me");

        // Add a listener to handle button clicks
        button.addTableButtonListener((row, col) -> {
            System.out.println("Button clicked at row " + row + ", column " + col);
        });

        // Set the button as both the cell renderer and editor for the "Action" column
        table.getColumnModel().getColumn(1).setCellRenderer(button);
        table.getColumnModel().getColumn(1).setCellEditor(button);

        // Add table to the frame
        frame.add(new JScrollPane(table));
        frame.setVisible(true);
    }
}
