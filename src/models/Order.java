package models;

import java.util.ArrayList;

public record Order(int orderId, String shippingAddress, double totalPaid, ArrayList<CartItem> items) {
}
