import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<String, Cake> cakes;

    public Inventory() {
        cakes = new HashMap<>();
    }

    public void addCake(Cake cake) {
        if (cakes.containsKey(cake.getName())) {
            Cake existingCake = cakes.get(cake.getName());
            existingCake.setQuantity(existingCake.getQuantity() + cake.getQuantity());
        } else {
            cakes.put(cake.getName(), cake);
        }
    }

    public Cake getCake(String name) {
        return cakes.get(name);
    }

    public void updateCakeQuantity(String cakeName, int newQuantity) {
        Cake cake = cakes.get(cakeName); // Use 'cakes' instead of 'inventoryMap'
        if (cake != null) {
            cake.setQuantity(newQuantity);
            // If you need to save the updated quantity in a database or file, ensure that happens here
        } else {
            // Handle the case where the cake is not found in the inventory
            System.out.println("Cake not found: " + cakeName);
        }
    }

    public void removeCake(String name, int quantity) {
        Cake cake = cakes.get(name);
        if (cake != null) {
            if (cake.getQuantity() >= quantity) {
                cake.setQuantity(cake.getQuantity() - quantity);
                // Optionally remove the cake if quantity reaches zero
                if (cake.getQuantity() == 0) {
                    cakes.remove(name);
                }
            } else {
                // Handle the case where the quantity is insufficient
                System.out.println("Not enough stock for: " + name);
            }
        } else {
            // Handle the case where the cake is not found in the inventory
            System.out.println("Cake not found: " + name);
        }
    }

    public Map<String, Cake> getAllCakes() {
        return cakes;
    }
}


