package models;

public class CartItem {
    private final int productId;
    private final String productName;
    private int quantity;
    private final double price;
    private final String brand;
    private final double rating;

    public CartItem(int productId, String productName, int quantity, double price, String brand, double rating) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.brand = brand;
        this.rating = rating;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getBrand() {
        return brand;
    }

    public double getRating() {
        return rating;
    }
}
