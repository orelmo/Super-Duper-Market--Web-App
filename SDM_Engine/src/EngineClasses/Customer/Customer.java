package EngineClasses.Customer;

import EngineClasses.Interfaces.Locationable;
import EngineClasses.Location.Location;
import EngineClasses.Users.User;
import Exceptions.LocationException;
import OrderConteiner.OrderContainer;
import OrderConteiner.OrdersContainer;
import SDMSystem.SDMZone;

public class Customer extends User  {

    private OrderContainer currentOrder;

    public Customer(){}

    public Customer(User newUser) {
        super(newUser);
    }

    public OrderContainer getCurrentOrder() {
        return this.currentOrder;
    }

    public void setCurrentOrder(OrderContainer currentOrder) {
        this.currentOrder = currentOrder;
    }

    @Override
    public String toString() {
        return "Serial Number: " + this.getId() + ", Name: " + this.getName();
    }
}