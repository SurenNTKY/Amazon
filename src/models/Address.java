package models;

public record Address(int id, String country, String city, String street, String postal) {

    @Override
    public String toString() {
        return String.format("Postal Code: %s\nCountry: %s\nCity: %s\nStreet: %s", postal, country, city, street);
    }
}
