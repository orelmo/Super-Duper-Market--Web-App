package EngineClasses.Item;

import com.sun.xml.internal.ws.util.StringUtils;
import jaxbClasses.SDMItem;

public class Item {
    private String name;
    private ePurchaseCategory purchaseCategory;
    private int id;

    public Item(String name, ePurchaseCategory purchaseCategory, int id){
        this.name = name;
        this.purchaseCategory = purchaseCategory;
        this.id = id;
    }

     public Item(SDMItem SDMitem) {
         this.id = SDMitem.getId();
         this.purchaseCategory = ePurchaseCategory.valueOf(SDMitem.getPurchaseCategory());
         this.name = toTitleCase(SDMitem.getName());
     }

     public Item(Item otherItem){
         this.name = otherItem.name;
         this.id = otherItem.id;
         this.purchaseCategory = otherItem.purchaseCategory;
     }

    public String getName() {
        return this.name;
    }

    public ePurchaseCategory getPurchaseCategory() {
        return this.purchaseCategory;
    }

    public int getId() {
        return this.id;
    }

    private String toTitleCase(String str) {
        StringBuilder sb = new StringBuilder();
        for (String word : str.split(" ")) {
            sb.append(StringUtils.capitalize(word.toLowerCase()));
            sb.append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    @Override
    public String toString(){
        return this.getName() + ", id: " + this.getId() + ", Category: " + this.getPurchaseCategory();
    }
}
