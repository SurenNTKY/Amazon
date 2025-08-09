package controllers;

import models.App;
import models.CartItem;
import models.Product;
import models.Review;
import models.Result;
import models.enums.Menu;
import java.util.ArrayList;
import java.util.List;

public class ProductMenuControllers {
    private List<Product> currentProductList = new ArrayList<>();
    private String currentSortBy = "";
    private int currentPage = 0;
    private final int pageSize = 10;

    public Result showProducts(String sortBy) {
        currentSortBy = (sortBy == null || sortBy.isEmpty()) ? "Date" : sortBy.substring(0, 1).toUpperCase() + sortBy.substring(1);
        currentProductList = App.productDataManager.getSortedProducts(currentSortBy);
        currentPage = 0;
        return showPage();
    }

    public Result showNext10Products() {
        if (currentProductList.isEmpty()) {
            return new Result(false, "No products to show. Please use 'show products -sortBy ...' first.");
        }
        if ((currentPage + 1) * pageSize < currentProductList.size()) {
            currentPage++;
            return showPage();
        } else {
            return new Result(false, "No more products available.");
        }
    }

    public Result showPast10Products() {
        if (currentProductList.isEmpty()) {
            return new Result(false, "No products to show. Please use 'show products -sortBy ...' first.");
        }
        if (currentPage > 0) {
            currentPage--;
            return showPage();
        } else {
            return new Result(false, "No more products available.");
        }
    }

    private Result showPage() {
        if (currentProductList.isEmpty()) {
            return new Result(false, "No products available.");
        }
        int startIndex = currentPage * pageSize;
        int endIndex = Math.min(startIndex + pageSize, currentProductList.size());
        List<Product> page = currentProductList.subList(startIndex, endIndex);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Product List (Sorted by: %s)\n", currentSortBy));
        sb.append("------------------------------------------------\n");
        for (Product p : page) {
            boolean isSoldOut = (p.getStock() <= 0);
            boolean isOnSale = (p.getDiscountPercentage() > 0 && p.getDiscountQuantity() > 0);
            sb.append(String.format("ID: %d", p.getProductId()));
            if (isOnSale) {
                sb.append(String.format("  **(On Sale! %d units discounted)**\n", p.getDiscountQuantity()));
            } else if (isSoldOut) {
                sb.append("  **(Sold out!)**\n");
            } else {
                sb.append("\n");
            }
            sb.append(String.format("Name: %s\n", p.getName()));
            sb.append(String.format("Rate: %.1f/5\n", p.getRating()));
            if (isOnSale && !isSoldOut) {
                sb.append(String.format("Price: ~$%.1f~ → $%.1f (-%.0f%%)\n", p.getPrice(), p.getDiscountedPrice(), p.getDiscountPercentage()));
            } else {
                sb.append(String.format("Price: $%.1f\n", p.getDiscountedPrice()));
            }
            sb.append(String.format("Brand: %s\n", p.getBrand()));
            sb.append(String.format("Stock: %d\n", p.getStock()));
            sb.append("------------------------------------------------\n");
        }
        sb.append(String.format("(Showing %d-%d out of %d)\n", startIndex + 1, startIndex + pageSize, currentProductList.size()));
        if (endIndex < currentProductList.size()) {
            sb.append("Use `show next 10 products' to see more.");
        }

        return new Result(true, sb.toString());
    }

    public Result showInformationOf(String productIdStr) {
        int productId;
        try {
            productId = Integer.parseInt(productIdStr);
        } catch (NumberFormatException e) {
            return new Result(false, "No product found.");
        }
        Product product = App.productDataManager.getProductById(productId);
        if (product == null) {
            return new Result(false, "No product found.");
        }
        boolean isSoldOut = (product.getStock() <= 0);
        boolean isOnSale = (product.getDiscountPercentage() > 0 && product.getDiscountQuantity() > 0);
        StringBuilder sb = new StringBuilder();
        sb.append("Product Details\n");
        sb.append("------------------------------------------------\n");
        if (isSoldOut) {
            sb.append(String.format("Name: %s  **(Sold out!)**\n", product.getName()));
        } else if (isOnSale) {
            sb.append(String.format("Name: %s  **(On Sale! %d units discounted)**\n", product.getName(), product.getDiscountQuantity()));
        } else {
            sb.append(String.format("Name: %s\n", product.getName()));
        }
        sb.append(String.format("ID: %d\n", product.getProductId()));
        sb.append(String.format("Rating: %.1f/5\n", product.getRating()));
        if (isOnSale && !isSoldOut) {
            sb.append(String.format("Price: ~$%.1f~ → $%.1f (-%.0f%%)\n", product.getPrice(), product.getDiscountedPrice(), product.getDiscountPercentage()));
        } else {
            sb.append(String.format("Price: $%.1f\n", product.getDiscountedPrice()));
        }
        sb.append(String.format("Brand: %s\n", product.getBrand()));
        sb.append(String.format("Number of Products Remaining: %d\n", product.getStock()));
        sb.append(String.format("About this item:\n%s\n\n", product.getAboutThisItem()));
        sb.append("Customer Reviews:\n");
        sb.append("------------------------------------------------\n");

        var reviews = product.getReviews();
        for (int i = 0; i < reviews.size(); i++) {
            Review r = reviews.get(i);
            sb.append(String.format("%s (%d/5)\n", r.reviewerName(), r.rating()));
            if (!r.message().trim().isEmpty()) {
                sb.append(String.format("\"%s\"\n", r.message()));
            }
            sb.append("------------------------------------------------");
            if (i < reviews.size() - 1) {
                sb.append("\n");
            }
        }
        return new Result(true, sb.toString());
    }

    public Result rateProduct(String ratingStr, String message, String productIdStr) {
        if (App.currentUser == null) {
            return new Result(false, "You must be logged in to rate a product.");
        }
        int productId, rating;
        try {
            rating = Integer.parseInt(ratingStr);
            productId = Integer.parseInt(productIdStr);
        } catch (NumberFormatException e) {
            return new Result(false, "No product found.");
        }
        if (rating < 1 || rating > 5) {
            return new Result(false, "Rating must be between 1 and 5.");
        }
        Product product = App.productDataManager.getProductById(productId);
        if (product == null) {
            return new Result(false, "No product found.");
        }
        String reviewerName = App.currentUser.getFirstName() + " " + App.currentUser.getLastName();
        Review review = new Review(reviewerName, rating, (message == null ? "" : message));
        product.addReview(review);
        return new Result(true, "Thank you! Your rating and review have been submitted successfully.");
    }

    public Result addToCart(String productIdStr, String quantityStr) {
        if (App.currentUser == null) {
            return new Result(false, "You must be logged in to add items to your cart.");
        }
        int productId, quantity;
        try {
            productId = Integer.parseInt(productIdStr);
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            return new Result(false, "No product found.");
        }
        if (quantity <= 0) {
            return new Result(false, "Quantity must be a positive number.");
        }
        Product product = App.productDataManager.getProductById(productId);
        if (product == null) {
            return new Result(false, "No product found.");
        }
        if (product.getStock() < quantity) {
            return new Result(false, String.format("Only %d units of \"%s\" are available.", product.getStock(), product.getName()));
        }

        CartItem item = new CartItem(productId, product.getName(), quantity,
                product.getDiscountedPrice(), product.getBrand(), product.getRating());
        App.userDataManager.addToCart(App.currentUser, item);

        product.setStock(product.getStock() - quantity);

        return new Result(true, String.format("\"%s\" (x%d) has been added to your cart.", product.getName(), quantity));
    }

    public Result goBack(){
        App.setCurrentMenu(Menu.MainMenu);
        return new Result(true,"Redirecting to the MainMenu ...");
    }
}
