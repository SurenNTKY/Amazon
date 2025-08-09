package models.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MainMenuCommands {
    GoToMenu("(\\s*)go(\\s+)to(\\s+)-m(\\s+)(?<menuName>\\S+)(\\s*)"),
    Exit("(\\s*)exit(\\s*)");

    private final String pattern;

    MainMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }
}

