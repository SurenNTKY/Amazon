package models.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ProductMenuCommands {
    ShowProducts
            ("(\\s*)show(\\s+)products(\\s+)" +
            "-sortBy(\\s+)(?<sortBy>(rate|higher price to lower|lower price to higher|number of sold))(\\s*)"
    ),
    ShowNext10Products
            ("(\\s*)show(\\s+)next(\\s+)10(\\s+)products(\\s*)"
            ),
    ShowPast10Products
            ("(\\s*)show(\\s+)past(\\s+)10(\\s+)products(\\s*)"
            ),
    ShowInformation
            ("(\\s*)show(\\s+)information(\\s+)of(\\s+)-id(\\s+)(?<productId>-?\\d+)(\\s*)"
            ),
    RateProduct
            ("(\\s*)Rate(\\s+)product(\\s+)-r(\\s+)(?<rating>-?\\d+)(\\s+)" +
            "(-m(\\s+)\"(?<message>[\\S\\s]+)\")?(\\s*)-id(\\s+)(?<productId>-?\\d+)(\\s*)"
    ),
    AddToCart
            ("(\\s*)add(\\s+)to(\\s+)cart(\\s+)-product(\\s+)(?<productId>-?\\d+)(\\s+)" +
            "-quantity(\\s+)(?<quantity>-?\\d+)(\\s*)"
    ),

    GoBack("(\\s*)go(\\s+)back(\\s*)");

    private final String pattern;

    ProductMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }
}

