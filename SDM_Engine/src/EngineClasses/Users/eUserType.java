package EngineClasses.Users;

public enum eUserType {
    Customer, Seller;

    public static eUserType getUserType(String userType) {
        switch (userType.toLowerCase()) {
            case "customer": {
                return Customer;
            }
            case "seller": {
                return Seller;
            }
            default: {
                return null;
            }
        }
    }
}