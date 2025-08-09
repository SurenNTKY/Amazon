package controllers;

import models.App;
import models.Product;
import models.Result;
import models.enums.Menu;
import java.time.LocalDateTime;
import java.util.List;

public class StoreMenuControllers {

    public Result addProduct(String name, String producerCostStr, String priceStr, String about, String npStr) {
        double producerCost, price;
        int np;
        try {
            producerCost = Double.parseDouble(producerCostStr);
            price = Double.parseDouble(priceStr);
            np = Integer.parseInt(npStr);
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid input.");
        }
        if (price < producerCost) {
            return new Result(false, "Selling price must be greater than or equal to the producer cost.");
        }
        if (np <= 0) {
            return new Result(false, "Number of products must be a positive number.");
        }

        int id = App.productDataManager.getNextProductId();
        String brand = App.currentStore.brand();
        Product product = new Product(id, name, producerCost, price, 0.0, brand, np, 0, 2.5, LocalDateTime.now(), about);
        App.productDataManager.addProduct(product);
        return new Result(true, String.format("Product \"%s\" has been added successfully with ID %d.", name, id));
    }

    public Result applyDiscount(String productIdStr, String discountStr, String quantityStr) {
        int productId, discount, quantity;
        try {
            productId = Integer.parseInt(productIdStr);
            discount = Integer.parseInt(discountStr);
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid input.");
        }
        if (discount < 1 || discount > 100) {
            return new Result(false, "Discount percentage must be between 1 and 100.");
        }
        Product product = App.productDataManager.getProductById(productId);
        if (product == null) {
            return new Result(false, "No product found.");
        }
        if (quantity > product.getStock()) {
            return new Result(false, "Oops! Not enough stock to apply the discount to that many items.");
        }
        product.setDiscount(discount, quantity);
        return new Result(true, String.format("A %d%% discount has been applied to %d units of product ID %d.", discount, quantity, productId));
    }

    public Result showProfit() {
        double revenue = App.productDataManager.getTotalRevenue();
        double productionCost = App.productDataManager.getTotalCost();
        double profit = revenue - productionCost;
        return new Result(true, String.format("Total Profit: $%.1f\n(Revenue: $%.1f - Costs: $%.1f)", profit, revenue, productionCost));
    }

    public Result showListOfProducts() {
        List<Product> products = App.productDataManager.getProductsSortedByDateAdded();
        if (products.isEmpty()) {
            return new Result(false, "No products available in the store.");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Store Products (Sorted by date added)\n------------------------------------------------\n");

        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);

            if (p.getStock() == 0) {
                sb.append(String.format("ID: %d  (**Sold out!**)\n", p.getProductId()));
            } else if (p.getDiscountPercentage() > 0) {
                sb.append(String.format("ID: %d  **(On Sale! %d units discounted)**\n", p.getProductId(), p.getDiscountQuantity()));
            } else {
                sb.append(String.format("ID: %d\n", p.getProductId()));
            }
            sb.append(String.format("Name: %s\n", p.getName()));
            if (p.getDiscountPercentage() > 0 && p.getStock() > 0) {
                sb.append(String.format("Price: ~$%.1f~ → $%.1f (-%.0f%%)\n", p.getPrice(), p.getDiscountedPrice(), p.getDiscountPercentage()));
            } else {
                sb.append(String.format("Price: $%.1f\n", p.getPrice()));
            }
            sb.append(String.format("Stock: %d\n", p.getStock()));
            sb.append(String.format("Sold: %d\n", p.getNumberOfSold()));

            if (i != products.size() - 1) {
                sb.append("------------------------------------------------\n");
            } else {
                sb.append("------------------------------------------------");
            }
        }
        return new Result(true, sb.toString());
    }

    public Result addStock(String productIdStr, String amountStr) {
        int productId, amount;
        try {
            productId = Integer.parseInt(productIdStr);
            amount = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid input.");
        }
        if (amount <= 0) {
            return new Result(false, "Amount must be a positive number.");
        }
        Product product = App.productDataManager.getProductById(productId);
        if (product == null) {
            return new Result(false, "No product found.");
        }
        product.setStock(product.getStock() + amount);
        return new Result(true, String.format("%d units of \"%s\" have been added to the stock.", amount, product.getName()));
    }

    public Result updatePrice(String productIdStr, String newPriceStr) {
        int productId;
        double newPrice;
        try {
            productId = Integer.parseInt(productIdStr);
            newPrice = Double.parseDouble(newPriceStr);
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid input.");
        }
        if (newPrice <= 0) {
            return new Result(false, "Price must be a positive number.");
        }
        Product product = App.productDataManager.getProductById(productId);
        if (product == null) {
            return new Result(false, "No product found.");
        }
        product.setPrice(newPrice);
        return new Result(true, String.format("Price of \"%s\" has been updated to $%.1f.", product.getName(), newPrice));
    }

    public Result goBack() {
        App.setCurrentMenu(Menu.MainMenu);
        return new Result(true, "Redirecting to the MainMenu ...");
    }
}
