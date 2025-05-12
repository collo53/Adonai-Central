import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<Cake> cakes;
    


    public Order() {
        this.cakes = new ArrayList<>();
        
    }
    

    public void addCake(Cake cake) {
        cakes.add(cake);
    }

    public List<Cake> getCakes() {
        return cakes;
    }

    public double getTotal() {
        return cakes.stream().mapToDouble(Cake::getTotalPrice).sum();
    }

    
}
