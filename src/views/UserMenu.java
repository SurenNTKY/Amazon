package views;

import controllers.UserMenuControllers;
import models.enums.UserMenuCommands;

import java.util.Scanner;
import java.util.regex.Matcher;

public class UserMenu implements AppMenu {
    private final UserMenuControllers controller = new UserMenuControllers();

    @Override
    public void check(Scanner scanner) {

        String input = scanner.nextLine().trim();
        Matcher matcher;

        if ((matcher = UserMenuCommands.ListMyOrders.getMatcher(input)) != null) {
            System.out.println(controller.listMyOrders());
        } else if ((matcher = UserMenuCommands.ShowOrderDetails.getMatcher(input)) != null) {
            System.out.println(controller.showOrderDetails(matcher.group(1)));
        } else if ((matcher = UserMenuCommands.EditName.getMatcher(input)) != null) {
            System.out.println(controller.editName(
                    matcher.group("firstName"),
                    matcher.group("lastName"),
                    matcher.group("password")
            ));
        } else if ((matcher = UserMenuCommands.EditEmail.getMatcher(input)) != null) {
            System.out.println(controller.editEmail(
                    matcher.group("email"),
                    matcher.group("password")
            ));
        } else if ((matcher = UserMenuCommands.EditPassword.getMatcher(input)) != null) {
            System.out.println(controller.editPassword(
                    matcher.group("newPassword"),
                    matcher.group("oldPassword")
            ));
        } else if ((matcher = UserMenuCommands.ShowMyInfo.getMatcher(input)) != null) {
            System.out.println(controller.showMyInfo());
        } else if ((matcher = UserMenuCommands.AddAddress.getMatcher(input)) != null) {
            System.out.println(controller.addAddress(
                    matcher.group("country"),
                    matcher.group("city"),
                    matcher.group("street"),
                    matcher.group("postal")
            ));
        } else if ((matcher = UserMenuCommands.DeleteAddress.getMatcher(input)) != null) {
            System.out.println(controller.deleteAddress(matcher.group("addressId")));
        } else if ((matcher = UserMenuCommands.ListMyAddresses.getMatcher(input)) != null) {
            System.out.println(controller.listMyAddresses());
        } else if ((matcher = UserMenuCommands.AddCreditCard.getMatcher(input)) != null) {
            System.out.println(controller.addCreditCard(
                    matcher.group("cardNumber"),
                    matcher.group("expirationDate"),
                    matcher.group("cvv"),
                    matcher.group("initialValue")
            ));
        } else if ((matcher = UserMenuCommands.ChargeCredit.getMatcher(input)) != null) {
            System.out.println(controller.chargeCredit(
                    matcher.group("amount"),
                    matcher.group("cardId")
            ));
        } else if ((matcher = UserMenuCommands.CheckCredit.getMatcher(input)) != null) {
            System.out.println(controller.checkCredit(
                    matcher.group("cardId")
            ));
        } else if ((matcher = UserMenuCommands.ShowProductInCart.getMatcher(input)) != null) {
            System.out.println(controller.showProductsInCart());
        } else if ((matcher = UserMenuCommands.CheckOut.getMatcher(input)) != null) {
            System.out.println(controller.checkOut(
                    matcher.group("cardId"),
                    matcher.group("addressId")
            ));
        } else if ((matcher = UserMenuCommands.RemoveFromCart.getMatcher(input)) != null) {
            System.out.println(controller.removeFromCart(
                    matcher.group("productId"),
                    matcher.group("quantity")
            ));
        } else if (UserMenuCommands.GoBack.getMatcher(input) != null) {
            System.out.println(controller.goBack());
        } else{
            System.out.println("invalid command");
        }
    }
}
