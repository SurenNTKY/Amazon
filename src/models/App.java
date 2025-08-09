package models;

import java.util.ArrayList;
import models.enums.Menu;

public class App {
    public static final ArrayList<User> user = new ArrayList<>();
    public static final ArrayList<Store> store = new ArrayList<>();

    public static User currentUser = null;
    public static Store currentStore = null;
    private static Menu currentMenu = Menu.MainMenu;

    public static Menu getCurrentMenu() {
        return currentMenu;
    }

    public static void setCurrentMenu(Menu currentMenu) {
        App.currentMenu = currentMenu;
    }

    public static final OrderManager orderManager = new OrderManager();
    public static final UserDataManager userDataManager = new UserDataManager();
    public static final ProductDataManager productDataManager = new ProductDataManager();
}
