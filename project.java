import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
}

public class InventoryManagementSystem {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nInventory Management System");
            System.out.println("1. Add item");
            System.out.println("2. Remove item");
            System.out.println("3. Check inventory");
            System.out.println("4. Exit");
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
                    System.out.println("Exiting the inventory system.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
