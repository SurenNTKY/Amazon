package views;

import controllers.LoginMenuControllers;
import models.enums.LoginMenuCommands;

import java.awt.event.ActionListener;
import java.util.Scanner;
import java.util.regex.Matcher;

public class LoginMenu implements AppMenu{
    private final LoginMenuControllers controller = new LoginMenuControllers();

    @Override
    public void check(Scanner scanner) {

        String input = scanner.nextLine().trim();
        Matcher matcher;

        if ((matcher = LoginMenuCommands.RegisterUser.getMatcher(input)) != null) {
            System.out.println(controller.registerUser(
                    matcher.group("firstname"),
                    matcher.group("lastname"),
                    matcher.group("password"),
                    matcher.group("repassword"),
                    matcher.group("email")
            ).toString());
        } else if ((matcher = LoginMenuCommands.RegisterStore.getMatcher(input)) != null) {
            System.out.println(controller.registerStore(
                    matcher.group("brand"),
                    matcher.group("password"),
                    matcher.group("repassword"),
                    matcher.group("email")
            ).toString());
        } else if ((matcher = LoginMenuCommands.LoginUser.getMatcher(input)) != null) {
            System.out.println(controller.loginUser(
                    matcher.group("email"),
                    matcher.group("password")
            ).toString());
        } else if ((matcher = LoginMenuCommands.LoginStore.getMatcher(input)) != null) {
            System.out.println(controller.loginStore(
                    matcher.group("email"),
                    matcher.group("password")
            ).toString());
        } else if ((matcher = LoginMenuCommands.DeleteAccount.getMatcher(input)) != null) {
            System.out.println(controller.deleteAccount(
                    matcher.group("password"),
                    matcher.group("repassword")
            ).toString());
        } else if ((matcher = LoginMenuCommands.GoBack.getMatcher(input)) != null) {
            System.out.println(controller.goBack());
        } else if ((matcher = LoginMenuCommands.Logout.getMatcher(input)) != null) {
            System.out.println(controller.logout());
        } else {
            System.out.println("invalid command");
        }
    }
}
