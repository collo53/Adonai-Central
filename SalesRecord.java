// SalesRecord.java
import java.time.LocalDate;

public class SalesRecord {
    private LocalDate salesDate;
    private double totalSales;

    public SalesRecord(LocalDate salesDate, double totalSales) {
        this.salesDate = salesDate;
        this.totalSales = totalSales;
    }

    public LocalDate getSalesDate() {
        return salesDate;
    }

    public double getTotalSales() {
        return totalSales;
    }
}
