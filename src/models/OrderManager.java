package models;

import java.util.ArrayList;

public class OrderManager {
    private final ArrayList<Order> orders = new ArrayList<>();

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public int placeOrder(User user, int cardId, int addressId) {
        if (user == null) return 0;
        ArrayList<CartItem> cart = App.userDataManager.getCart(user);
        if (cart == null || cart.isEmpty()) return 0;

        double total = 0;
        for (CartItem item : cart) {
            total += item.getPrice() * item.getQuantity();
        }

        String shippingAddress = App.userDataManager.getAddressDetails(user, addressId);
        int orderId = orders.size() + 101;
        Order order = new Order(orderId, shippingAddress, total, new ArrayList<>(cart));
        orders.add(order);

        App.userDataManager.clearCart(user);
        return orderId;
    }
}
