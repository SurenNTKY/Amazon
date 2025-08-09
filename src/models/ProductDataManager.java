package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProductDataManager {
    private final List<Product> products;
    private double totalProductionCost = 0;

    public ProductDataManager() {
        products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
        totalProductionCost += product.getProducerCost() * product.getStock();
    }

    public Product getProductById(int productId) {
        for (Product p : products) {
            if (p.getProductId() == productId) {
                return p;
            }
        }
        return null;
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public int getNextProductId() {
        return products.size() + 101;
    }

    public double getTotalRevenue() {
        double revenue = 0;
        for (Product p : products) {
            revenue += p.getNumberOfSold() * p.getDiscountedPrice();
        }
        return revenue;
    }

    public double getTotalCost() {
        return totalProductionCost;
    }

    public List<Product> getProductsSortedByDateAdded() {
        List<Product> sorted = new ArrayList<>(products);
        sorted.sort(Comparator.comparing(Product::getDateAdded));
        return sorted;
    }

    public List<Product> getSortedProducts(String sortBy) {
        List<Product> sorted = new ArrayList<>(products);
        switch (sortBy.toLowerCase()) {
            case "rate":
                sorted.sort((p1, p2) -> {
                    int cmp = Double.compare(p2.getRating(), p1.getRating());
                    if (cmp == 0) {
                        return p1.getDateAdded().compareTo(p2.getDateAdded());
                    }
                    return cmp;
                });
                break;
            case "higher price to lower":
                sorted.sort((p1, p2) -> {
                    double diff = p2.getDiscountedPrice() - p1.getDiscountedPrice();
                    if (diff == 0) {
                        return p1.getDateAdded().compareTo(p2.getDateAdded());
                    }
                    return (diff > 0) ? 1 : -1;
                });
                break;
            case "lower price to higher":
                sorted.sort((p1, p2) -> {
                    double diff = p1.getDiscountedPrice() - p2.getDiscountedPrice();
                    if (diff == 0) {
                        return p1.getDateAdded().compareTo(p2.getDateAdded());
                    }
                    return (diff > 0) ? 1 : -1;
                });
                break;
            case "number of sold":
                sorted.sort((p1, p2) -> {
                    int cmp = Integer.compare(p2.getNumberOfSold(), p1.getNumberOfSold());
                    if (cmp == 0) {
                        return p1.getDateAdded().compareTo(p2.getDateAdded());
                    }
                    return cmp;
                });
                break;
            default:
                sorted.sort(Comparator.comparing(Product::getDateAdded));
                break;
        }
        return sorted;
    }

}
