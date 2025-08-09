package models;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDataManager {

    private final HashMap<String, ArrayList<Address>> addresses = new HashMap<>();
    private final HashMap<String, ArrayList<CreditCard>> creditCards = new HashMap<>();
    private final HashMap<String, ArrayList<CartItem>> carts = new HashMap<>();

    public ArrayList<Address> getAddresses(User user) {
        return addresses.getOrDefault(user.getEmail(), new ArrayList<>());
    }

    public int getNextAddressId(User user) {
        ArrayList<Address> userAddresses = addresses.get(user.getEmail());
        return (userAddresses == null) ? 1 : userAddresses.size() + 1;
    }

    public boolean isPostalCodeExists(User user, String postal) {
        for (Address addr : getAddresses(user)) {
            if (addr.postal().equals(postal)) {
                return true;
            }
        }
        return false;
    }

    public void addAddress(User user, String country, String city, String street, String postal) {
        String key = user.getEmail();
        ArrayList<Address> userAddresses = addresses.getOrDefault(key, new ArrayList<>());
        int id = getNextAddressId(user);
        userAddresses.add(new Address(id, country, city, street, postal));
        addresses.put(key, userAddresses);
    }

    public boolean deleteAddress(User user, int id) {
        String key = user.getEmail();
        ArrayList<Address> userAddresses = addresses.get(key);
        if (userAddresses == null) return false;
        for (int i = 0; i < userAddresses.size(); i++) {
            if (userAddresses.get(i).id() == id) {
                userAddresses.remove(i);
                return true;
            }
        }
        return false;
    }

    public String getAddressesString(User user) {
        String key = user.getEmail();
        ArrayList<Address> userAddresses = addresses.get(key);
        if (userAddresses == null || userAddresses.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("Saved Addresses\n━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
        for (int i = 0; i < userAddresses.size(); i++) {
            Address addr = userAddresses.get(i);
            sb.append(String.format("Address %d:\n%s\n\n━━━━━━━━━━━━━━━━━━━━━━━━━━", addr.id(), addr));
            if (i < userAddresses.size() - 1) {
                sb.append("\n\n");
            }
        }
        return sb.toString();
    }

    public boolean addressExists(User user, int id) {
        for (Address addr : getAddresses(user)) {
            if (addr.id() == id) return true;
        }
        return false;
    }

    public String getAddressDetails(User user, int id) {
        for (Address addr : getAddresses(user)) {
            if (addr.id() == id) {
                return String.format("%s, %s, %s", addr.street(), addr.city(), addr.country());
            }
        }
        return "";
    }

    public ArrayList<CreditCard> getCreditCards(User user) {
        return creditCards.getOrDefault(user.getEmail(), new ArrayList<>());
    }

    public int getNextCreditCardId(User user) {
        ArrayList<CreditCard> cards = creditCards.get(user.getEmail());
        return (cards == null) ? 1 : cards.size() + 1;
    }

    public boolean creditCardExists(User user, String cardNumber) {
        for (CreditCard card : getCreditCards(user)) {
            if (card.getCardNumber().equals(cardNumber)) return true;
        }
        return false;
    }

    public void addCreditCard(User user, String cardNumber, String expirationDate, String cvv, double initialValue) {
        String key = user.getEmail();
        ArrayList<CreditCard> cards = creditCards.getOrDefault(key, new ArrayList<>());
        int id = getNextCreditCardId(user);
        cards.add(new CreditCard(id, cardNumber, expirationDate, cvv, initialValue));
        creditCards.put(key, cards);
    }

    public boolean creditCardExistsById(User user, int cardId) {
        for (CreditCard card : getCreditCards(user)) {
            if (card.getId() == cardId) return true;
        }
        return false;
    }

    public boolean hasSufficientBalance(User user, int cardId, double amount) {
        for (CreditCard card : getCreditCards(user)) {
            if (card.getId() == cardId) {
                return card.getBalance() >= amount;
            }
        }
        return false;
    }

    public double chargeCredit(User user, int cardId, double amount) {
        for (CreditCard card : getCreditCards(user)) {
            if (card.getId() == cardId) {
                card.setBalance(card.getBalance() + amount);
                return card.getBalance();
            }
        }
        return -1;
    }

    public double getCreditCardBalance(User user, int cardId) {
        for (CreditCard card : getCreditCards(user)) {
            if (card.getId() == cardId) {
                return card.getBalance();
            }
        }
        return -1;
    }

    public ArrayList<CartItem> getCart(User user) {
        return carts.getOrDefault(user.getEmail(), new ArrayList<>());
    }

    public void addToCart(User user, CartItem item) {
        String key = user.getEmail();
        ArrayList<CartItem> cart = carts.getOrDefault(key, new ArrayList<>());
        boolean found = false;
        for (CartItem existingItem : cart) {
            if (existingItem.getProductId() == item.getProductId()) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                found = true;
                break;
            }
        }
        if (!found) {
            cart.add(item);
        }
        carts.put(key, cart);
    }

    public void clearCart(User user) {
        carts.put(user.getEmail(), new ArrayList<>());
    }

    public boolean isCartEmpty(User user) {
        return getCart(user).isEmpty();
    }

    public String getCartDetails(User user) {
        ArrayList<CartItem> cart = getCart(user);
        if (cart.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("Your Shopping Cart:\n------------------------------------\n");
        cart.sort((a, b) -> a.getProductName().compareToIgnoreCase(b.getProductName()));
        for (int i = 0; i < cart.size(); i++) {
            CartItem item = cart.get(i);
            double unitPriceRounded = Math.round(item.getPrice() * 10) / 10.0;
            double totalPriceRounded = Math.round(item.getPrice() * item.getQuantity() * 10) / 10.0;
            sb.append(String.format("Product ID  : %d\n", item.getProductId()));
            sb.append(String.format("Name        : %s\n", item.getProductName()));
            sb.append(String.format("Quantity    : %d\n", item.getQuantity()));
            sb.append(String.format("Price       : $%.1f (each)\n", unitPriceRounded));
            sb.append(String.format("Total Price : $%.1f\n", totalPriceRounded));
            sb.append(String.format("Brand       : %s\n", item.getBrand()));
            sb.append(String.format("Rating      : %.1f/5\n", item.getRating()));
            sb.append("------------------------------------");
            if (i < cart.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public int removeFromCart(User user, int productId, int quantity) {
        ArrayList<CartItem> cart = getCart(user);
        for (CartItem item : cart) {
            if (item.getProductId() == productId) {
                if (quantity > item.getQuantity()) {
                    return -1;
                } else {
                    item.setQuantity(item.getQuantity() - quantity);
                    if (item.getQuantity() == 0) {
                        cart.remove(item);
                    }
                    return 1;
                }
            }
        }
        return 0;
    }

    public int getProductQuantityInCart(User user, int productId) {
        for (CartItem item : getCart(user)) {
            if (item.getProductId() == productId) {
                return item.getQuantity();
            }
        }
        return 0;
    }

    public String getProductNameFromCart(User user, int productId) {
        for (CartItem item : getCart(user)) {
            if (item.getProductId() == productId) {
                return item.getProductName();
            }
        }
        return "";
    }

    public double getTotalCartAmount(User user) {
        double total = 0;
        for (CartItem item : getCart(user)) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }
}
