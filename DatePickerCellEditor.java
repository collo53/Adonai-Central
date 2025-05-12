import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.EventObject;
import com.toedter.calendar.JDateChooser;

class DatePickerCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JDateChooser dateChooser;

    public DatePickerCellEditor() {
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd"); // Set the date format
        JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooser.getDateEditor();
        editor.setEditable(false); // Make the text field non-editable
        dateChooser.setPreferredSize(new Dimension(150, 30)); // Set preferred size
    }

    @Override
    public Object getCellEditorValue() {
        return dateChooser.getDate();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return dateChooser;
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        return true;
    }
}
