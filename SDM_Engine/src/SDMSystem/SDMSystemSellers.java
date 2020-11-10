package SDMSystem;

import EngineClasses.Interfaces.Containable;

import java.util.HashMap;
import java.util.Map;

public class SDMSystemSellers implements Containable<Integer, SDMSystemSeller> {

    private Map<Integer, SDMSystemSeller> idToSeller = new HashMap<>();

    @Override
    public boolean isExist(Integer id) {
        return this.idToSeller.containsKey(id);
    }

    @Override
    public SDMSystemSeller get(Integer id) {
        return this.idToSeller.get(id);
    }

    public SDMSystemSeller get(String username){
        for(SDMSystemSeller seller : this.idToSeller.values()){
            if(seller.getName().equals(username)){
                return seller;
            }
        }

        return null;
    }

    @Override
    public Iterable<SDMSystemSeller> getIterable() {
        return this.idToSeller.values();
    }

    @Override
    public void add(Integer id, SDMSystemSeller newSeller) {
        this.idToSeller.put(id, newSeller);
    }
}