import java.sql.*;

public class DatabaseManager {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/cakeshop";
    private static final String USERNAME = "your_username";
    private static final String PASSWORD = "your_password";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String insertQuery = "INSERT INTO inventory (name, price, quantity) VALUES (?, ?, ?)";
            
            // Example values
            String name = "Black Forest";
            double price = 25.99;
            int quantity = 10;
            
            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setString(1, name);
                stmt.setDouble(2, price);
                stmt.setInt(3, quantity);
                
                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("A new cake added to inventory successfully!");
                } else {
                    System.out.println("Failed to add cake to inventory.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
public JPanel createOrderPanel() {
    JPanel orderPanel = new JPanel(new BorderLayout()); // Create the order panel

    // Create and initialize the input panel and gbc within this method
    JPanel inputPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(20, 40, 20, 40); // Increased padding for the input panel
    gbc.gridx = 0; // Align elements in the first column
    gbc.gridy = GridBagConstraints.RELATIVE; // Automatically place components in the next row

    // Create JDateChooser to select the date
    JLabel dateLabel = new JLabel("Select Date:");
    JDateChooser dateChooser = new JDateChooser();
    dateChooser.setPreferredSize(new Dimension(150, 30)); // Set size of the date picker

    // Add date picker to the input panel
    inputPanel.add(dateLabel, gbc);
    inputPanel.add(dateChooser, gbc);

    // Add a button to show total sales for the selected date
    JButton showSalesButton = new JButton("Show Total Sales");
    inputPanel.add(showSalesButton, gbc);

    // Create a panel to hold the station buttons in a grid (two columns)
    JPanel stationButtonPanel = new JPanel(new GridBagLayout()); // Use GridBagLayout to control the grid positioning
    GridBagConstraints stationGbc = new GridBagConstraints();
    stationGbc.insets = new Insets(30, 40, 30, 40); // More space between station buttons
    stationGbc.gridx = 0;
    stationGbc.gridy = 0;
    stationGbc.fill = GridBagConstraints.HORIZONTAL;

    List<StationSales> stationSales = new ArrayList<>(); // Track all station sales

    // Button to add Station 1 Sale
    StationSales station1Sale = new StationSales("Station 1", 0.0, new Date(System.currentTimeMillis())); // Use default values
    stationSales.add(station1Sale);

    // Create a panel that will act as the content for the larger button
    JPanel station1ButtonContent = new JPanel(new BorderLayout());
    station1ButtonContent.setPreferredSize(new Dimension(300, 300));

    // Create the larger button for Station 1
    JButton station1Button = new JButton();
    station1Button.setLayout(new BorderLayout()); // Set layout for the button to add components
    station1Button.setPreferredSize(new Dimension(300, 300)); // Enlarge the button
    station1Button.setFont(new Font("Arial", Font.BOLD, 18)); // Set larger font size

    // Create a sub-panel to hold multiple buttons in a grid layout
    JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10)); // GridLayout with two buttons side by side

    // Create the "Place Order" button
    JButton placeOrderButton = new JButton("Cakes Distributed");
    placeOrderButton.setPreferredSize(new Dimension(150, 50)); // Size of the button
    placeOrderButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font for the button

    // Create the second button (e.g., "Cancel Order")
    JButton cancelOrderButton = new JButton("Cakes Sold");
    cancelOrderButton.setPreferredSize(new Dimension(150, 50)); // Size of the button
    cancelOrderButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font for the button
    JLabel totalSalesLabel = new JLabel("Total Sales: " + station1Sale.getTotalSales()); // Initial total sales label
    totalSalesLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Set a readable font for the sales label

    // Add both buttons to the button panel
    buttonPanel.add(placeOrderButton);
    buttonPanel.add(cancelOrderButton);
    station1ButtonContent.add(totalSalesLabel, BorderLayout.CENTER); // Display above the buttons

    // Define the ActionListener that both buttons will share (for Place Order)
    ActionListener openStationSaleListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Shared code to bring up the placing order popup
            openStationSaleDialog(station1Sale);
        }
    };

    // Define an ActionListener for the cancel order button
    ActionListener cancelOrderListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Logic for canceling the order (you can customize this action)
            openCakesSoldDialog(station1Sale);
        }
    };

    // Add the shared ActionListener to the "Place Order" button
    placeOrderButton.addActionListener(openStationSaleListener);

    // Add the cancel ActionListener to the "Cancel Order" button
    cancelOrderButton.addActionListener(cancelOrderListener);

    // Add a label and the button panel to the station content panel
    station1ButtonContent.add(new JLabel("Station 1 Sale"), BorderLayout.NORTH); // Label for the station name
    station1ButtonContent.add(buttonPanel, BorderLayout.SOUTH); // Add the button panel at the bottom

    // Add the content panel to the larger button
    station1Button.add(station1ButtonContent, BorderLayout.CENTER);

    stationButtonPanel.add(station1Button, stationGbc); // Add Station 1 button to the panel

    // Button to add more station buttons
    JButton addStationButton = new JButton("Add Another Station");

    // Set size and font for addStationButton
    addStationButton.setPreferredSize(new Dimension(200, 50)); // Enlarge the button
    addStationButton.setFont(new Font("Arial", Font.BOLD, 18)); // Set larger font size
    addStationButton.setMargin(new Insets(30, 40, 30, 40));

    addStationButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String stationName = JOptionPane.showInputDialog(null, "Enter Station Name:", "New Station", JOptionPane.PLAIN_MESSAGE);
            if (stationName != null && !stationName.trim().isEmpty()) {
                StationSales newStationSale = new StationSales(stationName, 0.0, new Date(System.currentTimeMillis())); // Use default values
                stationSales.add(newStationSale);

                // Create a new larger button for the new station
                JButton newStationButton = new JButton();
                newStationButton.setLayout(new BorderLayout());
                newStationButton.setPreferredSize(new Dimension(300, 300)); // Enlarge the button
                newStationButton.setFont(new Font("Arial", Font.BOLD, 18)); // Set larger font size

                // Create a panel that will act as the content for the larger button
                JPanel newStationButtonContent = new JPanel(new BorderLayout());
                newStationButtonContent.setPreferredSize(new Dimension(300, 300));

                // Create a sub-panel to hold multiple buttons in a grid layout
                JPanel newButtonPanel = new JPanel(new GridLayout(1, 2, 10, 10)); // GridLayout for the buttons

                // Create the "Place Order" button
                JButton newPlaceOrderButton = new JButton("Cakes Distributed");
                newPlaceOrderButton.setPreferredSize(new Dimension(150, 50)); // Size of the button
                newPlaceOrderButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font for the button

                // Create the second button (e.g., "Cancel Order")
                JButton newCancelOrderButton = new JButton("Cakes Sold");
                newCancelOrderButton.setPreferredSize(new Dimension(150, 50)); // Size of the button
                newCancelOrderButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font for the button
                JLabel newTotalSalesLabel = new JLabel("Total Sales: " + newStationSale.getTotalSales()); // Initial total sales label
                newTotalSalesLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Set a readable font for the sales label

                // Add both buttons to the new button panel
                newButtonPanel.add(newPlaceOrderButton);
                newButtonPanel.add(newCancelOrderButton);
                newStationButtonContent.add(newTotalSalesLabel, BorderLayout.CENTER); // Display above the buttons

                newStationButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Show the cakes left at the new station
                        int cakesLeft = newStationSale.getCakesLeft(); // Get the number of cakes left
                        JOptionPane.showMessageDialog(null, "Cakes left at " + stationName + ": " + cakesLeft, 
                                                      "Station Inventory", JOptionPane.INFORMATION_MESSAGE);
                    }
                });

                // Define the ActionListener for the new buttons
                ActionListener openNewStationSaleListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Shared code for new station sale
                        openStationSaleDialog(newStationSale);
                    }
                };

                // Define an ActionListener for the new cancel order button
                ActionListener newCancelOrderListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Logic for canceling the order for the new station
                        openCakesSoldDialog(newStationSale);
                    }
                };

                // Add the shared ActionListener to the "Place Order" button
                newPlaceOrderButton.addActionListener(openNewStationSaleListener);

                // Add the cancel ActionListener to the "Cancel Order" button
                newCancelOrderButton.addActionListener(newCancelOrderListener);

                newStationButtonContent.add(new JLabel(stationName + " Sale"), BorderLayout.NORTH); // Label for the new station name
                newStationButtonContent.add(newButtonPanel, BorderLayout.SOUTH); // Add the button panel at the bottom

                newStationButton.add(newStationButtonContent, BorderLayout.CENTER); // Add the content panel to the new button
                stationButtonPanel.add(newStationButton); // Add the new station button to the panel

                // Refresh the stationButtonPanel to show the new button
                stationButtonPanel.revalidate();
                stationButtonPanel.repaint();
            }
        }
    });

    stationButtonPanel.add(addStationButton, stationGbc); // Add the "Add Another Station" button

    // Create the order table for displaying the orders
    String[] columnNames = {"Cake", "Quantity", "Price"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    JTable orderTable = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(orderTable);
    orderPanel.add(scrollPane, BorderLayout.CENTER); // Add the order table to the center of the order panel

    // Add input panel and station button panel to the order panel
    orderPanel.add(inputPanel, BorderLayout.NORTH); // Add the input panel at the top
    orderPanel.add(stationButtonPanel, BorderLayout.SOUTH); // Add the station buttons panel at the bottom

    return orderPanel; // Return the fully constructed order panel
}
