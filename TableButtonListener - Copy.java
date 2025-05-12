import java.util.EventListener;

public interface TableButtonListener extends EventListener {
    void tableButtonClicked(int row, int col);
}
