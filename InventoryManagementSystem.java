import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


class Inventory {
    private Map<String, Integer> items;

    public Inventory() {
        items = new HashMap<>();
    }

    public void addItem(String itemName, int quantity) {
        items.put(itemName, items.getOrDefault(itemName, 0) + quantity);
        System.out.println("Added " + quantity + " of " + itemName + ". Current quantity: " + items.get(itemName));
    }

    public void removeItem(String itemName, int quantity) {
        if (items.containsKey(itemName) && items.get(itemName) >= quantity) {
            items.put(itemName, items.get(itemName) - quantity);
            System.out.println("Removed " + quantity + " of " + itemName + ". Current quantity: " + items.get(itemName));
            if (items.get(itemName) == 0) {
                items.remove(itemName);
                System.out.println(itemName + " is out of stock.");
            }
        } else {
            System.out.println("Cannot remove " + quantity + " of " + itemName + ". Not enough stock or item does not exist.");
        }
    }

    public void checkInventory() {
        if (items.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("Current inventory:");
            for (Map.Entry<String, Integer> entry : items.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    public boolean hasItem(String itemName, int quantity) {
        return items.containsKey(itemName) && items.get(itemName) >= quantity;
    }

    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        inventory.addItem("Cake", 10);
        inventory.addItem("Cupcake", 20);
        inventory.checkInventory();
        inventory.removeItem("Cake", 5);
        inventory.checkInventory();
        inventory.removeItem("Cupcake", 25);
        inventory.checkInventory();
    }
}

class Sales {
    private List<String> salesRecords;

    public Sales() {
        salesRecords = new ArrayList<>();
    }

    public void recordSale(String itemName, int quantity) {
        salesRecords.add("Sold " + quantity + " of " + itemName);
    }

    public void printSalesReport() {
        if (salesRecords.isEmpty()) {
            System.out.println("No sales recorded today.");
        } else {
            System.out.println("Sales Report for the Day:");
            for (String record : salesRecords) {
                System.out.println(record);
            }
        }
    }

    public void writeSalesReportToExcel(String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sales Report");

        int rowNum = 0;
        for (String record : salesRecords) {
            Row row = sheet.createRow(rowNum++);
            Cell cell = row.createCell(0);
            cell.setCellValue(record);
        }

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            System.out.println("Error writing to Excel file: " + e.getMessage());
        }

        System.out.println("Sales report written to " + filePath);
    }
}

public class InventoryManagementSystem {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        Sales sales = new Sales();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nInventory Management System");
            System.out.println("1. Add item");
            System.out.println("2. Remove item");
            System.out.println("3. Check inventory");
            System.out.println("4. Record a sale");
            System.out.println("5. Print sales report");
            System.out.println("6. Write sales report to Excel");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter the item name: ");
                    String addItemName = scanner.nextLine();
                    System.out.print("Enter the quantity: ");
                    int addQuantity = scanner.nextInt();
                    inventory.addItem(addItemName, addQuantity);
                    break;
                case 2:
                    System.out.print("Enter the item name: ");
                    String removeItemName = scanner.nextLine();
                    System.out.print("Enter the quantity: ");
                    int removeQuantity = scanner.nextInt();
                    inventory.removeItem(removeItemName, removeQuantity);
                    break;
                case 3:
                    inventory.checkInventory();
                    break;
                case 4:
                    System.out.print("Enter the item name: ");
                    String saleItemName = scanner.nextLine();
                    System.out.print("Enter the quantity: ");
                    int saleQuantity = scanner.nextInt();
                    if (inventory.hasItem(saleItemName, saleQuantity)) {
                        inventory.removeItem(saleItemName, saleQuantity);
                        sales.recordSale(saleItemName, saleQuantity);
                    } else {
                        System.out.println("Sale failed. Not enough stock.");
                    }
                    break;
                case 5:
                    sales.printSalesReport();
                    break;
                case 6:
                    System.out.print("Enter the file path to save the sales report: ");
                    String filePath = scanner.nextLine();
                    sales.writeSalesReportToExcel(filePath);
                    break;
                case 7:
                    System.out.println("Exiting the inventory system.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
