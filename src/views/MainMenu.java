package views;

import controllers.MainMenuControllers;
import models.enums.MainMenuCommands;

import java.util.Scanner;
import java.util.regex.Matcher;

public class MainMenu implements AppMenu{

    private final MainMenuControllers controller = new MainMenuControllers();

    @Override
    public void check(Scanner scanner) {

        String input = scanner.nextLine().trim();
        Matcher matcher;
        if ((matcher = MainMenuCommands.GoToMenu.getMatcher(input)) != null) {
            System.out.println(controller.goToMenu(
                    matcher.group("menuName")
            ).toString());
        } else if ((matcher = MainMenuCommands.Exit.getMatcher(input)) != null) {
            System.out.println(controller.exit());
        } else {
            System.out.println("invalid command");
        }
    }
}
