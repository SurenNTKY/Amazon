package models.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum StoreMenuCommands {

    AddProduct
            ("(\\s*)add(\\s+)product(\\s+)-n(\\s+)\"(?<name>[\\S\\s]+)\"(\\s+)" +
            "-pc(\\s+)(?<producerCost>-?\\d+(\\.\\d+)?)(\\s+)-p(\\s+)(?<price>-?\\d+(\\.\\d+)?)(\\s+)" +
            "-about(\\s+)\"(?<about>[\\S\\s]+)\"(\\s+)-np(\\s+)(?<np>-?\\d+)(\\s*)"
    ),

    ApplyDiscount
            ("(\\s*)apply(\\s+)discount(\\s+)-p(\\s+)(?<productId>-?\\d+)(\\s+)" +
            "-d(\\s+)(?<discount>-?\\d+)(\\s+)-q(\\s+)(?<quantity>-?\\d+)(\\s*)"
            ),

    ShowProfit("(\\s*)show(\\s+)profit(\\s*)"),
    ShowListOfProducts("(\\s*)show(\\s+)list(\\s+)of(\\s+)products(\\s*)"),
    AddStock
            ("(\\s*)add(\\s+)stock(\\s+)-product(\\s+)(?<productId>-?\\d+)(\\s+)" +
            "-amount(\\s+)(?<amount>-?\\d+)(\\s*)"
    ),
    UpdatePrice
            ("(\\s*)update(\\s+)price(\\s+)-product(\\s+)(?<productId>-?\\d+)(\\s+)" +
            "-price(\\s+)(?<newPrice>-?\\d+(\\.\\d+)?)(\\s*)"
    ),

    GoBack("(\\s*)go(\\s+)back(\\s*)");

    private final String pattern;

    StoreMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }
}
