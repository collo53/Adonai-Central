public class Cake {
    private String name;
    private double price;
    private int quantity;
    private double totalPrice;
    private String inventoryDate; // New field
    private String orderDate;     // New field
    private String station;

    public Cake(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.inventoryDate = inventoryDate;
    }

    // Add constructors to include the date fields
    public Cake(String name, double price, int quantity, String inventoryDate) {
        this(name, price, quantity);
        this.inventoryDate = inventoryDate;
    }

    public Cake(String name, double price, int quantity, double totalPrice, String orderDate) {
        this(name, price, quantity);
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
      public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

  

    public String getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(String inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
     public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
}