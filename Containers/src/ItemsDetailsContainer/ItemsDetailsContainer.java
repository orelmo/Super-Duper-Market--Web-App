package ItemsDetailsContainer;

import java.util.ArrayList;
import java.util.List;

public class ItemsDetailsContainer {
    List<ItemDetailsContainer> itemsDetailsContainer = new ArrayList<>();

    public void add(ItemDetailsContainer itemDetailsContainer) {
        this.itemsDetailsContainer.add(itemDetailsContainer);
    }

    public List<ItemDetailsContainer> getIteratable(){
        return this.itemsDetailsContainer;
    }
}
