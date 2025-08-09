package controllers;

import models.*;
import models.enums.Menu;

import java.util.ArrayList;

public class UserMenuControllers {

    public Result listMyOrders() {
        var orders = App.orderManager.getOrders();
        if (orders.isEmpty()) {
            return new Result(false, "No orders found.");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("order History\n");
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");

        for (int i = 0; i < orders.size(); i++) {
            var order = orders.get(i);
            sb.append(String.format("Order ID: %d\n", order.orderId()));
            sb.append("Shipping Address: " + order.shippingAddress() + "\n");

            int totalItems = 0;
            for (CartItem item : order.items()) {
                totalItems += item.getQuantity();
            }
            sb.append(String.format("Total Items Ordered: %d\n\n", totalItems));

            var sortedItems = new ArrayList<>(order.items());
            sortedItems.sort((item1, item2) -> item1.getProductName().compareToIgnoreCase(item2.getProductName()));
            sb.append("Products (Sorted by Name):\n");
            int counter = 1;
            for (var item : sortedItems) {
                sb.append(String.format("  %d- %s\n", counter, item.getProductName()));
                counter++;
            }

            if (i == orders.size() - 1) {
                sb.append("\n━━━━━━━━━━━━━━━━━━━━━━━━━━");
            } else {
                sb.append("\n━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
            }
        }
        return new Result(true, sb.toString());
    }

    public Result showOrderDetails(String orderIdStr) {
        int orderId;
        try {
            orderId = Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            return new Result(false, "Order not found.");
        }
        var orderOpt = App.orderManager.getOrders()
                .stream()
                .filter(o -> o.orderId() == orderId)
                .findFirst();
        if (orderOpt.isEmpty()) {
            return new Result(false, "Order not found.");
        }
        var order = orderOpt.get();
        StringBuilder sb = new StringBuilder();
        sb.append("Products in this order:\n\n");
        var sortedItems = new ArrayList<>(order.items());
        sortedItems.sort((item1, item2) -> Integer.compare(item1.getProductId(), item2.getProductId()));
        int counter = 1;
        for (var item : sortedItems) {
            sb.append(String.format("%d- Product Name: %s\n", counter, item.getProductName()));
            sb.append(String.format("    ID: %d\n", item.getProductId()));
            sb.append("    Brand: " + item.getBrand() + "\n");
            sb.append(String.format("    Rating: %.1f/5\n", item.getRating()));
            sb.append(String.format("    Quantity: %d\n", item.getQuantity()));
            if (item.getQuantity() > 1) {
                sb.append(String.format("    Price: $%.1f each\n\n", item.getPrice()));
            } else {
                sb.append(String.format("    Price: $%.1f\n\n", item.getPrice()));
            }
            counter++;
        }
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        sb.append(String.format("**Total Cost: $%.1f**", order.totalPaid()));
        return new Result(true, sb.toString());
    }

    public Result editName(String firstName, String lastName, String password) {
        User user = App.currentUser;
        if (!user.getPassword().equals(password)) {
            return new Result(false, "Incorrect password. Please try again.");
        }
        if (user.getFirstName().equals(firstName) || user.getLastName().equals(lastName)) {
            return new Result(false, "The new name must be different from the current name.");
        }
        if (firstName.length() < 3 || lastName.length() < 3) {
            return new Result(false, "Name is too short.");
        }
        if (!firstName.matches("^[A-Z][a-z]{2,}$") || !lastName.matches("^[A-Z][a-z]{2,}$")) {
            return new Result(false, "Incorrect name format.");
        }
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return new Result(true, "Name updated successfully.");
    }

    public Result editEmail(String email, String password) {
        User user = App.currentUser;
        if (!user.getPassword().equals(password)) {
            return new Result(false, "Incorrect password. Please try again.");
        }
        if (user.getEmail().equals(email)) {
            return new Result(false, "The new email must be different from the current email.");
        }
        if (!email.matches("^[A-Za-z0-9]+(?:\\.[A-Za-z0-9]*)?@[a-z]+\\.com$")) {
            return new Result(false, "Incorrect email format.");
        }
        for (User u : App.user) {
            if (u.getEmail().equals(email)) {
                return new Result(false, "Email already exists.");
            }
        }
        for (Store u : App.store) {
            if (u.email().equals(email)) {
                return new Result(false, "Email already exists.");
            }
        }
        user.setEmail(email);
        return new Result(true, "Email updated successfully.");
    }

    public Result editPassword(String newPass, String oldPass) {
        User user = App.currentUser;
        if (!user.getPassword().equals(oldPass)) {
            return new Result(false, "Incorrect password. Please try again.");
        }
        if (oldPass.equals(newPass)) {
            return new Result(false, "The new password must be different from the old password.");
        }
        if (!newPass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]+$")) {
            return new Result(false, "The new password is weak.");
        }
        user.setPassword(newPass);
        user.setReEnteredPassword(newPass);
        return new Result(true, "Password updated successfully.");
    }

    public Result showMyInfo() {
        User user = App.currentUser;
        String info = "Name: " + user.getFirstName() + " " + user.getLastName() + "\nEmail: " + user.getEmail();
        return new Result(true, info);
    }

    public Result addAddress(String country, String city, String street, String postal) {

        if (!postal.matches("^\\d{10}$")) {
            return new Result(false, "Invalid postal code. It should be a 10-digit number.");
        }

        if (App.userDataManager.isPostalCodeExists(App.currentUser, postal)) {
            return new Result(false, "This postal code is already associated with an existing address.");
        }

        int id = App.userDataManager.getNextAddressId(App.currentUser);
        App.userDataManager.addAddress(App.currentUser, country, city, street, postal);

        return new Result(true, String.format("Address successfully added with id %d.", id));
    }

    public Result deleteAddress(String idStr) {
        int id;
        try {
            id = Integer.parseInt(idStr.trim());
        } catch (NumberFormatException e) {
            return new Result(false, "No address found.");
        }
        boolean deleted = App.userDataManager.deleteAddress(App.currentUser, id);
        if (deleted) {
            return new Result(true, String.format("Address with id %d deleted successfully.", id));
        } else {
            return new Result(false, "No address found.");
        }
    }

    public Result listMyAddresses() {
        String addresses = App.userDataManager.getAddressesString(App.currentUser);
        if (addresses.isEmpty()) {
            return new Result(false, "No addresses found. Please add an address first.");
        }
        return new Result(true, addresses);
    }

    public Result addCreditCard(String cardNumber, String expirationDate, String cvv, String initialValue) {
        if (!cardNumber.matches("^\\d{16}$")) {
            return new Result(false, "The card number must be exactly 16 digits.");
        }
        if (!expirationDate.matches("^(0[1-9]|1[0-2])\\/\\d{2}$")) {
            return new Result(false, "Invalid expiration date. Please enter a valid date in MM/YY format.");
        }
        if (!cvv.matches("^(\\d{4}|\\d{3})$")) {
            return new Result(false, "The CVV code must be 3 or 4 digits.");
        }
        double initialVal;
        try {
            initialVal = Double.parseDouble(initialValue);
        } catch (NumberFormatException e) {
            return new Result(false, "The initial value cannot be negative.");
        }
        if (initialVal < 0) {
            return new Result(false, "The initial value cannot be negative.");
        }
        if (App.userDataManager.creditCardExists(App.currentUser, cardNumber)) {
            return new Result(false, "This card is already saved in your account.");
        }
        int id = App.userDataManager.getNextCreditCardId(App.currentUser);
        App.userDataManager.addCreditCard(App.currentUser, cardNumber, expirationDate, cvv, initialVal);
        return new Result(true, String.format("Credit card with Id %d has been added successfully.", id));
    }

    public Result chargeCredit(String amountStr, String idStr) {
        double amount;
        int id;
        try {
            amount = Double.parseDouble(amountStr);
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid input.");
        }
        if (amount <= 0) {
            return new Result(false, "The amount must be greater than zero.");
        }
        double newBalance = App.userDataManager.chargeCredit(App.currentUser, id, amount);
        if (newBalance < 0) {
            return new Result(false, "No credit card found.");
        }
        return new Result(true, String.format("$%.1f has been added to the credit card %d. New balance: $%.1f.", amount, id, newBalance));
    }

    public Result checkCredit(String idStr) {
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            return new Result(false, "No credit card found.");
        }
        double balance = App.userDataManager.getCreditCardBalance(App.currentUser, id);
        if (balance < 0) {
            return new Result(false, "No credit card found.");
        }
        return new Result(true, String.format("Current balance : $%.1f", balance));
    }

    public Result showProductsInCart() {
        String cart = App.userDataManager.getCartDetails(App.currentUser);
        if (cart.isEmpty()) {
            return new Result(false, "Your shopping cart is empty.");
        }
        return new Result(true, cart);
    }

    public Result checkOut(String cardIdStr, String addressIdStr) {
        if (App.userDataManager.isCartEmpty(App.currentUser)) {
            return new Result(false, "Your shopping cart is empty.");
        }
        int cardId, addressId;
        try {
            cardId = Integer.parseInt(cardIdStr);
            addressId = Integer.parseInt(addressIdStr);
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid input.");
        }
        if (!App.userDataManager.addressExists(App.currentUser, addressId)) {
            return new Result(false, "The provided address ID is invalid.");
        }
        if (!App.userDataManager.creditCardExistsById(App.currentUser, cardId)) {
            return new Result(false, "The provided card ID is invalid.");
        }
        double totalAmount = App.userDataManager.getTotalCartAmount(App.currentUser);
        if (!App.userDataManager.hasSufficientBalance(App.currentUser, cardId, totalAmount)) {
            return new Result(false, "Insufficient balance in the selected card.");
        }


        var cartItems = new ArrayList<>(App.userDataManager.getCart(App.currentUser));


        int orderId = App.orderManager.placeOrder(App.currentUser, cardId, addressId);


        for (CartItem item : cartItems) {
            Product product = App.productDataManager.getProductById(item.getProductId());
            if (product != null) {
                product.increaseNumberOfSold(item.getQuantity());
            }
        }

        String shippingDetails = App.userDataManager.getAddressDetails(App.currentUser, addressId);
        return new Result(true, String.format("Order has been placed successfully!\nOrder ID: %d\nTotal Paid: $%.1f\nShipping to: %s", orderId, totalAmount, shippingDetails));
    }

    public Result removeFromCart(String productIdStr, String quantityStr) {
        if (App.userDataManager.isCartEmpty(App.currentUser)) {
            return new Result(false, "Your shopping cart is empty.");
        }

        int productId, quantity;
        try {
            productId = Integer.parseInt(productIdStr);
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid input.");
        }

        if (quantity <= 0) {
            return new Result(false, "Quantity must be a positive number.");
        }

        String productName = App.userDataManager.getProductNameFromCart(App.currentUser, productId);
        if (productName.isEmpty()) {
            return new Result(false, String.format("The product with ID %d is not in your cart.", productId));
        }

        int availableQuantity = App.userDataManager.getProductQuantityInCart(App.currentUser, productId);
        if (quantity > availableQuantity) {
            return new Result(false, String.format("You only have %d of \"%s\" in your cart.", availableQuantity, productName));
        }

        int result = App.userDataManager.removeFromCart(App.currentUser, productId, quantity);
        if (result == 1) {
            if (App.userDataManager.getProductQuantityInCart(App.currentUser, productId) == 0) {
                return new Result(true, String.format("\"%s\" has been completely removed from your cart.", productName));
            } else {
                return new Result(true, String.format("Successfully removed %d of \"%s\" from your cart.", quantity, productName));
            }
        }

        return new Result(false, "invalid command");
    }

    public Result goBack() {
        App.setCurrentMenu(Menu.MainMenu);
        return new Result(true, "Redirecting to the MainMenu ...");
    }
}
