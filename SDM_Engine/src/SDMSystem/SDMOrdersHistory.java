package SDMSystem;

import EngineClasses.Interfaces.Containable;
import EngineClasses.Order.Order;

import java.util.ArrayList;
import java.util.List;

public class SDMOrdersHistory implements Containable<Integer, Order> {
    private List<Order> ordersHistory = new ArrayList<>();

    public SDMOrdersHistory() {
        this.ordersHistory = new ArrayList<>();
    }

    @Override
    public boolean isExist(Integer id) {
        for (Order order : ordersHistory) {
            if (order.getId() == id) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Order get(Integer id) {
        for (Order order : ordersHistory) {
            if (order.getId() == id) {
                return order;
            }
        }

        return null;
    }

    public void add(Order order) {
        this.add(order.getId(), order);
    }

    public void add(SDMOrdersHistory ordersHistory){
        for(Order order : ordersHistory.getIterable()){
            this.ordersHistory.add(order);
        }
    }

    public int size() {
        return this.ordersHistory.size();
    }

    @Override
    public void add(Integer id, Order newObj) {
        this.ordersHistory.add(newObj);
    }

    @Override
    public Iterable<Order> getIterable() {
        return this.ordersHistory;
    }
}