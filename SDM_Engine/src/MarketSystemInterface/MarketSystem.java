package MarketSystemInterface;

import ItemsDetailsContainer.ItemsDetailsContainer;
import OrderConteiner.OrderContainer;
import StoresDetailsConteiner.StoresDetailsContainer;


public interface MarketSystem {
    public boolean loadFile(String content, String username, StringBuilder errorMessage);
    public void fillStoresDetailsContainer(StoresDetailsContainer container);
    public ItemsDetailsContainer getAllItemsDetails();
    public void executeOrder(OrderContainer container);
    public void updateItemPriceInStore(int storeId, int itemId, int itemNewPrice);
    public void addItemToStore(int storeId, int itemId, int itemPrice);
    public void deleteItemFromStore(int storeId, int itemId);
    public void exitSystem();
}
