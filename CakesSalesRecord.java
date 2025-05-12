public class CakeSalesRecord {
    private String cakeName;
    private int quantitySold;
    private double totalSales;

    public CakeSalesRecord(String cakeName, int quantitySold, double totalSales) {
        this.cakeName = cakeName;
        this.quantitySold = quantitySold;
        this.totalSales = totalSales;
    }

    public String getCakeName() {
        return cakeName;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public double getTotalSales() {
        return totalSales;
    }
}
