import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.border.TitledBorder;

import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.table.TableRowSorter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.util.Rotation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.plot.PiePlot;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.jfree.data.general.PieDataset;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import com.formdev.flatlaf.icons.FlatSearchIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;


import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import java.awt.Color;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

import java.io.BufferedWriter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import javax.swing.plaf.basic.BasicButtonUI;



public class CakeShopPOS extends JFrame {
    private Inventory inventory;
    private Order order;
    private DefaultTableModel inventoryTableModel;
    private DefaultTableModel orderTableModel;
    private DefaultTableModel stationSalesTableModel;
    private DefaultTableModel progressTableModel;
    private DefaultTableModel bookingsTableModel;
    private JTable orderTable; // Made orderTable a class-level variable
    private DefaultTableModel topSellingCakesTableModel;
private JTable topSellingCakesTable;   
    private JComboBox<String> autocompleteComboBox;  // Declare here as an instance variable
    private JPanel authPanel;
    private JPanel mainPanel;

    private JTextField inventoryCakeNameField, inventoryCakePriceField, inventoryCakeQuantityField, inventoryDateField;
    private JTextField orderCakeNameField, orderCakeQuantityField;
    private JComboBox<String> stationDropdown;
    private DatabaseHelper dbHelper;
    private boolean isAuthenticated = false;
    private JSpinner dateSpinner; // Date picker for filtering sales
    private JTable progressTable; // Class-level declaration


// Inside createProgressPanel method


private JMenuBar createMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    JMenu themeMenu = new JMenu("Themes");
    themeMenu.setFont(new Font("Arial", Font.PLAIN, 14));

    JMenuItem lightThemeItem = new JMenuItem("Light Theme");
    lightThemeItem.addActionListener(e -> changeTheme("light"));
    themeMenu.add(lightThemeItem);

    JMenuItem darkThemeItem = new JMenuItem("Dark Theme");
    darkThemeItem.addActionListener(e -> changeTheme("dark"));
    themeMenu.add(darkThemeItem);

    JMenuItem intelliJThemeItem = new JMenuItem("IntelliJ Theme");
    intelliJThemeItem.addActionListener(e -> changeTheme("intellij"));
    themeMenu.add(intelliJThemeItem);

    JMenuItem darculaThemeItem = new JMenuItem("Darcula Theme");
    darculaThemeItem.addActionListener(e -> changeTheme("darcula"));
    themeMenu.add(darculaThemeItem);

    menuBar.add(themeMenu);

    return menuBar;
}
public class RoundedButtonUI extends BasicButtonUI {
    private final Color backgroundColor;

    public RoundedButtonUI(Color bgColor) {
        this.backgroundColor = bgColor;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        JButton button = (JButton) c;
        int width = button.getWidth();
        int height = button.getHeight();

        // Draw rounded background
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, width, height, 25, 25); // Change 25 for more or less curve

        super.paint(g, c);
    }
}
    public CakeShopPOS() {
    inventory = new Inventory();
    order = new Order();
    dbHelper = new DatabaseHelper();

    setTitle(" Version 1.0");
    setSize(1200, 1000); // Increase the size for better visibility
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setLocationRelativeTo(null); // Center the frame on the screen
    
    
    // Set a custom window icon
    setIconImage(new ImageIcon("Adonai.png").getImage());
    
    // Apply default FlatLaf theme (FlatLightLaf)
    try {
        UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
    } catch (Exception e) {
        e.printStackTrace();
    }
     initAuthPanel();
        mainPanel = new JPanel(new BorderLayout());
        
        // Show authentication panel initially
        setContentPane(authPanel);
        setVisible(true);
    }
   private void initAuthPanel() {
    authPanel = new JPanel(new GridBagLayout());
    authPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40)); // Increase padding
    authPanel.setBackground(UIManager.getColor("Panel.background"));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(12, 12, 12, 12); // More spacing between elements
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Title Label
    JLabel titleLabel = new JLabel("Login to Adonai");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 22)); // Larger font for emphasis
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setForeground(new Color(50, 50, 50)); // Darker text for visibility
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2; 
    authPanel.add(titleLabel, gbc);

    // Username label
    JLabel usernameLabel = new JLabel("Username:");
    usernameLabel.setFont(UIManager.getFont("Label.font"));
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    authPanel.add(usernameLabel, gbc);

    // Username field
    JTextField usernameField = new JTextField(15);
    usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
    usernameField.setBorder(BorderFactory.createCompoundBorder(
        usernameField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)
    )); // Adds padding inside text field
    gbc.gridx = 1;
    gbc.gridy = 1;
    authPanel.add(usernameField, gbc);

    // Password label
    JLabel passwordLabel = new JLabel("Password:");
    passwordLabel.setFont(UIManager.getFont("Label.font"));
    gbc.gridx = 0;
    gbc.gridy = 2;
    authPanel.add(passwordLabel, gbc);

    // Password field
    JPasswordField passwordField = new JPasswordField(15);
    passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
    passwordField.setBorder(BorderFactory.createCompoundBorder(
        passwordField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)
    ));
    gbc.gridx = 1;
    gbc.gridy = 2;
    authPanel.add(passwordField, gbc);

    // Login button
    JButton loginButton = new JButton("Login");
    loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
    loginButton.setBackground(new Color(70, 130, 180)); // Soft blue
    loginButton.setForeground(Color.WHITE);
    loginButton.setFocusPainted(false);
    loginButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20)); // Padding for button
    loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Add button with proper spacing
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    authPanel.add(loginButton, gbc);

    // Login action
    loginButton.addActionListener(e -> {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (authenticate(username, password)) {
            switchToMainPanel();
        } else {
            JOptionPane.showMessageDialog(authPanel, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
}



    private boolean authenticate(String username, String password) {
        // Replace with your actual authentication logic
        return "admin".equals(username) && "password".equals(password);
    }

    private void switchToMainPanel() {
        if (!isAuthenticated) {
            isAuthenticated = true;
            initializeMainPanel(); // Set up the main panel with components
            setContentPane(mainPanel);
            revalidate();
            repaint();
        }
    }

    private void initializeMainPanel() {

    // Create a tabbed pane with custom styling
    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.setFont(new Font("Arial", Font.BOLD, 16));
    tabbedPane.setBackground(new Color(30, 136, 129));
    tabbedPane.setForeground(Color.WHITE);
    tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Add tabs with icons
     FontIcon inventoryIcon = FontIcon.of(FontAwesomeSolid.BOX, 20, Color.WHITE);
    tabbedPane.addTab("Inventory",inventoryIcon,
            createInventoryPanel(), "Manage Inventory");
    FontIcon orderIcon = FontIcon.of(FontAwesomeSolid.SHOPPING_CART, 20, Color.WHITE);

    tabbedPane.addTab("Order",orderIcon,
            createOrderPanel(), "Create Orders");
    FontIcon dashboardIcon = FontIcon.of(FontAwesomeSolid.TACHOMETER_ALT, 20, Color.WHITE);

    tabbedPane.addTab("Dashboard",dashboardIcon,
            createDashboardPanel(), "View Sales Dashboard");

         FontIcon progressIcon = FontIcon.of(FontAwesomeSolid.SPINNER, 20, Color.WHITE);
    tabbedPane.addTab("Progress",progressIcon,
            createProgressPanel(tabbedPane), "Track Progress");

             FontIcon bookingIcon = FontIcon.of(FontAwesomeSolid.CALENDAR_ALT, 20, Color.WHITE);

tabbedPane.addTab("Bookings",bookingIcon,
            createBookingsPanel(), "Track Bookings");

    // Add a change listener for authentication check
    

    // Create and set up the menu bar for theme switching
    JMenuBar menuBar = createMenuBar();
    setJMenuBar(menuBar);

    // Create a custom status bar
    JPanel statusBar = new JPanel(new BorderLayout());
    statusBar.setBackground(new Color(119, 200, 219)); // Deep blue background for a modern look
    statusBar.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(52, 73, 94))); // Top border for separation

    // Add a status message label
    JLabel statusLabel = new JLabel(" ADONAI");
    statusLabel.setForeground(Color.RED); // White text for readability
    statusLabel.setFont(new Font("Monospaced", Font.BOLD, 20)); // Use a bold, modern font
    statusLabel.setHorizontalAlignment(SwingConstants.LEFT);

    // Add a clock label (dynamic time display)
    JLabel clockLabel = new JLabel();
    clockLabel.setForeground(Color.WHITE);
    clockLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
    clockLabel.setHorizontalAlignment(SwingConstants.RIGHT);

    // Add a timer to update the clock label every second
    Timer timer = new Timer(1000, e -> {
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a"));
        clockLabel.setText(currentDateTime);
    });
    timer.start();

    // Add the components to the status bar
    statusBar.add(statusLabel, BorderLayout.WEST); // Status message on the left
    statusBar.add(clockLabel, BorderLayout.EAST); // Clock on the right

    // Add components to the frame
    mainPanel.add(tabbedPane, BorderLayout.CENTER);
    mainPanel.add(statusBar, BorderLayout.SOUTH);

    // Set frame to be visible
    setVisible(true);
}
private void changeTheme(String theme) {
    try {
        switch (theme.toLowerCase()) {
            case "light":
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
                break;
            case "dark":
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
                break;
            case "intellij":
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatIntelliJLaf());
                break;
            case "darcula":
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarculaLaf());
                break;
            default:
                throw new IllegalArgumentException("Unknown theme: " + theme);
        }
        SwingUtilities.updateComponentTreeUI(this); // Refresh UI to apply the theme
    } catch (Exception e) {
        e.printStackTrace();
    }
}


private JPanel createInventoryPanel() {
    // Set the FlatLaf Light theme for consistent UI
    try {
        UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
    } catch (Exception e) {
        e.printStackTrace();
    }

    JPanel inventoryPanel = new JPanel(new BorderLayout());
    inventoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding around the panel
    inventoryPanel.setBackground(UIManager.getColor("Panel.background")); // Default FlatLight background

    // Create table model and table
    inventoryTableModel = new DefaultTableModel(new Object[]{"Name", "Price", "Quantity", "Date"}, 0);
    JTable inventoryTable = new JTable(inventoryTableModel) {
        @Override
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
            Component c = super.prepareRenderer(renderer, row, column);
            if (!isRowSelected(row)) {
                // Alternate row colors for better readability
                c.setBackground(row % 2 == 0 ? new Color(240, 240, 240) : new Color(225, 225, 225));

            }
            if (getSelectionModel().isSelectedIndex(row)) {
                    c.setBackground(new Color(180, 220, 255));
                }
            return c;
        }
    };

    // Make the table non-editable
    inventoryTable.setDefaultEditor(Object.class, null);

    // Customize table header
    JTableHeader header = inventoryTable.getTableHeader();
    header.setFont(UIManager.getFont("TableHeader.font"));
    header.setBackground(UIManager.getColor("TableHeader.background"));
    header.setForeground(UIManager.getColor("TableHeader.foreground"));

    // Center-align header text
    DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
    headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    for (int i = 0; i < inventoryTable.getColumnModel().getColumnCount(); i++) {
        inventoryTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
    }

    inventoryTable.setRowHeight(25);
    inventoryTable.setShowGrid(true);
    inventoryTable.setGridColor(UIManager.getColor("Table.gridColor"));
    inventoryTable.setFillsViewportHeight(true); // Ensure the table stretches to fill the viewport

    // Add table inside a scroll pane
    JScrollPane tableScrollPane = new JScrollPane(inventoryTable);
    tableScrollPane.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor"), 1));

    // Create button panel
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // Add space above buttons
    buttonPanel.setOpaque(false); // Match FlatLight background

    // Add styled buttons
        FontIcon addInventoryIcon = FontIcon.of(FontAwesome.PLUS, 20, Color.WHITE);

    JButton addInventoryButton = createStyledButton("Add Inventory", new Color(30, 136, 129), Color.WHITE);
    addInventoryButton.setIcon(addInventoryIcon);
    addInventoryButton.addActionListener(e -> openAddInventoryDialog());
    buttonPanel.add(addInventoryButton);

    FontIcon viewInventoryIcon = FontIcon.of(FontAwesome.EYE, 20, Color.WHITE);

    JButton viewInventoryButton = createStyledButton("View Inventory", new Color(30, 136, 129), Color.WHITE);
    viewInventoryButton.setIcon(viewInventoryIcon);
    viewInventoryButton.addActionListener(e -> updateInventoryTable());
    buttonPanel.add(Box.createHorizontalStrut(10)); // Space between buttons
    buttonPanel.add(viewInventoryButton);

    FontIcon downloadInventoryIcon = FontIcon.of(FontAwesome.DOWNLOAD, 20, Color.WHITE);

    JButton downloadInventoryButton = createStyledButton("Download Inventory", new Color(30, 136, 129), Color.WHITE);
    downloadInventoryButton.setIcon(downloadInventoryIcon);
    downloadInventoryButton.addActionListener(e -> downloadInventory());
    buttonPanel.add(Box.createHorizontalStrut(10)); // Space between buttons
    buttonPanel.add(downloadInventoryButton);

        FontIcon removeIcon = FontIcon.of(FontAwesome.TRASH, 20, Color.WHITE);

     JButton removebutton = createStyledButton("Clear", new Color(30, 136, 129), Color.WHITE);
     removebutton.setIcon(removeIcon);
     removebutton.addActionListener(e -> {
    int selectedRow = inventoryTable.getSelectedRow(); // Get the selected row

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Please select a cake to remove.");
        return;
    }

    String cakeName = (String) inventoryTable.getValueAt(selectedRow, 0); // Assuming name is in column 0

    int confirm = JOptionPane.showConfirmDialog(
        null,
        "Are you sure you want to remove '" + cakeName + "' from inventory?",
        "Confirm Deletion",
        JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        DatabaseHelper dbHelper = new DatabaseHelper();
        boolean success = dbHelper.removeCakeFromInventory(cakeName);

        if (success) {
            ((DefaultTableModel) inventoryTable.getModel()).removeRow(selectedRow); // Remove from UI
            JOptionPane.showMessageDialog(null, "Cake removed successfully.");
        } else {
            JOptionPane.showMessageDialog(null, "Failed to remove cake.");
        }
    }
});
      buttonPanel.add(Box.createHorizontalStrut(10)); // Space between buttons
    buttonPanel.add(removebutton);


    // Align the button panel at the bottom
    JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonContainer.setOpaque(false); // Match FlatLight background
    buttonContainer.add(buttonPanel);

    // Add components to the main panel
    inventoryPanel.add(tableScrollPane, BorderLayout.CENTER);
    inventoryPanel.add(buttonContainer, BorderLayout.SOUTH);

    return inventoryPanel;
}



// Helper method to create styled buttons
private JButton createStyledButton(String text, Color backgroundColor, Color textColor) {
    JButton button = new JButton(text);
    button.setBackground(backgroundColor);
    button.setForeground(textColor);
    button.setFocusPainted(false);
    button.setFont(new Font("SansSerif", Font.PLAIN, 16));
    button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Add padding inside the button
    return button;
}

private void openAddInventoryDialog() {
    JDialog addInventoryDialog = new JDialog(this, "➕ Add Inventory Item", true);
    addInventoryDialog.setSize(550, 500);
    addInventoryDialog.setLocationRelativeTo(this); // Center the dialog
    addInventoryDialog.setLayout(new BorderLayout(10, 10));
    addInventoryDialog.getContentPane().setBackground(new Color(245, 245, 245)); // Light gray background

    // Create main content panel with rounded edges
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new GridBagLayout());
    contentPanel.setBackground(Color.WHITE);
    contentPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200), 1), // Soft border
        BorderFactory.createEmptyBorder(20, 25, 20, 25) // Padding inside
    ));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.anchor = GridBagConstraints.WEST;

    // Custom label styling
    Color primaryColor = new Color(30, 136, 229); // Soft blue
    Font labelFont = new Font("Arial", Font.BOLD, 15);

    // Item Name field with autocomplete
    JLabel cakeNameLabel = new JLabel(" Item Name:");
    cakeNameLabel.setFont(labelFont);
    cakeNameLabel.setForeground(primaryColor);
    gbc.gridx = 0;
    gbc.gridy = 0;
    contentPanel.add(cakeNameLabel, gbc);

    inventoryCakeNameField = new JTextField();
    styleTextField(inventoryCakeNameField, "Enter item name...");
    gbc.gridx = 1;
    contentPanel.add(inventoryCakeNameField, gbc);

    // Autocomplete dropdown
    autocompleteComboBox = new JComboBox<>();
    autocompleteComboBox.setVisible(false);
    gbc.gridy = 1;
    contentPanel.add(autocompleteComboBox, gbc);

    inventoryCakeNameField.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            String input = inventoryCakeNameField.getText();
            updateAutocompleteSuggestions(input);
        }
    });

    // Item Price field
    JLabel cakePriceLabel = new JLabel(" Item Price:");

    cakePriceLabel.setFont(labelFont);
    cakePriceLabel.setForeground(primaryColor);
    gbc.gridx = 0;
    gbc.gridy = 2;
    contentPanel.add(cakePriceLabel, gbc);

    inventoryCakePriceField = new JTextField();
    styleTextField(inventoryCakePriceField, "Enter price...");
    gbc.gridx = 1;
    contentPanel.add(inventoryCakePriceField, gbc);

    // Item Quantity field
    JLabel cakeQuantityLabel = new JLabel(" Item Quantity:");
    cakeQuantityLabel.setFont(labelFont);
    cakeQuantityLabel.setForeground(primaryColor);
    gbc.gridx = 0;
    gbc.gridy = 3;
    contentPanel.add(cakeQuantityLabel, gbc);

    inventoryCakeQuantityField = new JTextField();
    styleTextField(inventoryCakeQuantityField, "Enter quantity...");
    gbc.gridx = 1;
    contentPanel.add(inventoryCakeQuantityField, gbc);

    // Button panel with modern buttons
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
    buttonPanel.setBackground(new Color(245, 245, 245));

    FontIcon addIcon = FontIcon.of(FontAwesome.PLUS, 20, Color.WHITE);

         // 🟢 Add Button with Icon
    JButton addButton = createStyledButton15(" Add", new Color(46, 204, 113)); // Red
            addButton.setIcon(addIcon); // Set FontAwesome icon
            addButton.setFont(new Font("Arial", Font.BOLD, 14));
            addButton.setBackground(new Color(46, 204, 113)); // Green
            addButton.setForeground(Color.WHITE);
            addButton.setFocusPainted(false);
            addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    addButton.addActionListener(e -> {
        addCakeToInventory();
        addInventoryDialog.dispose();
    });

    FontIcon cancelIcon = FontIcon.of(FontAwesome.TRASH, 20, Color.WHITE);
    JButton cancelButton = createStyledButton15(" Cancel", new Color(231, 76, 60)); // Red
    cancelButton.setIcon(cancelIcon);
    cancelButton.addActionListener(e -> addInventoryDialog.dispose());

    buttonPanel.add(addButton);
    buttonPanel.add(cancelButton);

    // Add components to dialog
    addInventoryDialog.add(contentPanel, BorderLayout.CENTER);
    addInventoryDialog.add(buttonPanel, BorderLayout.SOUTH);
    addInventoryDialog.setVisible(true);
}

// Utility method to style text fields
private void styleTextField(JTextField textField, String placeholder) {
    textField.setPreferredSize(new Dimension(250, 35));
    textField.setFont(new Font("Arial", Font.PLAIN, 14));
    textField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(189, 195, 199), 1), // Light gray border
        BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding inside
    ));

    // Placeholder effect
    textField.setForeground(Color.GRAY);
    textField.setText(placeholder);
    textField.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            if (textField.getText().equals(placeholder)) {
                textField.setText("");
                textField.setForeground(Color.BLACK);
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (textField.getText().isEmpty()) {
                textField.setText(placeholder);
                textField.setForeground(Color.GRAY);
            }
        }
    });
}

// Utility method to create modern buttons
private JButton createStyledButton15(String text, Color color) {
    JButton button = new JButton(text);
    button.setFont(new Font("Arial", Font.BOLD, 14));
    button.setBackground(color);
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    button.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(color.darker(), 2), 
        BorderFactory.createEmptyBorder(8, 20, 8, 20)
    ));

    // Add hover effect
    button.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            button.setBackground(color.darker());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            button.setBackground(color);
        }
    });

    return button;
}


private void updateAutocompleteSuggestions(String input) {
    DatabaseHelper dbHelper = new DatabaseHelper();
    autocompleteComboBox.removeAllItems();

    if (input.isEmpty()) {
        autocompleteComboBox.setVisible(false);
        return;
    }

    // Assume a method getSuggestionsFromDatabase that returns matching results as a Vector
    
    Vector<String> suggestions = dbHelper.getSuggestionsFromDatabase(input);

    for (String suggestion : suggestions) {
        autocompleteComboBox.addItem(suggestion);
    }

    if (!suggestions.isEmpty()) {
        autocompleteComboBox.setVisible(true);
    } else {
        autocompleteComboBox.setVisible(false);
    }

    // Handle selection from ComboBox
    autocompleteComboBox.addActionListener(e -> {
        String selectedItem = (String) autocompleteComboBox.getSelectedItem();
        if (selectedItem != null) {
            inventoryCakeNameField.setText(selectedItem);
            autocompleteComboBox.setVisible(false);
        }
    });
}

private JPanel createOrderPanel() {
    JPanel orderPanel = new JPanel(new BorderLayout());
    orderPanel.setBackground(new Color(247, 249, 252)); // Soft background color

    // Main content panel with a vertical layout
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    contentPanel.setBackground(Color.WHITE);
    contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Padding

    // Input panel for date selection and actions
    JPanel inputPanel = new JPanel(new GridBagLayout());
    inputPanel.setBackground(new Color(230, 240, 255)); // Light pastel blue background
    inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(180, 180, 200)), "Select Date & Actions"));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 20, 10, 20);
    gbc.gridx = 0;
    gbc.gridy = GridBagConstraints.RELATIVE;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel dateLabel = new JLabel("Select Date:");
    dateLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

    JDateChooser dateChooser = new JDateChooser();
    dateChooser.setPreferredSize(new Dimension(180, 30));

    inputPanel.add(dateLabel, gbc);
    inputPanel.add(dateChooser, gbc);

    // Buttons with modern colors
    JButton showSalesButton = createStyledButton2("Show Total Sales", new Color(45, 140, 200), Color.WHITE);
    JButton addStationButton = createStyledButton2("Add New Station", new Color(34, 166, 120), Color.WHITE);

    inputPanel.add(showSalesButton, gbc);
    inputPanel.add(addStationButton, gbc);

    // Panel for station buttons with padding and clean grid layout
    JPanel stationButtonPanel = new JPanel(new GridLayout(0, 2, 15, 15)); // 2 columns with spacing
    stationButtonPanel.setBackground(Color.WHITE);
    stationButtonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(180, 180, 200)), "Station Overview"));

    // Load stations dynamically
    DatabaseHelper dbHelper = new DatabaseHelper();
    List<StationSales> stationSalesList = dbHelper.loadStationsFromDatabase();

    for (StationSales stationSale : stationSalesList) {
        JButton stationButton = createStationButton(stationSale);
        stationButton.setPreferredSize(new Dimension(250, 150));
        stationButtonPanel.add(stationButton);
    }

    // Add components to the content panel
    contentPanel.add(inputPanel);
    contentPanel.add(Box.createVerticalStrut(10)); // Space between sections
    contentPanel.add(stationButtonPanel);

    // Wrap the content panel in a scroll pane for better UX
    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());

    orderPanel.add(scrollPane, BorderLayout.CENTER);

    // ActionListener for "Show Total Sales" button
    showSalesButton.addActionListener(e -> {
        java.util.Date selectedDate = dateChooser.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(orderPanel, "Please select a date.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
        for (Component component : stationButtonPanel.getComponents()) {
            if (component instanceof JButton) {
                JButton stationButton = (JButton) component;
                JPanel buttonContent = (JPanel) stationButton.getComponent(0); 
                JLabel stationNameLabel = (JLabel) buttonContent.getComponent(0);
                JLabel totalSalesLabel = (JLabel) buttonContent.getComponent(1);

                String stationName = stationNameLabel.getText();
                double totalSales = dbHelper.fetchTotalSales(stationName, sqlDate);
                totalSalesLabel.setText("Total Sales: " + totalSales);
            }
        }
    });

    // ActionListener for "Add New Station" button
    addStationButton.addActionListener(e -> {
        JTextField stationNameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] message = {
            "Enter Station Name:", stationNameField,
            "Enter Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add New Station", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String stationName = stationNameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (!stationName.isEmpty() && !password.isEmpty()) {
                dbHelper.addStationToDatabase(stationName, 0.0, password);

                JButton newStationButton = createStationButton(new StationSales(stationName, 0.0, new java.util.Date()));
                newStationButton.setPreferredSize(new Dimension(250, 150));
                stationButtonPanel.add(newStationButton);
                stationButtonPanel.revalidate();
                stationButtonPanel.repaint();

                addStationToProgressTable(stationName);
            } else {
                JOptionPane.showMessageDialog(null, "Station name and password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    return orderPanel;
}

// Helper method to create styled buttons with a hover effect
private JButton createStyledButton2(String text, Color backgroundColor, Color textColor) {
    JButton button = new JButton(text);
    button.setBackground(backgroundColor);
    button.setForeground(textColor);
    button.setFont(new Font("SansSerif", Font.PLAIN, 14));
    button.setFocusPainted(false);
    button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Padding
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Rounded corners and border
    button.setBorder(BorderFactory.createLineBorder(backgroundColor.darker()));

    // Hover effect
    button.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            button.setBackground(backgroundColor.darker());
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent evt) {
            button.setBackground(backgroundColor);
        }
    });

    return button;
}



// Method to create a station button with initial total sales set to 0.0
// Method to create a station button with initial total sales set to 0.0
private JButton createStationButton(StationSales stationSale) {
    JButton stationButton = new JButton();
    stationButton.setLayout(new BorderLayout());
    stationButton.setPreferredSize(new Dimension(300, 150));
    stationButton.setFont(new Font("Arial", Font.BOLD, 18));
    
    Color primaryColor = new Color(45, 140, 200); // Modern sky blue
Color hoverColor = new Color(25, 120, 180);  // Darker shade for hover

stationButton.setPreferredSize(new Dimension(300, 150));
stationButton.setFont(new Font("Arial", Font.BOLD, 18));
stationButton.setForeground(Color.WHITE);
stationButton.setBackground(primaryColor);
stationButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
stationButton.setContentAreaFilled(false);
stationButton.setOpaque(false);

// Add hover effect
stationButton.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseEntered(MouseEvent e) {
        stationButton.setBackground(hoverColor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        stationButton.setBackground(primaryColor);
    }
});

    // Panel inside button for content
    JPanel stationButtonContent = new JPanel(new BorderLayout());
    Color contentBackground = new Color(240, 248, 255); // Soft pastel blue
    Color contentHover = new Color(225, 235, 245);

    stationButtonContent.setBackground(contentBackground);
    stationButtonContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Add hover effect for the content panel
    stationButton.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            stationButtonContent.setBackground(contentHover);
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent evt) {
            stationButtonContent.setBackground(contentBackground);
        }
    });

    // Labels
    JLabel stationNameLabel = new JLabel(stationSale.getStationName(), SwingConstants.CENTER);
    stationNameLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
    stationNameLabel.setForeground(new Color(50, 50, 50)); // Darker text for contrast

    JLabel totalSalesLabel = new JLabel("Total Sales: 0.0", SwingConstants.CENTER);
    totalSalesLabel.setFont(new Font("Arial", Font.PLAIN, 16));
    totalSalesLabel.setForeground(new Color(80, 80, 80)); 

    // Buttons inside station panel
        FontIcon placeOrderIcon = FontIcon.of(FontAwesomeSolid.SHOPPING_CART, 20, Color.WHITE);
        FontIcon cancelOrderIcon = FontIcon.of(FontAwesomeSolid.TAGS, 20, Color.WHITE);

    JButton placeOrderButton = createStyledButton3("Item Distributed");
    placeOrderButton.setIcon(placeOrderIcon);

    JButton cancelOrderButton = createStyledButton3("Item Sold");
    cancelOrderButton.setIcon(cancelOrderIcon);

    JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
    buttonPanel.setBackground(contentBackground);
    buttonPanel.add(placeOrderButton);
    buttonPanel.add(cancelOrderButton);

    // Assemble components
    stationButtonContent.add(stationNameLabel, BorderLayout.NORTH);
    stationButtonContent.add(totalSalesLabel, BorderLayout.CENTER);
    stationButtonContent.add(buttonPanel, BorderLayout.SOUTH);

    stationButton.add(stationButtonContent, BorderLayout.CENTER);

    // ActionListener for station buttons
    placeOrderButton.addActionListener(e -> openCakeDistributionDialog(stationSale.getStationName()));
    cancelOrderButton.addActionListener(e -> openItemsSoldDialog(stationSale.getStationName()));

    return stationButton;
}

// Styled button helper method
private JButton createStyledButton3(String text) {
    JButton button = new JButton(text);
    Color buttonColor = new Color(0, 172, 193);  // Light turquoise
    Color hoverButtonColor = new Color(0, 142, 163); 

    button.setBackground(buttonColor);
    button.setForeground(Color.WHITE);
    button.setFont(new Font("SansSerif", Font.PLAIN, 14));
    button.setFocusPainted(false);
    button.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Hover effect
    button.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            button.setBackground(hoverButtonColor);
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent evt) {
            button.setBackground(buttonColor);
        }
    });

    return button;
}

 private void openCakeDistributionDialog(String stationName) {
    JDialog distributionDialog = new JDialog(this, "Item Distribution for " + stationName, true);
    distributionDialog.setSize(800, 800);
    distributionDialog.setLocationRelativeTo(this); // Center the dialog

    // Create the table model for distribution
    JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JLabel dateLabel = new JLabel("Select Date:");
    JDateChooser dateChooser = new JDateChooser();
    dateChooser.setDate(new java.sql.Date(System.currentTimeMillis())); // Set default to today's date

    datePanel.add(dateLabel);
    datePanel.add(dateChooser);
    distributionDialog.add(datePanel, BorderLayout.NORTH);

    DefaultTableModel distributionTableModel = new DefaultTableModel(
        new Object[]{"Item Name", "Quantity", "Distribution Date"}, 0
    );
    JTable distributionTable = new JTable(distributionTableModel){

        @Override
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
            Component c = super.prepareRenderer(renderer, row, column);
            if (!isRowSelected(row)) {
                // Alternate row colors for better readability
                c.setBackground(row % 2 == 0 ? new Color(240, 240, 240) : new Color(225, 225, 225));

            }
            if (getSelectionModel().isSelectedIndex(row)) {
                    c.setBackground(new Color(180, 220, 255));
                }
            return c;
        }
    };




    // Customize table appearance with Flat design
    JTableHeader header = distributionTable.getTableHeader();
    header.setFont(new Font("Arial", Font.BOLD, 14));
    header.setBackground(new Color(30, 136, 129)); // Blue header
    header.setForeground(Color.WHITE);
    distributionTable.setRowHeight(30); // Increased row height
    distributionTable.setShowGrid(true);
    distributionTable.setGridColor(new Color(220, 220, 220)); // Soft grid lines
    distributionTable.setDefaultEditor(Object.class, null); // Disable editing

    // Wrap the table with a scroll pane
    JScrollPane scrollPane = new JScrollPane(distributionTable);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    distributionDialog.add(scrollPane, BorderLayout.CENTER);

    // Button panel for Add and View Distribution buttons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBackground(new Color(245, 245, 245)); // Light background
        FontIcon addDistributionIcon = FontIcon.of(FontAwesomeSolid.PLUS, 20, Color.WHITE);
        FontIcon viewDistributionIcon = FontIcon.of(FontAwesomeSolid.EYE, 20, Color.WHITE);
        FontIcon clearDistributionIcon = FontIcon.of(FontAwesomeSolid.TRASH, 20, Color.WHITE);

    JButton addDistributionButton = createFlatButton("Add Item Distribution", new Color(30, 136, 129), e -> openAddCakeDistributionDialog(stationName));
    buttonPanel.add(addDistributionButton);
    addDistributionButton.setIcon(addDistributionIcon);

JButton viewDistributionButton = createFlatButton("View Item Distribution", new Color(30, 136, 129), e -> {
    java.sql.Date selectedDate = new java.sql.Date(System.currentTimeMillis());
    updateDistributionTable(distributionTableModel, stationName, selectedDate);
});
    buttonPanel.add(viewDistributionButton);
    viewDistributionButton.setIcon(viewDistributionIcon);

    JButton clearDistributionButton = createFlatButton("Clear Items Distributed", 
    new Color(30, 136, 129), 
    e -> {
        int selectedRow = distributionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to clear.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cakeName = distributionTable.getValueAt(selectedRow, 0).toString();
        int currentQuantity = Integer.parseInt(distributionTable.getValueAt(selectedRow, 1).toString()); // Assuming quantity is in the 2nd column

        String input = JOptionPane.showInputDialog(this, 
            "Enter quantity to delete for " + cakeName + " (Max: " + currentQuantity + "):", 
            "Delete Quantity", JOptionPane.PLAIN_MESSAGE);

        if (input != null) {
            try {
                int quantityToDelete = Integer.parseInt(input);

                if (quantityToDelete > 0 && quantityToDelete <= currentQuantity) {
                    DatabaseHelper dbHelper = new DatabaseHelper();
                    boolean success = dbHelper.deleteDistributedCakes(stationName, cakeName, quantityToDelete);

                    if (success) {
                        JOptionPane.showMessageDialog(this, "Deleted " + quantityToDelete + " from " + cakeName);

java.sql.Date todayDate = new java.sql.Date(System.currentTimeMillis());
updateDistributionTable(distributionTableModel, stationName, todayDate);
                    } else {
                        JOptionPane.showMessageDialog(this, "Error updating the database.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid quantity entered.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
);
    clearDistributionButton.setIcon(clearDistributionIcon);
buttonPanel.add(clearDistributionButton);

    distributionDialog.add(buttonPanel, BorderLayout.SOUTH);

    // Load the distribution data for the station
  dateChooser.addPropertyChangeListener("date", evt -> {
    java.util.Date utilDate = dateChooser.getDate();
    
    // Declare selectedDate outside the if-block to ensure scope visibility
    java.sql.Date selectedDate = null;

    if (utilDate != null) {
        selectedDate = new java.sql.Date(utilDate.getTime()); // Convert to SQL Date
        updateDistributionTable(distributionTableModel, stationName, selectedDate);
    } else {
        JOptionPane.showMessageDialog(this, "Please select a date first.", "Warning", JOptionPane.WARNING_MESSAGE);
    }
});

    distributionDialog.setVisible(true);
}

private void openItemsSoldDialog(String stationName) {
    JDialog salesDialog = new JDialog(this, "Item Sold for " + stationName, true);
    salesDialog.setSize(800, 800);
    salesDialog.setLocationRelativeTo(this); // Center the dialog

    // Create the table model for sales
    DefaultTableModel salesTableModel = new DefaultTableModel(
        new Object[]{"Item Name", "Quantity", "Update Time"}, 0
    );
    JTable salesTable = new JTable(salesTableModel){
        @Override
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
            Component c = super.prepareRenderer(renderer, row, column);
            if (!isRowSelected(row)) {
                // Alternate row colors for better readability
                c.setBackground(row % 2 == 0 ? new Color(240, 240, 240) : new Color(225, 225, 225));

            }
            if (getSelectionModel().isSelectedIndex(row)) {
                    c.setBackground(new Color(180, 220, 255));
                }
            return c;
        }
    };

    // Customize table appearance with Flat design
    JTableHeader header = salesTable.getTableHeader();
    header.setFont(new Font("Arial", Font.BOLD, 14));
    header.setBackground(new Color(30, 136, 129)); // Blue header
    header.setForeground(Color.WHITE);
    salesTable.setRowHeight(30); // Increased row height
    salesTable.setShowGrid(true);
    salesTable.setGridColor(new Color(220, 220, 220)); // Soft grid lines
    salesTable.setDefaultEditor(Object.class, null); // Disable editing

    // Wrap the table with a scroll pane
    JScrollPane scrollPane = new JScrollPane(salesTable);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    salesDialog.add(scrollPane, BorderLayout.CENTER);

    // Button panel for Add and View Sales buttons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBackground(new Color(245, 245, 245)); // Light background

    FontIcon viewSalesIcon = FontIcon.of(FontAwesomeSolid.EYE, 20, Color.WHITE);

    JButton viewSalesButton = createFlatButton("View Items Sold", new Color(30, 136, 129), e -> updateSalesTable(salesTableModel, stationName));
    viewSalesButton.setIcon(viewSalesIcon);
    buttonPanel.add(viewSalesButton);


    salesDialog.add(buttonPanel, BorderLayout.SOUTH);

    // Load the sales data for the station
    updateSalesTable(salesTableModel, stationName);

    salesDialog.setVisible(true);
}

// Helper method to create flat styled buttons
private JButton createFlatButton(String text, Color bgColor, ActionListener actionListener) {
    JButton button = new JButton(text);
    button.setFont(new Font("Arial", Font.BOLD, 14));
    button.setBackground(bgColor); 
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    button.setBorder(BorderFactory.createEmptyBorder(10,20,10,20)); // Thin border for a flat look
    
    button.setPreferredSize(new Dimension(180, 40)); // Set preferred size for the button
    button.setOpaque(false);
    button.setContentAreaFilled(false);
    button.setUI(new RoundedButtonUI(bgColor));
    button.addActionListener(actionListener);
    return button;
}


private void updateDistributionTable(DefaultTableModel distributionTableModel, String stationName, java.sql.Date selectedDate) {
        System.out.println("Updating table for station: " + stationName + " and date: " + selectedDate);

    distributionTableModel.setRowCount(0); // Clear the table before adding new data

    DatabaseHelper dbHelper = new DatabaseHelper();
    List<CakeDistributed> distributedCakes = dbHelper.loadCakesDistributedForStation(stationName, selectedDate); 
         if (distributedCakes.isEmpty()) {
        System.out.println("No data found for this date!");
    } else {
    for (CakeDistributed cake : distributedCakes) {
        distributionTableModel.addRow(new Object[]{
            cake.getCakeName(),
            cake.getQuantity(),
            cake.getDistributionDate()
        });
    }
  }
}

private void updateSalesTable(DefaultTableModel salesTableModel, String stationName) {
    salesTableModel.setRowCount(0);  // Clear the table

    DatabaseHelper dbHelper = new DatabaseHelper();
    List<ItemsSold> itemsSold = dbHelper.loadItemsSoldForStation(stationName); // Load for specific station

    for (ItemsSold cake : itemsSold) {
        salesTableModel.addRow(new Object[]{
            cake.getCakeName(),
            cake.getQuantity(),
            cake.getUpdateTime()
        });
    }
}
private void openAddCakeDistributionDialog(String stationName) {
    JDialog addDistributionDialog = new JDialog(this, "Add Item Distribution for " + stationName, true);
    addDistributionDialog.setSize(400, 300);
    addDistributionDialog.setLocationRelativeTo(this); // Center the dialog

    // Create content panel with GridBagLayout for better control
    JPanel contentPanel = new JPanel(new GridBagLayout());
    contentPanel.setBackground(new Color(245, 245, 245)); // Light background color
    contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

    // GridBagConstraints for positioning components
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between components
    gbc.anchor = GridBagConstraints.WEST; // Align components to the left

    // Item Name ComboBox
    contentPanel.add(new JLabel("Item Name:"), gbc);
    String[] cakeNames = fetchCakeNames(); // Method to fetch cake names from the database
    JComboBox<String> cakeNameComboBox = new JComboBox<>(cakeNames);
    cakeNameComboBox.setEditable(true); // Make the combo box editable
    cakeNameComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
    gbc.gridx = 1;
    contentPanel.add(cakeNameComboBox, gbc);

    // Quantity TextField
    contentPanel.add(new JLabel("Quantity:"), gbc);
    JTextField quantityField = new JTextField();
    quantityField.setFont(new Font("Arial", Font.PLAIN, 16));
    gbc.gridx = 1;
    contentPanel.add(quantityField, gbc);

    // Button panel for Add and Cancel buttons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.setBackground(new Color(245, 245, 245)); // Match background color

    JButton addButton = createFlatButton("Add Distribution", e -> {
        String cakeName = (String) cakeNameComboBox.getSelectedItem();
        int quantity = Integer.parseInt(quantityField.getText());

        // Add the distribution record to the database
        DatabaseHelper dbHelper = new DatabaseHelper();
        dbHelper.addCakesDistributed(stationName, cakeName, quantity); // Remove the date parameter

        addDistributionDialog.dispose();
    });
    buttonPanel.add(addButton);

    JButton cancelButton = createFlatButton("Cancel", e -> addDistributionDialog.dispose());
    buttonPanel.add(cancelButton);

    // Add content panel and button panel to dialog
    addDistributionDialog.add(contentPanel, BorderLayout.CENTER);
    addDistributionDialog.add(buttonPanel, BorderLayout.SOUTH);

    addDistributionDialog.setVisible(true);
}




// Helper method to create flat styled buttons
private JButton createFlatButton(String text, ActionListener actionListener) {
    JButton button = new JButton(text);
    button.setFont(new Font("Arial", Font.BOLD, 14));
    button.setBackground(new Color(30, 136, 129)); // Blue background
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    button.setBorder(BorderFactory.createLineBorder(new Color(30, 136, 229))); // Thin border for flat look
    button.addActionListener(actionListener);
    button.setPreferredSize(new Dimension(180, 40)); // Set preferred size for consistency
    return button;
}

private String[] fetchCakeNames() {
    DatabaseHelper dbHelper = new DatabaseHelper();
    List<String> cakeNamesList = dbHelper.getAllCakeNames(); // Assuming you have this method in your DatabaseHelper
    return cakeNamesList.toArray(new String[0]); // Convert list to array
}






private JPanel createDashboardPanel() {
    // Apply FlatLaf Light theme
    try {
        UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
    } catch (Exception e) {
        e.printStackTrace();
    }

    JPanel dashboardPanel = new JPanel(new BorderLayout());
    dashboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding around the panel

    // Sales Graph Panel
    JPanel salesGraphPanel = createSalesGraphPanel();
    salesGraphPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")),
            "Sales Overview",
            SwingConstants.LEFT, SwingConstants.TOP, // Corrected the alignment constants
            UIManager.getFont("Label.font")));
    dashboardPanel.add(salesGraphPanel, BorderLayout.NORTH);

    // Top Selling Items Table Panel
   JPanel topSellingPanel = new JPanel(new BorderLayout());
topSellingPanel.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
        "🔥 Top Selling Items",
        TitledBorder.LEFT, TitledBorder.TOP,
        new Font("SansSerif", Font.BOLD, 16),
        new Color(30, 136, 129)));

topSellingCakesTableModel = new DefaultTableModel(new Object[]{"Item Name", "Quantity Sold", "Total Sales"}, 0);
JTable topSellingCakesTable = new JTable(topSellingCakesTableModel);

// Improve table styling
topSellingCakesTable.setRowHeight(30);
topSellingCakesTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
topSellingCakesTable.setGridColor(new Color(200, 200, 200));
topSellingCakesTable.setShowGrid(true);
topSellingCakesTable.setIntercellSpacing(new Dimension(10, 5));
topSellingCakesTable.setDefaultEditor(Object.class, null);


// Set table header styling
JTableHeader header = topSellingCakesTable.getTableHeader();
header.setFont(new Font("SansSerif", Font.BOLD, 14));
header.setBackground(new Color(30, 136, 129));
header.setForeground(Color.WHITE);
header.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

// Wrap table in a scroll pane with rounded corners
JScrollPane tableScrollPane = new JScrollPane(topSellingCakesTable);
tableScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
tableScrollPane.getViewport().setBackground(Color.WHITE);

// Add slight padding around the table
topSellingPanel.add(Box.createVerticalStrut(10), BorderLayout.NORTH);
topSellingPanel.add(tableScrollPane, BorderLayout.CENTER);
topSellingPanel.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);

dashboardPanel.add(topSellingPanel, BorderLayout.CENTER);


    // Pie Chart Panel for Popular Items
    JPanel pieChartPanel = new JPanel(new BorderLayout());
    pieChartPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

    JLabel pieChartLabel = new JLabel("Popular Items", JLabel.CENTER);
    pieChartLabel.setFont(UIManager.getFont("Label.font").deriveFont(Font.BOLD, 16));
    pieChartLabel.setBackground(new Color(30, 136, 129));

    pieChartPanel.add(pieChartLabel, BorderLayout.NORTH);

    // Create pie chart dataset
    DefaultPieDataset<String> pieDataset = new DefaultPieDataset<>();
    List<CakeSalesRecord> popularCakes = dbHelper.getCombinedPopularCakes(); // Assume this fetches data
    for (CakeSalesRecord cakeRecord : popularCakes) {
        pieDataset.setValue(cakeRecord.getCakeName(), cakeRecord.getQuantitySold());
    }

    // Create pie chart
    JFreeChart pieChart = ChartFactory.createPieChart(
            null, // No title
            pieDataset,
            true,  // Show legend
            true,  // Use tooltips
            false  // URLs not needed
    );

    PiePlot plot = (PiePlot) pieChart.getPlot();
    
    // Set background color and 3D effect (optional)
    plot.setBackgroundPaint(new Color(240, 240, 240)); // Light gray background
    plot.setOutlineVisible(false); // Hide outline

    // Custom colors for slices
    Color[] colors = {new Color(52, 152, 219), new Color(231, 76, 60), new Color(46, 204, 113), 
                      new Color(241, 196, 15), new Color(155, 89, 182)};
    PieRenderer.setPieColors(plot, colors);


    // Add shadow effect
    plot.setShadowPaint(new Color(150, 150, 150, 50)); // Soft shadow effect

    // Format labels
    plot.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
    plot.setLabelBackgroundPaint(Color.WHITE);
    plot.setLabelOutlinePaint(null);
    plot.setLabelShadowPaint(null);
    plot.setSimpleLabels(true); // Clean labels
    plot.setLabelGenerator(null); // Name, Value, Percentage
    plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: {1} ({2})")); // Show labels on hover

    // Move legend outside
    pieChart.getLegend().setPosition(RectangleEdge.RIGHT); // Legend on the right side

    // Create Chart Panel
    ChartPanel chartPanel = new ChartPanel(pieChart);
    chartPanel.setPreferredSize(new Dimension(400, 300));
    pieChartPanel.add(chartPanel, BorderLayout.CENTER);

    dashboardPanel.add(pieChartPanel, BorderLayout.EAST);

    // Sales Report Panel
    JPanel reportPanel = new JPanel(new BorderLayout());
reportPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                "Sales Report",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14),
                new Color(50, 50, 50)
        ),
        BorderFactory.createEmptyBorder(10, 10, 10, 10) // Inner padding
));

// Styled text area for better readability
JTextArea reportArea = new JTextArea();
reportArea.setEditable(false);
reportArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
reportArea.setBackground(new Color(245, 245, 245)); // Light gray background
reportArea.setForeground(new Color(50, 50, 50));
reportArea.setMargin(new Insets(10, 10, 10, 10)); // Inner padding
reportArea.setLineWrap(true);
reportArea.setWrapStyleWord(true);
reportArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Small padding

// Scroll pane with rounded corners
JScrollPane reportScrollPane = new JScrollPane(reportArea);
reportScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
reportScrollPane.setPreferredSize(new Dimension(300, 200));

reportPanel.add(reportScrollPane, BorderLayout.CENTER);

// Add to dashboard
dashboardPanel.add(reportPanel, BorderLayout.WEST);


    // Button Panel
    JButton generateReportButton = createStyledButton("Generate Report", new Color(52, 152, 219), Color.WHITE);
    generateReportButton.addActionListener(e -> generateSalesReport(reportArea));

    JButton downloadReportButton = createStyledButton("Download Sales Report", new Color(46, 204, 113), Color.WHITE);
    downloadReportButton.addActionListener(e -> downloadSalesReport());

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(generateReportButton);
    buttonPanel.add(downloadReportButton);
    buttonPanel.setOpaque(false);

    dashboardPanel.add(buttonPanel, BorderLayout.SOUTH);

    // Load data for the top-selling cakes table
    updateTopSellingCakesTable();

    return dashboardPanel;
}

// Custom renderer for pie chart colors
static class PieRenderer {
    public static void setPieColors(PiePlot plot, Color[] baseColors) {
        PieDataset dataset = plot.getDataset();
        if (dataset != null) {
            int i = 0;
            for (Object keyObj : dataset.getKeys()) {
                Comparable<?> key = (Comparable<?>) keyObj; // Cast Object to Comparable
                
                // Create a gradient color effect
                Color baseColor = baseColors[i % baseColors.length];
                Color gradientColor = new Color(
                        Math.min(baseColor.getRed() + 30, 255),
                        Math.min(baseColor.getGreen() + 30, 255),
                        Math.min(baseColor.getBlue() + 30, 255)
                );

                GradientPaint paint = new GradientPaint(0, 0, baseColor, 100, 100, gradientColor);
                plot.setSectionPaint(key, paint);
                i++;
            }
        }
    }
}



/**
 * Creates a styled button with the specified text, background, and text color.
 */



// Helper method to create modern-styled buttons
private JButton createStyledButton7(String text, Color backgroundColor, Color textColor) {
    JButton button = new JButton(text);
    button.setBackground(backgroundColor);
    button.setForeground(textColor);
    button.setFocusPainted(false);
    button.setFont(UIManager.getFont("Button.font"));
    button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    return button;
}



private JPanel createProgressPanel(JTabbedPane tabbedPane) {
    JPanel progressPanel = new JPanel(new BorderLayout());
    progressPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding around the panel

    // Initialize table model
    String[] columnNames = {"Station Name", "Action"};
    progressTableModel = new DefaultTableModel(columnNames, 0); // Start with an empty model

    // Create the table
    JTable progressTable = new JTable(progressTableModel);
    progressTable.setRowHeight(35); // Set row height for better readability
    progressTable.setDefaultEditor(Object.class, null);

    // Add button in the "Action" column
    TableButton actionButton = new TableButton("View");
    actionButton.addTableButtonListener((row, col) -> {
        // Get the station name from the selected row
        String stationName = (String) progressTableModel.getValueAt(row, 0);
        System.out.println("View clicked for station: " + stationName);

        // Create the station progress panel for the selected station
        JPanel stationProgressPanel = createStationProgressPanel(stationName, tabbedPane);

        // Add the new tab for the station view
        tabbedPane.addTab(stationName + " Progress", stationProgressPanel);

        // Switch to the new tab
        tabbedPane.setSelectedComponent(stationProgressPanel);
    });

    // Set up button renderer and editor
    progressTable.getColumnModel().getColumn(1).setCellRenderer(actionButton);
    progressTable.getColumnModel().getColumn(1).setCellEditor(actionButton);

    // Customize table appearance
    JTableHeader tableHeader = progressTable.getTableHeader();
    tableHeader.setFont(new Font("Arial", Font.BOLD, 14));
    tableHeader.setBackground(new Color(30, 136, 129)); // Modern, darker blue header
    tableHeader.setForeground(Color.WHITE);

    progressTable.setGridColor(new Color(220, 220, 220)); // Light grid color
    progressTable.setSelectionBackground(new Color(100, 149, 237)); // Blue selection background
    progressTable.setSelectionForeground(Color.WHITE); // White text on selection

    // Alternate row colors for better readability
    progressTable.setDefaultRenderer(Object.class, new TableCellRenderer() {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel(value.toString());
            label.setOpaque(true);
            if (isSelected) {
                label.setBackground(new Color(100, 149, 237)); // Blue for selected rows
                label.setForeground(Color.WHITE);
            } else {
                label.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE); // Alternating row colors
                label.setForeground(Color.BLACK);
            }
            return label;
        }
    });

    // Add table to a scroll pane
    JScrollPane tableScrollPane = new JScrollPane(progressTable);
    tableScrollPane.setPreferredSize(new Dimension(650, 350)); // Larger size for more visible content
    tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200))); // Add a subtle border

    // Add components to the progress panel
    JLabel titleLabel = new JLabel("Sales Progress", JLabel.CENTER);
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
    titleLabel.setForeground(new Color(30, 136, 129)); // Title with the same color as the table header
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // Add space around the label
    progressPanel.add(titleLabel, BorderLayout.NORTH);
    progressPanel.add(tableScrollPane, BorderLayout.CENTER);

    // Load stations into progress table when the panel is created
    loadStationsIntoProgressTable(); // This will load the stations when the panel is created

    return progressPanel;
}



// Method to add a new station


// Method to load stations into the progress table
private void loadStationsIntoProgressTable() {
    // Clear existing rows in the model
    progressTableModel.setRowCount(0);

    // Get the list of stations from the database
    DatabaseHelper dbHelper = new DatabaseHelper();
    List<StationSales> stations = dbHelper.loadStationsFromDatabase(); // Assuming this returns a list of StationSales objects

    // Add each station to the progress table model
    for (StationSales stationSale : stations) {
        addStationToProgressTable(stationSale.getStationName()); // Assuming getStationName() returns the station name
    }
}

// Method to add a station to the progress table
private void addStationToProgressTable(String stationName) {
    // Add a new row for the added station in the progress table model
    progressTableModel.addRow(new Object[]{stationName, "View"});
}



// Method to create a progress panel for the selected station

public JPanel createStationProgressPanel(String stationName, JTabbedPane tabbedPane) {
    JPanel stationPanel = new JPanel(new BorderLayout());
    stationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    stationPanel.setBackground(new Color(240, 240, 240));

    // Close button setup
    JButton closeButton = new JButton("Close");
    closeButton.setBackground(new Color(220, 53, 69));
    closeButton.setForeground(Color.WHITE);
    closeButton.setFocusPainted(false);
    closeButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    closeButton.setFont(new Font("Arial", Font.BOLD, 12));
    closeButton.addActionListener(e -> {
        int index = tabbedPane.indexOfComponent(stationPanel);
        if (index != -1) {
            tabbedPane.remove(index);
        }
    });

    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setBackground(new Color(30, 136, 129));
    headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    headerPanel.add(closeButton, BorderLayout.EAST);

    JLabel stationLabel = new JLabel("Progress for " + stationName, JLabel.CENTER);
    stationLabel.setFont(new Font("Arial", Font.BOLD, 18));
    stationLabel.setForeground(Color.WHITE);
    headerPanel.add(stationLabel, BorderLayout.CENTER);
    stationPanel.add(headerPanel, BorderLayout.NORTH);

    // --- Time Series Chart for Daily Items Sold ---
    TimeSeries itemsSoldSeries = new TimeSeries("Items Sold");

    List<ItemsSold> itemsSoldRecords = dbHelper.loadItemsSold();
    Map<Day, Integer> aggregatedItemsSold = new HashMap<>();

    for (ItemsSold record : itemsSoldRecords) {
        if (record.getStationName().equals(stationName)) {
            Day date = new Day(record.getUpdateTime());
            aggregatedItemsSold.merge(date, record.getQuantity(), Integer::sum);
        }
    }

    for (Map.Entry<Day, Integer> entry : aggregatedItemsSold.entrySet()) {
        itemsSoldSeries.addOrUpdate(entry.getKey(), entry.getValue());
    }

    TimeSeriesCollection itemsSoldDataset = new TimeSeriesCollection(itemsSoldSeries);
    JFreeChart itemsSoldChart = ChartFactory.createTimeSeriesChart(
            "Daily Items Sold Progress",
            "Date",
            "Quantity Sold",
            itemsSoldDataset,
            true,
            true,
            false
    );

    // --- Chart Styling ---
    XYPlot plot = (XYPlot) itemsSoldChart.getPlot();
    plot.setBackgroundPaint(new GradientPaint(0, 0, Color.WHITE, 0, 400, new Color(30, 136, 129)));
    plot.setDomainGridlinePaint(new Color(200, 200, 200));
    plot.setRangeGridlinePaint(new Color(200, 200, 200));

    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    renderer.setSeriesPaint(0, new Color(52, 152, 219));
    renderer.setSeriesStroke(0, new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    renderer.setDefaultShapesVisible(true);
    renderer.setDefaultShapesFilled(true);

    plot.setRenderer(renderer);

    DateAxis domainAxis = (DateAxis) plot.getDomainAxis();
    domainAxis.setDateFormatOverride(new SimpleDateFormat("MMM dd"));
    domainAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
    domainAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 10));

    NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    rangeAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
    rangeAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 10));
    rangeAxis.setAutoRangeIncludesZero(false);

    plot.setDomainCrosshairVisible(true);
    plot.setRangeCrosshairVisible(true);
    plot.setDomainCrosshairPaint(Color.RED);
    plot.setRangeCrosshairPaint(Color.RED);

    ChartPanel itemsSoldChartPanel = new ChartPanel(itemsSoldChart);
    itemsSoldChartPanel.setPreferredSize(new Dimension(700, 400));
    itemsSoldChartPanel.setMouseWheelEnabled(true);
    itemsSoldChartPanel.setPopupMenu(null);

    stationPanel.add(itemsSoldChartPanel, BorderLayout.CENTER);

    // --- Daily Sales Bar Chart ---
    Map<String, List<DailySales>> allSalesRecords = dbHelper.fetchDailySalesForAllStations(stationName);
    List<DailySales> dailySalesRecords = allSalesRecords.get(stationName);
    DefaultCategoryDataset dailySalesDataset = new DefaultCategoryDataset();

    if (dailySalesRecords != null) {
        for (DailySales salesRecord : dailySalesRecords) {
            String dateString = new SimpleDateFormat("yyyy-MM-dd").format(salesRecord.getSalesDate());
            dailySalesDataset.addValue(salesRecord.getTotalSales(), "Total Sales", dateString);
        }

        JFreeChart dailySalesChart = ChartFactory.createBarChart(
                "Daily Sales Progress",
                "Date",
                "Total Sales (Amount)",
                dailySalesDataset
        );

        CategoryPlot barPlot = dailySalesChart.getCategoryPlot();
        barPlot.setRangeGridlinePaint(new Color(200, 200, 200));
        barPlot.setBackgroundPaint(Color.WHITE);

        BarRenderer barRenderer = (BarRenderer) barPlot.getRenderer();
        barRenderer.setSeriesPaint(0, new Color(52, 152, 219));
        barRenderer.setBarPainter(new StandardBarPainter());

        ChartPanel dailySalesChartPanel = new ChartPanel(dailySalesChart);
        dailySalesChartPanel.setPreferredSize(new Dimension(700, 300));
        dailySalesChartPanel.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        stationPanel.add(dailySalesChartPanel, BorderLayout.SOUTH);
    }

    // --- Popular Cakes Pie Chart ---
   JPanel bottomPanel = new JPanel(new BorderLayout());
bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
bottomPanel.setBackground(new Color(240, 240, 240));

JLabel pieChartLabel = new JLabel("Popular Items", JLabel.CENTER);
pieChartLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Increased size
pieChartLabel.setForeground(new Color(30, 136, 129)); // Same as headers for consistency
pieChartLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
bottomPanel.add(pieChartLabel, BorderLayout.NORTH);

DefaultPieDataset<String> pieDataset = new DefaultPieDataset<>();
List<CakeSalesRecord> cakeSalesRecords = dbHelper.fetchPopularCakes(stationName);

for (CakeSalesRecord cakeRecord : cakeSalesRecords) {
    pieDataset.setValue(cakeRecord.getCakeName(), cakeRecord.getQuantitySold());
}

// Create 3D Pie Chart
JFreeChart pieChart = ChartFactory.createPieChart3D(
        null, pieDataset, true, true, false
);

PiePlot3D plotPie = (PiePlot3D) pieChart.getPlot();
plotPie.setStartAngle(290); 
plotPie.setDirection(Rotation.CLOCKWISE);
plotPie.setForegroundAlpha(0.75f);
plotPie.setBackgroundPaint(new Color(240, 240, 240));
plotPie.setOutlineVisible(false);

// Assign custom colors for better visibility
Color[] colors = {
        new Color(52, 152, 219),  // Blue
        new Color(231, 76, 60),   // Red
        new Color(46, 204, 113),  // Green
        new Color(241, 196, 15),  // Yellow
        new Color(155, 89, 182)   // Purple
};

int i = 0;
for (Comparable<?> key : pieDataset.getKeys()) {
    plotPie.setSectionPaint(key, colors[i % colors.length]);
    i++;
}

// Enhance labels
plotPie.setLabelFont(new Font("Arial", Font.BOLD, 14));
plotPie.setLabelBackgroundPaint(Color.WHITE);
plotPie.setLabelOutlinePaint(null);
plotPie.setLabelShadowPaint(null);
plotPie.setSimpleLabels(true);
plotPie.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2}%)"));

// Highlight effect on hover
plotPie.setExplodePercent(pieDataset.getKey(0), 0.1); // Slightly pop out the first section

ChartPanel pieChartPanel = new ChartPanel(pieChart);
pieChartPanel.setPreferredSize(new Dimension(500, 300));
pieChartPanel.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
bottomPanel.add(pieChartPanel, BorderLayout.CENTER);

stationPanel.add(bottomPanel, BorderLayout.SOUTH);
stationPanel.revalidate();
stationPanel.repaint();

    return stationPanel;
}
private  JPanel createBookingsPanel() {
        JPanel bookingsPanel = new JPanel();
        bookingsPanel.setLayout(new BorderLayout());
        bookingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bookingsPanel.setBackground(UIManager.getColor("Panel.background"));

        DefaultTableModel bookingsTableModel = new DefaultTableModel(
                new Object[]{"Id","Name", "Status", "Cake", "Quantity", "Date Booked", "Date Cleared", "Amount Paid", "Amount Left","Station"}, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");

        JTable bookingsTable = new JTable(bookingsTableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    // Alternate row colors for better readability
                c.setBackground(row % 2 == 0 ? new Color(240, 240, 240) : new Color(225, 225, 225));
                }
                return c;
            }
        };

       
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(bookingsTableModel);
         bookingsTable.setRowSorter(sorter); // Attach sorter to the table

        bookingsTable.setDefaultEditor(Object.class, null);
        JTableHeader header = bookingsTable.getTableHeader();
        header.setFont(UIManager.getFont("TableHeader.font"));
        header.setBackground(UIManager.getColor("TableHeader.background"));
        header.setForeground(UIManager.getColor("TableHeader.foreground"));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < bookingsTable.getColumnModel().getColumnCount(); i++) {
            bookingsTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        bookingsTable.setRowHeight(25);
        bookingsTable.setShowGrid(true);
        bookingsTable.setGridColor(new Color(220, 220, 220)); // Light grid color

        bookingsTable.setGridColor(UIManager.getColor("Table.gridColor"));
        bookingsTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(bookingsTable);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.setOpaque(false);

        FontIcon removeBookingIcon = FontIcon.of(FontAwesome.TRASH, 20, Color.WHITE);
        FontIcon viewBookingIcon = FontIcon.of(FontAwesome.EYE, 20, Color.WHITE);


        JButton clearbutton = createStyledButton("Clear", new Color(30, 136, 129), Color.WHITE);
        clearbutton.setIcon(removeBookingIcon);
        JButton viewbutton = createStyledButton("View", new Color(30, 136, 129), Color.WHITE);
        viewbutton.setIcon(viewBookingIcon);
        buttonPanel.add(clearbutton);
        buttonPanel.add(viewbutton);


        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        searchPanel.setOpaque(false);

         FontIcon searchBookingIcon = FontIcon.of(FontAwesome.SEARCH, 20, Color.WHITE);

        JTextField searchBar = new JTextField(15);
        JButton searchButton = createStyledButton("Search", new Color(30, 136, 129), Color.WHITE);
        searchButton.setIcon(searchBookingIcon);
        searchPanel.add(searchBar);
        searchPanel.add(searchButton);

        searchButton.addActionListener(e -> {
            String searchText = searchBar.getText().trim();

            if (!searchText.isEmpty()) {
                RowFilter<DefaultTableModel, Object> rowFilter = RowFilter.regexFilter("(?i)" + searchText, 1); // Column 0 is "Name"
                sorter.setRowFilter(rowFilter);
            } else {
                sorter.setRowFilter(null); // Remove filter when search text is empty
            }
        });


        
       clearbutton.addActionListener(e -> {
    int selectedRow = bookingsTable.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Please select a record to clear.", "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Convert row index to model index (important when using sorting/filtering)
    int modelRow = bookingsTable.convertRowIndexToModel(selectedRow);

    // Ensure we are fetching the ID column (adjust index accordingly)
    int idColumnIndex = 0; // Change this if "id" is in a different column
    Object idObj = bookingsTableModel.getValueAt(modelRow, idColumnIndex);

    if (idObj == null) {
        JOptionPane.showMessageDialog(null, "Invalid selection. Unable to find booking ID.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Convert ID to string and check if it's a valid integer
    String idStr = idObj.toString().trim();
    if (!idStr.matches("\\d+")) {  // Ensure it's only digits
        JOptionPane.showMessageDialog(null, "Invalid ID. Please select a valid record.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int id = Integer.parseInt(idStr); // Safe conversion

    // Confirm deletion with user
    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear this booking?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    // Call method to delete from database
    DatabaseHelper dbHelper = new DatabaseHelper();
    boolean success = dbHelper.deleteBooking(id);

    if (success) {
        // Remove from the table model
        bookingsTableModel.removeRow(modelRow);
        JOptionPane.showMessageDialog(null, "Booking cleared successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(null, "Failed to clear booking. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
    }
});




        bookingsPanel.add(scrollPane, BorderLayout.CENTER);
        bookingsPanel.add(buttonPanel, BorderLayout.SOUTH);
        bookingsPanel.add(searchPanel, BorderLayout.NORTH);
       

        
        // Call the method to load data
       loadBookingsData(bookingsTableModel);

        bookingsPanel.setVisible(true);

        return bookingsPanel;
    }
    private  void loadBookingsData(DefaultTableModel tableModel) {
        DatabaseHelper dbHelper = new DatabaseHelper();

        List<Booking> bookings = dbHelper.fetchBookingsForStation(); // Ensure this is returning the correct data

        // Debugging: Check if bookings are being fetched
        System.out.println("Fetched bookings for station " +  ": " + bookings.size() + " bookings found.");

        // Clear the table before adding new data
        tableModel.setRowCount(0);

        // Add rows to the table model
        for (Booking booking : bookings) {
            tableModel.addRow(new Object[]{
                    booking.getId(),
                    booking.getBookerName(),
                    booking.getStatus(),
                    booking.getCakeName(),
                    booking.getQuantity(),
                    booking.getDateBooked(),
                    booking.getDateCleared(),
                    booking.getAmountPaid(),
                    booking.getAmountLeft(),
                    booking.getStation() // Add station data if necessary
            });
        }
    }


private void updateTopSellingCakesTable() {
    topSellingCakesTableModel.setRowCount(0); // Clear previous data
    List<CakeSalesRecord> topSellingCakes = dbHelper.getTopSellingCakes(); // Fetch data

    for (CakeSalesRecord record : topSellingCakes) {
        double totalSales = record.getTotalSales(); // Access total sales directly from CakeSalesRecord
        topSellingCakesTableModel.addRow(new Object[]{record.getCakeName(), record.getQuantitySold(), totalSales});
        System.out.println("Added to Table: " + record.getCakeName() + ", " + record.getQuantitySold() + ", " + totalSales); // Debug line
    }
}



   private JPanel createSalesGraphPanel() {
    TimeSeriesCollection dataset = new TimeSeriesCollection();
    TimeSeries salesSeries = new TimeSeries("Total Sales");

    // Fetch sales data from the database
    List<StationSales> salesList = dbHelper.getCombinedStationSales();

    // Aggregate sales data by date
    Map<Day, Double> aggregatedSales = new HashMap<>();

    for (StationSales sale : salesList) {
        Day salesDate = new Day(sale.getSalesDate());
        double totalSales = sale.getTotalSales();

        // Aggregate sales
        aggregatedSales.merge(salesDate, totalSales, Double::sum);
    }

    // Add aggregated sales to TimeSeries
    for (Map.Entry<Day, Double> entry : aggregatedSales.entrySet()) {
        salesSeries.addOrUpdate(entry.getKey(), entry.getValue());
    }

    dataset.addSeries(salesSeries);

    // Create Chart
    JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "Total Sales Over Time",
            "Date",
            "Sales Amount",
            dataset,
            true,
            true,
            false
    );

    // Customize Chart Appearance
    XYPlot plot = (XYPlot) chart.getPlot();
    
    // Set a gradient background
    plot.setBackgroundPaint(new GradientPaint(0, 0, Color.WHITE, 0, 400, new Color(30, 136, 129)));

    plot.setDomainGridlinePaint(new Color(200, 200, 200)); // Light gray grid lines
    plot.setRangeGridlinePaint(new Color(200, 200, 200));

    // Line Renderer for smooth curves and custom markers
    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    renderer.setSeriesPaint(0, new Color(52, 152, 219)); // Blue Line
    renderer.setSeriesStroke(0, new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); // Smooth curve
    renderer.setDefaultShapesVisible(true); // Show data points
    renderer.setDefaultShapesFilled(true); // Fill data points

    plot.setRenderer(renderer);

    // Customize Axis
    DateAxis domainAxis = (DateAxis) plot.getDomainAxis();
    domainAxis.setDateFormatOverride(new SimpleDateFormat("MMM dd"));
    domainAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
    domainAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 10));

    NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    rangeAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
    rangeAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 10));
    rangeAxis.setAutoRangeIncludesZero(false); // Ensures better visualization

    // Enable tooltips and zooming
    chart.getXYPlot().setDomainCrosshairVisible(true);
    chart.getXYPlot().setRangeCrosshairVisible(true);
    chart.getXYPlot().setDomainCrosshairPaint(Color.RED);
    chart.getXYPlot().setRangeCrosshairPaint(Color.RED);

    // Chart Panel with Zooming and Mouse Wheel Scrolling
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new Dimension(700, 400));
    chartPanel.setMouseWheelEnabled(true);
    chartPanel.setPopupMenu(null); // Remove JFreeChart default menu

    return chartPanel;
}


   

    private void addCakeToInventory() {
        String name = inventoryCakeNameField.getText();
        double price = Double.parseDouble(inventoryCakePriceField.getText());
        int quantity = Integer.parseInt(inventoryCakeQuantityField.getText());
      
        Cake cake = new Cake(name, price, quantity);
        inventory.addCake(cake);

        dbHelper.addInventoryItem(cake);

        updateInventoryTable();
    }

   private void updateInventoryTable() {
        inventoryTableModel.setRowCount(0);
        List<Cake> cakes = dbHelper.getInventoryItems();
        for (Cake cake : cakes) {
            inventoryTableModel.addRow(new Object[]{cake.getName(), cake.getPrice(), cake.getQuantity(),cake.getInventoryDate()});
        }
        
    }

    private void updateStationSalesTable() {
        stationSalesTableModel.setRowCount(0);
        Date filterDate = new Date(((java.util.Date) dateSpinner.getValue()).getTime());
        List<StationSales> stationSalesList = dbHelper.getStationSales(filterDate);
        for (StationSales sale : stationSalesList) {
            stationSalesTableModel.addRow(new Object[]{sale.getStationName(), sale.getTotalSales(), sale.getSalesDate()});
        }
    }

 private void downloadOrders() {
    JFileChooser fileChooser = new JFileChooser();
    int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        if (!file.getName().endsWith(".csv")) {
            file = new File(file.getAbsolutePath() + ".csv");
        }
        
        // Fetch order data from the database
        DatabaseHelper dbHelper = new DatabaseHelper();
        List<Cake> orderList = dbHelper.getOrders();
        
        try (FileWriter writer = new FileWriter(file)) {
            // Write the header
            writer.write("Name,Price,Quantity,Total Price,Order Date\n");

            // Write each order item to the file
            for (Cake cake : orderList) {
                writer.write(cake.getName() + ",");
                writer.write(cake.getPrice() + ",");
                writer.write(cake.getQuantity() + ",");
                writer.write(cake.getTotalPrice() + ",");
                writer.write(cake.getOrderDate() + "\n");  // Assuming Cake has getOrderDate()
            }
            
            JOptionPane.showMessageDialog(this, "Order data saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving order data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


private void downloadInventory() {
    JFileChooser fileChooser = new JFileChooser();
    int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        if (!file.getName().endsWith(".csv")) {
            file = new File(file.getAbsolutePath() + ".csv");
        }
        
        // Fetch inventory data from the database
        DatabaseHelper dbHelper = new DatabaseHelper();
        List<Cake> inventoryList = dbHelper.getInventoryItems();
        
        try (FileWriter writer = new FileWriter(file)) {
            // Write the header
            writer.write("Name,Price,Quantity,Inventory Date\n");

            // Write each inventory item to the file
            for (Cake cake : inventoryList) {
                writer.write(cake.getName() + ",");
                writer.write(cake.getPrice() + ",");
                writer.write(cake.getQuantity() + ",");
                writer.write(cake.getInventoryDate() + "\n");  // Assuming Cake has getInventoryDate()
            }
            
            JOptionPane.showMessageDialog(this, "Inventory data saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving inventory data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

  
    

  private void generateSalesReport(JTextArea reportArea) {
    StringBuilder report = new StringBuilder();

    // Generate the report content
    report.append("Sales Report\n");
    report.append("====================\n");

    // Fetch total sales data for the report
    double totalSales = dbHelper.getTotalSales();
    report.append("Total Sales: ").append(totalSales).append("\n");

    // Fetch top selling cakes data for the report
    List<CakeSalesRecord> topSellingCakes = dbHelper.getTopSellingCakes();
    report.append("\nTop Selling Cakes:\n");
    for (CakeSalesRecord record : topSellingCakes) {
        report.append(record.getCakeName()).append(": ")
               .append(record.getQuantitySold()).append(" sold, ")
               .append(record.getTotalSales()).append(" total sales\n");
    }

    // Update the report area with the generated report
    reportArea.setText(report.toString());
}
private void downloadSalesReport() {
    // Generate the sales report as a String
    StringBuilder report = new StringBuilder();
    report.append("Sales Report\n");
    report.append("====================\n");

    // Fetch total sales data for the report
    double totalSales = dbHelper.getTotalSales();
    report.append("Total Sales: ").append(totalSales).append("\n");

    // Fetch top selling cakes data for the report
    List<CakeSalesRecord> topSellingCakes = dbHelper.getTopSellingCakes();
    report.append("\nTop Selling Cakes:\n");
    for (CakeSalesRecord record : topSellingCakes) {
        report.append(record.getCakeName()).append(": ")
               .append(record.getQuantitySold()).append(" sold, ")
               .append(record.getTotalSales()).append(" total sales\n");
    }

    // Save the report to a file
    try {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report");
        fileChooser.setSelectedFile(new File("sales_report.txt")); // Default file name

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(report.toString());
                JOptionPane.showMessageDialog(null, "Sales report saved successfully!");
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error saving the sales report.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CakeShopPOS app = new CakeShopPOS();
            app.setVisible(true);
        });
    }
}