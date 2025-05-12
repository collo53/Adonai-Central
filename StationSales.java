import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StationSales {
    private String stationName;
    private List<SaleItem> items;
    private double totalSales; // Total sales for the station
    private Date salesDate; // Date of the sales
    private int cakesLeft;

    // Constructor with parameters
    public StationSales(String stationName, double totalSales, Date salesDate) {
        this.stationName = stationName;
        this.items = new ArrayList<>();
        this.totalSales = totalSales; // Initialize with provided total sales
        this.salesDate = salesDate != null ? salesDate : new Date(); // Use provided date or current date if null
        this.cakesLeft = 0; // Initialize cakesLeft to zero
    }

    public String getStationName() {
        return stationName;
    }

    public void addItem(String cakeName, int quantity) {
        items.add(new SaleItem(cakeName, quantity));
        totalSales += calculatePrice(cakeName, quantity); // Assuming you have a method to calculate price
    }

    public double getTotalSales() {
        return totalSales; // Return total sales
    }

    public Date getSalesDate() {
        return salesDate; // Return the date of sales
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate; // Set the date of sales
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public void setCakesLeft(int cakes) {
        this.cakesLeft = cakes;
    }

    public int getCakesLeft() {
        return cakesLeft;
    }
 
    // Nested class to hold individual sale items
    private static class SaleItem {
        private String cakeName;
        private int quantity;

        public SaleItem(String cakeName, int quantity) {
            this.cakeName = cakeName;
            this.quantity = quantity;
        }

        public String getCakeName() {
            return cakeName;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    // Example method to calculate price, you'll need to define this based on your pricing logic
    private double calculatePrice(String cakeName, int quantity) {
        // Logic to calculate the price of the cake
        return 10.0 * quantity; // Placeholder logic
    }
}
