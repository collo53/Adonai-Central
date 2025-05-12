import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date; 
import java.sql.Timestamp; 
import java.util.List;
import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;
import java.sql.Statement;




public class DatabaseHelper {
    private final String jdbcUrl = "jdbc:mysql://localhost:3306/original"; // Ensure this matches your DB
    private final String dbUsername = "root"; // Your MySQL username
    private final String dbPassword = "collins09"; // Your MySQL password

    // Method to get a connection to the database
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
    }


    public Vector<String> getSuggestionsFromDatabase(String input) {
    Vector<String> suggestions = new Vector<>();

    // Assuming getConnection() is your method to get the existing database connection
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT cake_name FROM inventory WHERE cake_name LIKE ? LIMIT 5")) {
        stmt.setString(1, input + "%");
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            suggestions.add(rs.getString("cake_name"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return suggestions;
}

    public List<String> getAllCakeNames() {
        List<String> cakeNames = new ArrayList<>();
        String query = "SELECT cake_name FROM inventory"; // Assuming your table has a column named cake_name

        try (Connection conn = getConnection(); // Get a connection
             Statement stmt = conn.createStatement(); // Create a Statement
             ResultSet rs = stmt.executeQuery(query)) { // Execute the query

            while (rs.next()) {
                String cakeName = rs.getString("cake_name"); // Fetch cake name from result set
                cakeNames.add(cakeName); // Add it to the list
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle query execution error
        }

        return cakeNames; // Return the list of cake names
    }
    // Method to add a cake to the inventory
   public void addInventoryItem(Cake cake) {
    String queryCheck = "SELECT * FROM inventory WHERE cake_name = ?";
    String queryUpdate = "UPDATE inventory SET quantity = quantity + ?, price = ? WHERE cake_name = ?";
    String queryInsert = "INSERT INTO inventory (cake_name, price, quantity) VALUES (?, ?, ?)";
    
    try (Connection conn = getConnection()) {
        // Check if the cake already exists by name
        try (PreparedStatement checkStmt = conn.prepareStatement(queryCheck)) {
            checkStmt.setString(1, cake.getName());
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                // If cake exists, update quantity and price
                try (PreparedStatement updateStmt = conn.prepareStatement(queryUpdate)) {
                    updateStmt.setInt(1, cake.getQuantity());
                    updateStmt.setDouble(2, cake.getPrice());
                    updateStmt.setString(3, cake.getName());
                    int rowsAffected = updateStmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Inventory item updated: " + cake.getName());
                    }
                }
            } else {
                // If cake does not exist, insert new record
                try (PreparedStatement insertStmt = conn.prepareStatement(queryInsert)) {
                    insertStmt.setString(1, cake.getName());
                    insertStmt.setDouble(2, cake.getPrice());
                    insertStmt.setInt(3, cake.getQuantity());
                    int rowsAffected = insertStmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Inventory item added: " + cake.getName());
                    }
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



    // Method to retrieve all inventory items
    public List<Cake> getInventoryItems() {
        List<Cake> cakes = new ArrayList<>();
        String query = "SELECT cake_name, price, quantity, date_added FROM inventory";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cake cake = new Cake(rs.getString("cake_name"), rs.getDouble("price"), rs.getInt("quantity"), rs.getString("date_added"));
                cakes.add(cake);
                System.out.println("Fetched inventory item: " + cake.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cakes;
    }

    // Method to update the quantity of an inventory item
   public void updateInventoryQuantity(String name, int quantity) {
    String query = "UPDATE inventory SET quantity = quantity + ? WHERE cake_name = ?";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, quantity);
        stmt.setString(2, name);
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Updated inventory quantity for: " + name);
        } else {
            System.out.println("Failed to update inventory quantity for: " + name);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
public boolean checkInventory(String name, int quantity) {
    String query = "SELECT quantity FROM inventory WHERE cake_name = ?";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            int availableQuantity = rs.getInt("quantity");
            System.out.println("Available quantity for " + name + ": " + availableQuantity);
            return availableQuantity >= quantity; // Check if enough stock is available
        } else {
            System.out.println("Item not found in inventory: " + name);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false; // Return false if item is not found or insufficient stock
}

public void addStationToDatabase(String stationName, double totalSales, String password) {
    String normalizedStationName = stationName.trim().toLowerCase(); // Normalize name
    String queryCheck = "SELECT * FROM stations WHERE LOWER(TRIM(station_name)) = ?";
    String queryUpdate = "UPDATE stations SET total_sales = total_sales + ?, created_at = ?, password = ? WHERE LOWER(TRIM(station_name)) = ?";
    String queryInsert = "INSERT INTO stations (station_name, total_sales, created_at, password) VALUES (?, ?, ?, ?)";

    try (Connection conn = getConnection()) {
        // Check if the station already exists
        try (PreparedStatement checkStmt = conn.prepareStatement(queryCheck)) {
            checkStmt.setString(1, normalizedStationName); // Use normalized name for checking
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Station exists, update total_sales and password
                try (PreparedStatement updateStmt = conn.prepareStatement(queryUpdate)) {
                    updateStmt.setDouble(1, totalSales); // Add to total sales
                    updateStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis())); // Update created_at timestamp
                    updateStmt.setString(3, password); // Update password
                    updateStmt.setString(4, normalizedStationName); // Use normalized name for the condition
                    int rowsAffected = updateStmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Station " + stationName + " updated in the database.");
                    }
                }
            } else {
                // Station does not exist, add new record
                try (PreparedStatement insertStmt = conn.prepareStatement(queryInsert)) {
                    insertStmt.setString(1, stationName.trim()); // Use original name with trimmed spaces
                    insertStmt.setDouble(2, totalSales);
                    insertStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                    insertStmt.setString(4, password); // Store the password
                    int rowsAffected = insertStmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Station " + stationName + " added to the database.");
                    }
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error adding/updating station to the database.");
    }
}

public boolean removeCakeFromInventory(String cakeName) {
    String query = "DELETE FROM inventory WHERE cake_name = ?";
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, cakeName);
        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

public List<StationSales> loadStationsFromDatabase() {
    List<StationSales> stations = new ArrayList<>();
    String query = "SELECT station_name, total_sales, created_at FROM stations";
    try (Connection conn = getConnection(); 
         PreparedStatement pstmt = conn.prepareStatement(query); 
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            String stationName = rs.getString("station_name"); // Get the station name
            double totalSales = rs.getDouble("total_sales"); // Get total sales
            java.sql.Timestamp sqlTimestamp = rs.getTimestamp("created_at"); // Get the created_at timestamp
            
            // Convert java.sql.Timestamp to java.util.Date
            java.util.Date salesDate = sqlTimestamp != null ? new java.util.Date(sqlTimestamp.getTime()) : null;

            // Create a new StationSales object           
            StationSales stationSale = new StationSales(stationName, totalSales, salesDate);
            stations.add(stationSale);

            // Log the fetched station sales
            System.out.println("Fetched station sales: " + stationName + ", Total Sales: " + totalSales + ", Sales Date: " + salesDate);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error loading stations from the database.");
    }

    return stations;
}

public void addCakesDistributed(String stationName, String cakeName, int quantity) {
    String queryCheck = "SELECT * FROM cakes_distributed WHERE station_name = ? AND cake_name = ?";
    String queryUpdate = "UPDATE cakes_distributed SET quantity = quantity + ? WHERE station_name = ? AND cake_name = ?";
    String queryInsert = "INSERT INTO cakes_distributed (station_name, cake_name, quantity) VALUES (?, ?, ?)"; // No date here

    try (Connection conn = getConnection()) {
        // Check if the cake distribution record already exists
        try (PreparedStatement checkStmt = conn.prepareStatement(queryCheck)) {
            checkStmt.setString(1, stationName);
            checkStmt.setString(2, cakeName);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // If record exists, update quantity
                try (PreparedStatement updateStmt = conn.prepareStatement(queryUpdate)) {
                    updateStmt.setInt(1, quantity); // Adding quantity
                    updateStmt.setString(2, stationName);
                    updateStmt.setString(3, cakeName);
                    int rowsAffected = updateStmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Cakes distributed record updated: " + cakeName + " at " + stationName);
                    }
                }
            } else {
                // If record does not exist, insert new record
                try (PreparedStatement insertStmt = conn.prepareStatement(queryInsert)) {
                    insertStmt.setString(1, stationName);
                    insertStmt.setString(2, cakeName);
                    insertStmt.setInt(3, quantity);
                    int rowsAffected = insertStmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Cakes distributed record added: " + cakeName + " at " + stationName);
                    }
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error adding/updating cake distributed record.");
    }
}
public boolean deleteDistributedCakes(String stationName, String cakeName, int quantityToDelete) {
    String updateQuery = "UPDATE cakes_distributed SET quantity = quantity - ? WHERE station_name = ? AND cake_name = ? AND quantity >= ?";
    String deleteQuery = "DELETE FROM cakes_distributed WHERE station_name = ? AND cake_name = ? AND quantity = 0";

    try (Connection conn = getConnection();
         PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
         PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {

        conn.setAutoCommit(false); // Start transaction

        // Reduce the quantity
        updateStmt.setInt(1, quantityToDelete);
        updateStmt.setString(2, stationName);
        updateStmt.setString(3, cakeName);
        updateStmt.setInt(4, quantityToDelete);
        int rowsUpdated = updateStmt.executeUpdate();

        // If quantity reaches zero, delete the record
        deleteStmt.setString(1, stationName);
        deleteStmt.setString(2, cakeName);
        int rowsDeleted = deleteStmt.executeUpdate();

        conn.commit(); // Commit transaction
        return rowsUpdated > 0 || rowsDeleted > 0; // Return true if anything was updated or deleted

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
public int getInventoryQuantity(String cakeName) {
    String query = "SELECT quantity FROM inventory WHERE cake_name = ?";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, cakeName);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("quantity");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0; // Default to 0 if cake is not found
}

public void addItemsSold(String stationName, String cakeName, int quantity) {
    String queryCheckItem = "SELECT * FROM items_sold WHERE station_name = ? AND cake_name = ? ORDER BY update_time DESC LIMIT 1";
    String queryUpdateItem = "UPDATE items_sold SET quantity = quantity + ? WHERE id = ?";
    String queryInsertItem = "INSERT INTO items_sold (station_name, cake_name, quantity, update_time) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

    String queryCheckDaily = "SELECT total_sales FROM daily_sales WHERE station_name = ? AND sales_date = ?";
    String queryUpdateDaily = "UPDATE daily_sales SET total_sales = total_sales + ? WHERE station_name = ? AND sales_date = ?";
    String queryInsertDaily = "INSERT INTO daily_sales (station_name, sales_date, total_sales) VALUES (?, ?, ?)";

    Connection conn = null;

    try {
        conn = getConnection();
        conn.setAutoCommit(false); // Start transaction

        // Check or update items_sold
        try (PreparedStatement checkItemStmt = conn.prepareStatement(queryCheckItem)) {
            checkItemStmt.setString(1, stationName);
            checkItemStmt.setString(2, cakeName);
            ResultSet rsItem = checkItemStmt.executeQuery();

            if (rsItem.next()) {
                Timestamp updateTime = rsItem.getTimestamp("update_time");
                int recordId = rsItem.getInt("id");

                java.sql.Date recordDate = new java.sql.Date(updateTime.getTime());
                java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

                if (recordDate.equals(currentDate)) {
                    try (PreparedStatement updateItemStmt = conn.prepareStatement(queryUpdateItem)) {
                        updateItemStmt.setInt(1, quantity);
                        updateItemStmt.setInt(2, recordId);
                        updateItemStmt.executeUpdate();
                    }
                } else {
                    insertNewRecord(conn, stationName, cakeName, quantity);
                }
            } else {
                insertNewRecord(conn, stationName, cakeName, quantity);
            }
        }

        // Update daily_sales
        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

        try (PreparedStatement checkDailyStmt = conn.prepareStatement(queryCheckDaily)) {
            checkDailyStmt.setString(1, stationName);
            checkDailyStmt.setDate(2, currentDate);
            ResultSet rsDaily = checkDailyStmt.executeQuery();

            if (rsDaily.next()) {
                try (PreparedStatement updateDailyStmt = conn.prepareStatement(queryUpdateDaily)) {
                    updateDailyStmt.setInt(1, quantity);
                    updateDailyStmt.setString(2, stationName);
                    updateDailyStmt.setDate(3, currentDate);
                    updateDailyStmt.executeUpdate();
                }
            } else {
                try (PreparedStatement insertDailyStmt = conn.prepareStatement(queryInsertDaily)) {
                    insertDailyStmt.setString(1, stationName);
                    insertDailyStmt.setDate(2, currentDate);
                    insertDailyStmt.setInt(3, quantity);
                    insertDailyStmt.executeUpdate();
                }
            }
        }

        conn.commit(); // Commit transaction
        System.out.println("Item and daily sales records updated successfully.");
    } catch (SQLException e) {
        e.printStackTrace();
        if (conn != null) {
            try {
                conn.rollback(); // Rollback in case of error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
        System.out.println("Error adding/updating item and daily sales records.");
    } finally {
        if (conn != null) {
            try {
                conn.close(); // Close connection
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
}

private void insertNewRecord(Connection conn, String stationName, String cakeName, int quantity) throws SQLException {
    String queryInsertItem = "INSERT INTO items_sold (station_name, cake_name, quantity, update_time) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
    try (PreparedStatement insertItemStmt = conn.prepareStatement(queryInsertItem)) {
        insertItemStmt.setString(1, stationName);
        insertItemStmt.setString(2, cakeName);
        insertItemStmt.setInt(3, quantity);
        insertItemStmt.executeUpdate();
        System.out.println("New item sales record added: " + cakeName + " at " + stationName);
    }
}




public List<CakeDistributed> loadCakesDistributed() {
    List<CakeDistributed> cakesDistributed = new ArrayList<>();
    String sql = "SELECT * FROM cakes_distributed";
    
    try (Connection conn = getConnection(); 
         PreparedStatement pstmt = conn.prepareStatement(sql); 
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            int id = rs.getInt("id");
            String stationName = rs.getString("station_name");
            String cakeName = rs.getString("cake_name");
            int quantity = rs.getInt("quantity");
            Timestamp distributionTimestamp = rs.getTimestamp("distribution_date"); // Retrieve as Timestamp
            
            Date distributionDate = distributionTimestamp != null ? new Date(distributionTimestamp.getTime()) : null; // Convert to Date if not null
            
            CakeDistributed cakeDistributed = new CakeDistributed(id, stationName, cakeName, quantity, distributionDate);
            cakesDistributed.add(cakeDistributed);
            System.out.println("Fetched distributed cake record: " + cakeName + " from " + stationName);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error loading distributed cakes from the database.");
    }

    return cakesDistributed;
}

public List<ItemsSold> loadItemsSold() {
    List<ItemsSold> itemsSoldList = new ArrayList<>(); // Renamed to avoid conflict
    String sql = "SELECT * FROM items_sold";
    
    try (Connection conn = getConnection(); 
         PreparedStatement pstmt = conn.prepareStatement(sql); 
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            int id = rs.getInt("id");
            String stationName = rs.getString("station_name");
            String cakeName = rs.getString("cake_name");
            int quantity = rs.getInt("quantity");
            Timestamp updateTimestamp = rs.getTimestamp("update_time"); // Retrieve as Timestamp
            
            // Convert Timestamp to Date if not null
            Date updateTime = updateTimestamp != null ? new Date(updateTimestamp.getTime()) : null; // Convert to Date if not null

            
            // Create an ItemsSold object and add it to the list
            ItemsSold itemSold = new ItemsSold(id, stationName, cakeName, quantity, updateTime); // Renamed variable
            itemsSoldList.add(itemSold); // Add to the correct list
            System.out.println("Fetched sold item record: " + cakeName + " from " + stationName);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error loading sold items from the database.");
    }

    return itemsSoldList; // Return the list
}

public List<CakeDistributed> loadCakesDistributedForStation(String stationName,java.sql.Date selectedDate) {
    List<CakeDistributed> distributedCakes = new ArrayList<>();
    String query = "SELECT id, cake_name, quantity, distribution_date FROM cakes_distributed WHERE station_name = ? AND DATE(distribution_date) = ?";

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, stationName);
        stmt.setDate(2, selectedDate); // Adding date filter

        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            int id = rs.getInt("id");  // Assuming 'id' is a column in your table
            String cakeName = rs.getString("cake_name");
            int quantity = rs.getInt("quantity");
            // Using java.sql.Date or java.time.LocalDate for better date handling
            java.sql.Date distributionDate = rs.getDate("distribution_date");

            // Create a new CakeDistributed object with the retrieved data
            distributedCakes.add(new CakeDistributed(id, stationName, cakeName, quantity, distributionDate));
        }
    } catch (SQLException e) {
        // Consider using a logging framework
        e.printStackTrace();
    }

    return distributedCakes;
}

public List<ItemsSold> loadItemsSoldForStation(String stationName) {
    List<ItemsSold> itemsSold = new ArrayList<>();
    String query = "SELECT id, cake_name, quantity,update_time FROM items_sold WHERE station_name = ?"; // Removed 'update_time'

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, stationName);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            int id = rs.getInt("id");  // Assuming 'id' is a column in your table
            String cakeName = rs.getString("cake_name");
            int quantity = rs.getInt("quantity");
            java.sql.Date updateTime = rs.getDate("update_time");


            // Create a new ItemsSold object with the retrieved data
            itemsSold.add(new ItemsSold(id, stationName, cakeName, quantity, updateTime));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return itemsSold;
}



    // Method to add an order to the database
  
// Method to add a cake order for a specific station
public void addCakeToStation(String stationName, Cake cake, java.sql.Date orderDate) {
    String query = "INSERT INTO station_cakes (station_name, cake_name, price, quantity, order_date, total_price) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, stationName);
        stmt.setString(2, cake.getName());
        stmt.setDouble(3, cake.getPrice());
        stmt.setInt(4, cake.getQuantity());
        stmt.setDate(5, orderDate);
        stmt.setDouble(6, cake.getPrice() * cake.getQuantity());
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Cake added to station: " + stationName);
            updateInventoryQuantity(cake.getName(), -cake.getQuantity()); // Update inventory after order
        } else {
            System.out.println("Failed to add cake to station: " + stationName);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

 
public List<StationSales> fetchTotalSalesForAllStations() {
    List<StationSales> salesData = new ArrayList<>();

    // SQL query to fetch total sales for all stations
    String sql = "SELECT station_name, SUM(total_sales) as total_sales, sales_date " +
                 "FROM daily_sales " +
                 "GROUP BY station_name, sales_date";

    try (Connection conn = getConnection(); 
         PreparedStatement pstmt = conn.prepareStatement(sql); 
         ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
            String stationName = rs.getString("station_name");
            double totalSales = rs.getDouble("total_sales");
            Date salesDate = rs.getDate("sales_date");

            // Create a StationSales object and add it to the list
            salesData.add(new StationSales(stationName, totalSales, salesDate));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return salesData; // Return the list of StationSales objects
}



    // Method to retrieve all orders from the database
    public List<Cake> getOrders() {
        List<Cake> cakes = new ArrayList<>();
        String query = "SELECT name, price, quantity, total_price, order_date FROM orders";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cake cake = new Cake(rs.getString("name"), rs.getDouble("price"), rs.getInt("quantity"), rs.getDouble("total_price"), rs.getString("order_date"));
                cakes.add(cake);
                System.out.println("Fetched order: " + cake.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cakes;
    }

    // Method to calculate the total sales from all orders
    public double calculateTotalSales() {
        double totalSales = 0.0;
        String query = "SELECT SUM(total_price) AS total_sales FROM orders";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                totalSales = rs.getDouble("total_sales");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalSales;
    }

public double fetchTotalSales(String stationName, java.sql.Date salesDate) {
    double totalSales = 0.0;
    String query = "SELECT SUM(i.quantity * inv.price) AS total_sales " +
                   "FROM items_sold i " +
                   "JOIN inventory inv ON i.cake_name = inv.cake_name " +
                   "WHERE i.station_name = ? AND DATE(i.update_time) = ?";

    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, stationName);
        pstmt.setDate(2, salesDate);

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            totalSales = rs.getDouble("total_sales");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error fetching total sales for station: " + stationName);
    }
    return totalSales;
}


public List<SalesRecord> fetchDailySales(String stationName) {
    List<SalesRecord> salesRecords = new ArrayList<>();
    String query = "SELECT DATE(i.update_time) AS sales_date, SUM(i.quantity * inv.price) AS total_sales " +
                   "FROM items_sold i " +
                   "JOIN inventory inv ON i.cake_name = inv.cake_name " +
                   "WHERE i.station_name = ? " +
                   "GROUP BY DATE(i.update_time)";

    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, stationName);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            LocalDate salesDate = rs.getDate("sales_date").toLocalDate();
            double totalSales = rs.getDouble("total_sales");
            salesRecords.add(new SalesRecord(salesDate, totalSales));
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error fetching daily sales for station: " + stationName);
    }
    return salesRecords;
}

public Map<String, List<DailySales>> fetchDailySalesForAllStations(String stationName) {
    Map<String, List<DailySales>> stationSalesData = new HashMap<>();
    String sql = "SELECT station_name, sales_date, total_sales FROM daily_sales";

    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            String station = rs.getString("station_name");
            Date salesDate = rs.getDate("sales_date");
            double totalSales = rs.getDouble("total_sales");

            // Create DailySales object with the fetched data
            DailySales salesRecord = new DailySales(station, salesDate, totalSales);

            // Add the sales record to the corresponding station in the map
            if (!stationSalesData.containsKey(station)) {
                stationSalesData.put(station, new ArrayList<>());
            }
            stationSalesData.get(station).add(salesRecord);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error fetching daily sales for all stations.");
    }

    return stationSalesData;
}




public void insertCakeSales(String stationName, String cakeName, int quantitySold, Date salesDate) {
    String query = "INSERT INTO cake_sales (station_name, cake_name, quantity_sold, sales_date) VALUES (?, ?, ?, ?)";

    try (Connection conn = getConnection(); // Make sure this method returns a valid connection
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, stationName);
        stmt.setString(2, cakeName);
        stmt.setInt(3, quantitySold);
        stmt.setDate(4, new java.sql.Date(salesDate.getTime())); // Convert java.util.Date to java.sql.Date

        int rowsAffected = stmt.executeUpdate();
        System.out.println("Inserted " + rowsAffected + " row(s) into cake_sales.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


public List<CakeSalesRecord> fetchPopularCakes(String stationName) {
    List<CakeSalesRecord> popularCakes = new ArrayList<>();
    String query = "SELECT i.cake_name, SUM(i.quantity) AS total_sold, MAX(inv.price) AS price " +
                   "FROM items_sold i " +
                   "JOIN inventory inv ON i.cake_name = inv.cake_name " +  // Join with inventory table to get the price
                   "WHERE i.station_name = ? " +
                   "GROUP BY i.cake_name " +
                   "ORDER BY total_sold DESC";

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, stationName);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            String cakeName = rs.getString("cake_name");
            int totalSold = rs.getInt("total_sold");
            double price = rs.getDouble("price");
            double totalSales = totalSold * price;  // Calculate total sales for the cake
            
            // Pass all three parameters to the CakeSalesRecord constructor
            popularCakes.add(new CakeSalesRecord(cakeName, totalSold, totalSales));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return popularCakes;
}



    // Method to get the total number of orders
    public int getNumberOfOrders() {
        int numberOfOrders = 0;
        String query = "SELECT COUNT(*) AS order_count FROM orders";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                numberOfOrders = rs.getInt("order_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfOrders;
    }

    // Method to get the total number of items sold
    public int getTotalItemsSold() {
        int totalItemsSold = 0;
        String query = "SELECT SUM(quantity) AS total_items_sold FROM orders";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                totalItemsSold = rs.getInt("total_items_sold");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalItemsSold;
    }

    // Method to add a station sale to the database
    public void addStationSale(String stationName, double totalSales, java.sql.Date salesDate) {
        String query = "INSERT INTO station_sales (station_name, total_sales, sales_date) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, stationName);
            stmt.setDouble(2, totalSales);
            stmt.setDate(3, salesDate);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Station sale added: " + stationName);
            } else {
                System.out.println("Failed to add station sale: " + stationName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    // Method to retrieve combined station sales from the database
    public List<StationSales> getCombinedStationSales() {
        List<StationSales> stationSalesList = new ArrayList<>();
        String query = "SELECT station_name, SUM(total_sales) AS total_sales, sales_date " +
                       "FROM daily_sales " +
                       "GROUP BY station_name, sales_date";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                StationSales stationSale = new StationSales(
                    rs.getString("station_name"),
                    rs.getDouble("total_sales"),
                    rs.getDate("sales_date")
                );
                stationSalesList.add(stationSale);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stationSalesList;
    }
     public List<Booking> fetchBookingsForStation() {
        List<Booking> bookings = new ArrayList<>();

        String query = "SELECT id, booker_name, status, cake_name, quantity, date_booked, date_cleared, amount_paid, amount_left, station FROM bookings ";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {


            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking(
                            rs.getInt("id"),
                            rs.getString("booker_name"),
                            rs.getString("status"),
                            rs.getString("cake_name"),
                            rs.getInt("quantity"),
                            rs.getDate("date_booked"),
                            rs.getDate("date_cleared"),
                            rs.getDouble("amount_paid"),
                            rs.getDouble("amount_left"),
                            rs.getString("station")
                    );
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
   public boolean deleteBooking(int id) {
    String deleteQuery = "DELETE FROM bookings WHERE id = ?";
    String resetIdQuery = "SET @num := 0";  
    String updateIdsQuery = "UPDATE bookings SET id = (@num := @num + 1) ORDER BY id";
    String resetAutoIncrementQuery = "ALTER TABLE bookings AUTO_INCREMENT = 1";

    try (Connection conn = getConnection();
         PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
         Statement stmt = conn.createStatement()) {

        // Delete the record
        deleteStmt.setInt(1, id);
        int rowsAffected = deleteStmt.executeUpdate();

        if (rowsAffected > 0) {
            // Reset IDs and auto-increment
            stmt.execute(resetIdQuery);
            stmt.execute(updateIdsQuery);
            stmt.execute(resetAutoIncrementQuery);
            return true;
        }
        return false;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

public List<CakeSalesRecord> getCombinedPopularCakes() {
    List<CakeSalesRecord> popularCakes = new ArrayList<>();
    // Updated query to join items_sold with inventory to get price
    String query = "SELECT i.cake_name, SUM(i.quantity) AS total_sold, SUM(i.quantity * inv.price) AS total_sales " +
                   "FROM items_sold i " +
                   "JOIN inventory inv ON i.cake_name = inv.cake_name " +
                   "GROUP BY i.cake_name " +
                   "ORDER BY total_sales DESC";

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            String cakeName = rs.getString("cake_name");
            int totalSold = rs.getInt("total_sold");
            double totalSales = rs.getDouble("total_sales"); // Fetch total sales

            // Pass the total sales to the CakeSalesRecord constructor
            popularCakes.add(new CakeSalesRecord(cakeName, totalSold, totalSales));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return popularCakes;
}
public double getTotalSales() {
    double totalSales = 0.0;
    // Explicitly referencing the table aliases for quantity and price to avoid ambiguity
    String query = "SELECT SUM(i.quantity * inv.price) AS total_sales " +
                   "FROM items_sold i " +
                   "JOIN inventory inv ON i.cake_name = inv.cake_name"; // Use alias 'i' for items_sold and 'inv' for inventory

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        if (rs.next()) {
            totalSales = rs.getDouble("total_sales");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return totalSales;
}


    // Method to retrieve station sales data filtered by date
    public List<StationSales> getStationSales(java.sql.Date filterDate) {
    List<StationSales> stationSalesList = new ArrayList<>();
    String query = "SELECT station_name, SUM(total_sales) AS total_sales, sales_date " +
                   "FROM station_sales WHERE sales_date = ? " +
                   "GROUP BY station_name, sales_date";

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setDate(1, filterDate);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                StationSales stationSale = new StationSales(
                    rs.getString("station_name"),
                    rs.getDouble("total_sales"),
                    rs.getDate("sales_date")
                );
                stationSalesList.add(stationSale);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return stationSalesList;
}


  public List<CakeSalesRecord> getTopSellingCakes() {
    List<CakeSalesRecord> topSellingCakes = new ArrayList<>();
    
    // SQL query to get top-selling cakes from the items_sold table, joining with the inventory table to get the price
    String sql = "SELECT i.cake_name, SUM(i.quantity) AS total_quantity, SUM(i.quantity * inv.price) AS total_sales " +
                 "FROM items_sold i " +
                 "JOIN inventory inv ON i.cake_name = inv.cake_name " +  // Join with inventory table to get the price of each cake
                 "GROUP BY i.cake_name " +
                 "ORDER BY total_sales DESC LIMIT 10";  // Top 10 cakes by total sales

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            String name = rs.getString("cake_name");
            int quantity = rs.getInt("total_quantity");
            double totalSales = rs.getDouble("total_sales");

            // Create a CakeSalesRecord with the fetched data
            CakeSalesRecord record = new CakeSalesRecord(name, quantity, totalSales);
            topSellingCakes.add(record);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return topSellingCakes;
}


public void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) { // Check if conn is not null and not closed
                conn.close(); // Close the connection
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle closing error
        }
    }

    
    
}
