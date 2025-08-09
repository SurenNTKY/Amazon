package models.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LoginMenuCommands {

    RegisterUser
            ("(\\s*)create(\\s+)a(\\s+)user(\\s+)account(\\s+)-fn(\\s+)(?<firstname>[\\S\\s]+)(\\s+)" +
                    "-ln(\\s+)(?<lastname>[\\S\\s]+)(\\s+)-p(\\s+)(?<password>[\\S\\s]+)(\\s+)" +
                    "-rp(\\s+)(?<repassword>[\\S\\s]+)(\\s+)-e(\\s+)(?<email>\\S+)(\\s*)"
            ),


    RegisterStore
            ("(\\s*)create(\\s+)a(\\s+)store(\\s+)account(\\s+)-b(\\s+)\"(?<brand>[\\S\\s]+)\"(\\s+)" +
            "-p(\\s+)(?<password>[\\S\\s]+)(\\s+)-rp(\\s+)(?<repassword>[\\S\\s]+)(\\s+)" +
            "-e(\\s+)(?<email>\\S+)(\\s*)"
            ),


    LoginUser
            ("(\\s*)login(\\s+)as(\\s+)user(\\s+)-e(\\s+)(?<email>[\\S\\s]+)(\\s+)" +
            "-p(\\s+)(?<password>\\S+)(\\s*)"
            ),

    LoginStore
            ("(\\s*)login(\\s+)as(\\s+)store(\\s+)-e(\\s+)(?<email>[\\S\\s]+)(\\s+)" +
            "-p(\\s+)(?<password>\\S+)(\\s*)"
            ),

    GoBack("(\\s*)go(\\s+)back(\\s*)"),

    DeleteAccount
            ("(\\s*)delete(\\s+)account(\\s+)-p(\\s+)(?<password>[\\S\\s]+)(\\s+)" +
            "-rp(\\s+)(?<repassword>\\S+)(\\s*)"
            ),

    Logout("(\\s*)logout(\\s*)");

    private final String pattern;

    LoginMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }
}
