package views;

import controllers.StoreMenuControllers;
import models.enums.StoreMenuCommands;
import models.Result;

import java.util.Scanner;
import java.util.regex.Matcher;

public class StoreMenu implements AppMenu {
    private final StoreMenuControllers controller = new StoreMenuControllers();

    @Override
    public void check(Scanner scanner) {

        String input = scanner.nextLine().trim();
        Matcher matcher;
        if ((matcher = StoreMenuCommands.AddProduct.getMatcher(input)) != null) {
            String name = matcher.group("name");
            String producerCost = matcher.group("producerCost");
            String price = matcher.group("price");
            String about = matcher.group("about");
            String np = matcher.group("np");
            Result result = controller.addProduct(name, producerCost, price, about, np);
            System.out.println(result.getMessage());
        } else if ((matcher = StoreMenuCommands.ApplyDiscount.getMatcher(input)) != null) {
            String productId = matcher.group("productId");
            String discount = matcher.group("discount");
            String quantity = matcher.group("quantity");
            Result result = controller.applyDiscount(productId, discount, quantity);
            System.out.println(result.getMessage());
        } else if ((matcher = StoreMenuCommands.ShowProfit.getMatcher(input)) != null) {
            Result result = controller.showProfit();
            System.out.println(result.getMessage());
        } else if ((matcher = StoreMenuCommands.ShowListOfProducts.getMatcher(input)) != null) {
            Result result = controller.showListOfProducts();
            System.out.println(result.getMessage());
        } else if ((matcher = StoreMenuCommands.AddStock.getMatcher(input)) != null) {
            String productId = matcher.group("productId");
            String amount = matcher.group("amount");
            Result result = controller.addStock(productId, amount);
            System.out.println(result.getMessage());
        } else if ((matcher = StoreMenuCommands.UpdatePrice.getMatcher(input)) != null) {
            String productId = matcher.group("productId");
            String newPrice = matcher.group("newPrice");
            Result result = controller.updatePrice(productId, newPrice);
            System.out.println(result.getMessage());
        } else if (StoreMenuCommands.GoBack.getMatcher(input) != null) {
            System.out.println(controller.goBack());
        } else {
            System.out.println("invalid command");
        }
    }
}
