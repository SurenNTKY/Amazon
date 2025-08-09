package models;

public class CreditCard {
    private final int id;
    private final String cardNumber;
    private final String expirationDate;
    private final String cvv;
    private double balance;

    public CreditCard(int id, String cardNumber, String expirationDate, String cvv, double initialValue) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.balance = initialValue;
    }

    public int getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getCvv() {
        return cvv;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
