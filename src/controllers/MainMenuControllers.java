package controllers;

import models.App;
import models.Result;
import models.enums.Menu;

public class MainMenuControllers {

    public Result goToMenu(String input) {
        switch (input) {
            case "LoginMenu":
                App.setCurrentMenu(Menu.LoginMenu);
                return new Result(true, "Redirecting to the LoginMenu ...");
            case "UserMenu":
                if (App.currentUser == null) {
                    return new Result(false, "You need to login as a user before accessing the user menu.");
                }
                App.setCurrentMenu(Menu.UserMenu);
                return new Result(true, "Redirecting to the UserMenu ...");
            case "StoreMenu":
                if (App.currentStore == null) {
                    return new Result(false,"You should login as store before accessing the store menu.");
                }
                App.setCurrentMenu(Menu.StoreMenu);
                return new Result(true, "Redirecting to the StoreMenu ...");
            case "ProductMenu":
                App.setCurrentMenu(Menu.ProductMenu);
                return new Result(true, "Redirecting to the ProductMenu ...");
            default:
                return new Result(false, "invalid command");
        }
    }

    public Result exit() {
        App.setCurrentMenu(Menu.ExitMenu);
        return new Result(true, "");
    }
}