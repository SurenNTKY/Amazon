package controllers;

import models.*;
import models.enums.Menu;

import java.util.ArrayList;

public class LoginMenuControllers {

    public Result registerUser(String firstName, String lastName, String password, String reEnteredPassword, String email) {

        Result nameValidation = validateUserName(firstName, lastName);
        if (!nameValidation.getIsSuccessful()) {
            return nameValidation;
        }

        Result passwordValidation = validatePassword(password, reEnteredPassword);
        if (!passwordValidation.getIsSuccessful()) {
            return passwordValidation;
        }

        Result emailValidation = validateEmail(email);
        if (!emailValidation.getIsSuccessful()) {
            return emailValidation;
        }

        if (isEmailExists(email)) {
            return new Result(false, "Email already exists.");
        }

        User newUser = new User(firstName, lastName, password, reEnteredPassword, email);
        App.user.add(newUser);

        return new Result(true, "User account for " + firstName + " " + lastName + " created successfully.");
    }

    public Result registerStore(String brand, String password, String reEnteredPassword, String email) {

        Result brandValidation = validateBrand(brand);
        if (!brandValidation.getIsSuccessful()) {
            return brandValidation;
        }

        Result passwordValidation = validatePassword(password, reEnteredPassword);
        if (!passwordValidation.getIsSuccessful()) {
            return passwordValidation;
        }

        Result emailValidation = validateEmail(email);
        if (!emailValidation.getIsSuccessful()) {
            return emailValidation;
        }

        if (isEmailExists(email)) {
            return new Result(false, "Email already exists.");
        }

        Store newStore = new Store(brand, password, reEnteredPassword, email);
        App.store.add(newStore);

        return new Result(true, "Store account for \"" + brand + "\" created successfully.");
    }

    public Result loginUser(String email, String password) {
        User foundUser = null;
        for (User u : App.user) {
            if (u.getEmail().equals(email)) {
                foundUser = u;
                break;
            }
        }

        if (foundUser == null) {
            return new Result(false, "No user account found with the provided email.");
        }

        if (!foundUser.getPassword().equals(password)) {
            return new Result(false, "Password is incorrect.");
        }

        App.currentUser = foundUser;
        App.currentStore = null;
        App.setCurrentMenu(Menu.MainMenu);

        return new Result(true, "User logged in successfully. Redirecting to the MainMenu ...");
    }

    public Result loginStore(String email, String password) {
        Store foundStore = null;
        for (Store s : App.store) {
            if (s.email().equals(email)) {
                foundStore = s;
                break;
            }
        }

        if (foundStore == null) {
            return new Result(false, "No store account found with the provided email.");
        }

        if (!foundStore.password().equals(password)) {
            return new Result(false, "Password is incorrect.");
        }

        App.currentStore = foundStore;
        App.currentUser = null;
        App.setCurrentMenu(Menu.MainMenu);

        return new Result(true, "Store logged in successfully. Redirecting to the MainMenu ...");
    }

    public Result logout() {
        if (App.currentUser == null && App.currentStore == null) {
            return new Result(false, "You should login first.");
        }

        App.currentUser = null;
        App.currentStore = null;
        App.setCurrentMenu(Menu.MainMenu);

        return new Result(true, "Logged out successfully. Redirecting to the MainMenu ...");
    }

    public Result deleteAccount(String password, String reEnteredPassword) {
        if (App.currentUser == null && App.currentStore == null) {
            return new Result(false, "You should login first.");
        }
        if (!password.equals(reEnteredPassword)) {
            return new Result(false, "Re-entered password is incorrect.");
        }
        if (App.currentUser != null) {
            if (!App.currentUser.getPassword().equals(password)) {
                return new Result(false, "Password is incorrect.");
            }
            ArrayList<CartItem> cart = App.userDataManager.getCart(App.currentUser);
            for (CartItem item : cart) {
                Product p = App.productDataManager.getProductById(item.getProductId());
                if (p != null) {
                    p.setStock(p.getStock() + item.getQuantity());
                }
            }
            App.userDataManager.clearCart(App.currentUser);
            App.user.remove(App.currentUser);
            App.currentUser = null;
            App.setCurrentMenu(Menu.MainMenu);
        } else {
            if (!App.currentStore.password().equals(password)) {
                return new Result(false, "Password is incorrect.");
            }
            App.store.remove(App.currentStore);
            String brand = App.currentStore.brand();
            removeProductsByBrand(brand);
            removeBrandItemsFromAllCarts(brand);
            App.currentStore = null;
            App.setCurrentMenu(Menu.MainMenu);
        }
        return new Result(true, "Account deleted successfully. Redirecting to the MainMenu ...");
    }

    public Result goBack(){
        App.setCurrentMenu(Menu.MainMenu);
        return new Result(true,"Redirecting to the MainMenu ...");
    }

    private Result validateUserName(String firstName, String lastName) {
        if (firstName.length() < 3 || lastName.length() < 3) {
            return new Result(false, "Name is too short.");
        }
        if (!firstName.matches("^[A-Z][a-z]{2,}$") || !lastName.matches("^[A-Z][a-z]{2,}$")) {
            return new Result(false, "Incorrect name format.");
        }
        return new Result(true, "");
    }

    private Result validateBrand(String brand) {
        if (brand.length() < 3) {
            return new Result(false, "Brand name is too short.");
        }
        return new Result(true, "");
    }

    private Result validatePassword(String password, String reEnteredPassword) {
        if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])[A-Za-z0-9]*$")) {
            return new Result(false, "Incorrect password format.");
        }
        if (!password.equals(reEnteredPassword)) {
            return new Result(false, "Re-entered password is incorrect.");
        }
        return new Result(true, "");
    }

    private Result validateEmail(String email) {
        if (!email.matches("^[A-Za-z0-9]+(?:\\.[A-Za-z0-9]*)?@[a-z]+\\.com$")) {
            return new Result(false, "Incorrect email format.");
        }
        String mailPart = email.substring(0, email.indexOf('@'));
        int dotCount = mailPart.length() - mailPart.replace(".", "").length();
        if (dotCount > 1) {
            return new Result(false, "Incorrect email format.");
        }
        return new Result(true, "");
    }

    private boolean isEmailExists(String email) {

        for (User u : App.user) {
            if (u.getEmail().equals(email)) {
                return true;
            }
        }

        for (Store s : App.store) {
            if (s.email().equals(email)) {
                return true;
            }
        }
        return false;
    }

    private void removeProductsByBrand(String brand) {
        App.productDataManager.getAllProducts().removeIf(p -> p.getBrand().equals(brand));
    }

    private void removeBrandItemsFromAllCarts(String brand) {
        for (User u : App.user) {
            ArrayList<CartItem> cart = App.userDataManager.getCart(u);
            cart.removeIf(item -> item.getBrand().equals(brand));
            App.userDataManager.clearCart(u);
            for (CartItem c : cart) {
                App.userDataManager.addToCart(u, c);
            }
        }
    }
}
