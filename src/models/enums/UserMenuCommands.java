package models.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum UserMenuCommands {

    ListMyOrders
            ("(\\s*)list(\\s+)my(\\s+)orders(\\s*)"),
    ShowOrderDetails
            ("(\\s*)show(\\s+)order(\\s+)details(\\s+)-id(\\s+)(?<orderId>-?\\d+)(\\s*)"),
    EditName
            ("(\\s*)edit(\\s+)name(\\s+)-fn(\\s+)(?<firstName>[\\S\\s]+)(\\s+)" +
                    "-ln(\\s+)(?<lastName>[\\S\\s]+)(\\s+)-p(\\s+)(?<password>[\\S\\s]+)(\\s*)"
            ),
    EditEmail
            ("(\\s*)edit(\\s+)email(\\s+)-e(\\s+)(?<email>[\\S\\s]+)(\\s+)" +
                    "-p(\\s+)(?<password>[\\S\\s]+)(\\s*)"
            ),
    EditPassword
            ("(\\s*)edit(\\s+)password(\\s+)-np(\\s+)(?<newPassword>[\\S\\s]+)(\\s+)" +
            "-op(\\s+)(?<oldPassword>[\\S\\s]+)(\\s*)"
            ),
    ShowMyInfo("(\\s*)show(\\s+)my(\\s+)info(\\s*)"),

    AddAddress("(\\s*)add(\\s+)address(\\s+)-country(\\s+)(?<country>[\\S\\s]+)(\\s+)" +
            "-city(\\s+)(?<city>[\\S\\s]+)(\\s+)" +
            "-street(\\s+)(?<street>[\\S\\s]+)(\\s+)-postal(\\s+)(?<postal>\\S+)(\\s*)"
    ),
    DeleteAddress
            ("(\\s*)delete(\\s+)address(\\s+)-id(\\s+)(?<addressId>-?\\d+)(\\s*)"),
    ListMyAddresses
            ("(\\s*)list(\\s+)my(\\s+)addresses(\\s*)"),
    AddCreditCard
            ("(\\s*)add(\\s+)a(\\s+)credit(\\s+)card(\\s+)-number(\\s+)(?<cardNumber>[\\S\\s]+)(\\s+)" +
            "-ed(\\s+)(?<expirationDate>[\\S\\s]+)(\\s+)-cvv(\\s+)(?<cvv>[\\S\\s]+)(\\s+)" +
            "-initialValue(\\s+)(?<initialValue>-?\\d+(\\.\\d+)?)(\\s*)"
            ),
    ChargeCredit
            ("(\\s*)Charge(\\s+)credit(\\s+)card(\\s+)" +
            "-a(\\s+)(?<amount>-?\\d+(\\.\\d+)?)(\\s+)-id(\\s+)(?<cardId>-?\\d+)(\\s*)"
            ),
    CheckCredit
            ("(\\s*)Check(\\s+)credit(\\s+)card(\\s+)balance(\\s+)-id(\\s+)(?<cardId>-?\\d+)(\\s*)"),
    ShowProductInCart
            ("(\\s*)show(\\s+)products(\\s+)in(\\s+)cart(\\s*)"),
    CheckOut
            ("(\\s*)checkout(\\s+)-card(\\s+)(?<cardId>-?\\d+)(\\s+)-address(\\s+)(?<addressId>-?\\d+)(\\s*)"),
    RemoveFromCart
            ("(\\s*)remove(\\s+)from(\\s+)cart(\\s+)-product(\\s+)(?<productId>-?\\d+)(\\s+)" +
            "-quantity(\\s+)(?<quantity>-?\\d+)(\\s*)"
    ),
    GoBack("(\\s*)go(\\s+)back(\\s*)");

    private final String pattern;

    UserMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }
}
