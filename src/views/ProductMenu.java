package views;

import controllers.ProductMenuControllers;
import models.enums.ProductMenuCommands;
import models.Result;
import java.util.Scanner;
import java.util.regex.Matcher;

public class ProductMenu implements AppMenu {
    private final ProductMenuControllers controller = new ProductMenuControllers();

    @Override
    public void check(Scanner scanner) {

        String input = scanner.nextLine().trim();
        Matcher matcher;
        if ((matcher = ProductMenuCommands.ShowProducts.getMatcher(input)) != null) {
            String sortBy = matcher.group("sortBy");
            Result result = controller.showProducts(sortBy);
            System.out.println(result.getMessage());
        } else if ((matcher = ProductMenuCommands.ShowNext10Products.getMatcher(input)) != null) {
            Result result = controller.showNext10Products();
            System.out.println(result.getMessage());
        } else if ((matcher = ProductMenuCommands.ShowPast10Products.getMatcher(input)) != null) {
            Result result = controller.showPast10Products();
            System.out.println(result.getMessage());
        } else if ((matcher = ProductMenuCommands.ShowInformation.getMatcher(input)) != null) {
            String productId = matcher.group("productId");
            Result result = controller.showInformationOf(productId);
            System.out.println(result.getMessage());
        } else if ((matcher = ProductMenuCommands.RateProduct.getMatcher(input)) != null) {
            String rating = matcher.group("rating");
            String message = matcher.group("message");
            String productId = matcher.group("productId");
            Result result = controller.rateProduct(rating, message, productId);
            System.out.println(result.getMessage());
        } else if ((matcher = ProductMenuCommands.AddToCart.getMatcher(input)) != null) {
            String productId = matcher.group("productId");
            String quantity = matcher.group("quantity");
            Result result = controller.addToCart(productId, quantity);
            System.out.println(result.getMessage());
        } else if (ProductMenuCommands.GoBack.getMatcher(input) != null) {
            System.out.println(controller.goBack());
        } else {
            System.out.println("invalid command");
        }
    }
}
