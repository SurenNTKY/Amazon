package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Product {
    private final int productId;
    private final String name;
    private final double producerCost;
    private double price;
    private double discountPercentage;
    private int discountQuantity;
    private final String brand;
    private int stock;
    private int numberOfSold;
    private double rating;
    private final LocalDateTime dateAdded;
    private final String aboutThisItem;
    private final List<Review> reviews;

    public Product(int productId, String name, double producerCost, double price, double discountPercentage,
                   String brand, int stock, int numberOfSold, double rating,
                   LocalDateTime dateAdded, String aboutThisItem) {
        this.productId = productId;
        this.name = name;
        this.producerCost = producerCost;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.discountQuantity = 0;
        this.brand = brand;
        this.stock = stock;
        this.numberOfSold = numberOfSold;
        this.rating = rating;
        this.dateAdded = dateAdded;
        this.aboutThisItem = aboutThisItem;
        this.reviews = new ArrayList<>();
    }

    public int getProductId() { return productId; }

    public String getName() { return name; }

    public double getProducerCost() { return producerCost; }

    public double getPrice() { return price; }

    public double getDiscountedPrice() {
        if (discountPercentage <= 0) {
            return price;
        }
        return price * (1 - discountPercentage / 100.0);
    }

    public double getDiscountPercentage() { return discountPercentage; }

    public int getDiscountQuantity() { return discountQuantity; }

    public String getBrand() { return brand; }

    public int getStock() { return stock; }

    public int getNumberOfSold() { return numberOfSold; }

    public double getRating() { return rating; }

    public LocalDateTime getDateAdded() { return dateAdded; }

    public String getAboutThisItem() { return aboutThisItem; }

    public List<Review> getReviews() { return reviews; }

    public void addReview(Review review) {
        reviews.add(review);
        recalculateRating();
    }

    private void recalculateRating() {
        double sum = 0;
        for (Review r : reviews) {
            sum += r.rating();
        }
        if (!reviews.isEmpty()) {
            rating = sum / reviews.size();
        }
    }

    public void setPrice(double price) { this.price = price; }

    public void setDiscountPercentage(double discountPercentage) { this.discountPercentage = discountPercentage; }

    public void setDiscount(int discountPercentage, int discountQuantity) {
        this.discountPercentage = discountPercentage;
        this.discountQuantity = discountQuantity;
    }

    public void setStock(int stock) { this.stock = stock; }

    public void increaseNumberOfSold(int quantity) {
        this.numberOfSold += quantity;
    }

}
