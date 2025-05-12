import java.util.Date;

public class StationProgress {
    private String stationName;
    private double totalSales;
    private Date salesDate;

    public StationProgress(String stationName, double totalSales, Date salesDate) {
        this.stationName = stationName;
        this.totalSales = totalSales;
        this.salesDate = salesDate;
    }

    // Getters and Setters
    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }
}
