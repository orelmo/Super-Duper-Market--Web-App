package SDMSystem;

import EngineClasses.Interfaces.Containable;

import java.util.HashMap;
import java.util.Map;

public class SDMSystemCustomers implements Containable<Integer, SDMSystemCustomer> {

    private Map<Integer, SDMSystemCustomer> idToCustomer = new HashMap<>();

    @Override
    public boolean isExist(Integer key) {
        return this.idToCustomer.containsKey(key);
    }

    @Override
    public SDMSystemCustomer get(Integer key) {
        return this.idToCustomer.get(key);
    }

    public SDMSystemCustomer get(String username) {
        for(SDMSystemCustomer customer : this.getIterable()){
            if(customer.getName().equals(username)){
                return customer;
            }
        }
        return null;
    }

        @Override
    public void add(Integer key, SDMSystemCustomer newObj) {
        this.idToCustomer.put(key, newObj);
    }

    @Override
    public Iterable<SDMSystemCustomer> getIterable() {
        return this.idToCustomer.values();
    }
}