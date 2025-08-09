package models.enums;

import views.*;

import java.util.Scanner;

public enum Menu {
    LoginMenu(new LoginMenu()),
    MainMenu(new MainMenu()),
    ProductMenu(new ProductMenu()),
    StoreMenu(new StoreMenu()),
    UserMenu(new UserMenu()),
    ExitMenu(new ExitMenu());

    private final AppMenu menu;

    Menu(AppMenu menu) {
        this.menu = menu;
    }

    public void checkCommand(Scanner scanner) {
        this.menu.check(scanner);
    }

}
