import java.util.Date;

public class DailySales {
    private String stationName;
    private Date salesDate;
    private double totalSales;

    // Constructor
    public DailySales(String stationName, Date salesDate, double totalSales) {
        this.stationName = stationName;
        this.salesDate = salesDate;
        this.totalSales = totalSales;
    }

    // Getters and setters
    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }
}
