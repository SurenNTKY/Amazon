package views;

import models.App;
import models.enums.Menu;
import java.util.Scanner;

public class AppViews {

    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (App.getCurrentMenu() != Menu.ExitMenu) {
                App.getCurrentMenu().checkCommand(scanner);
            }
        }
    }
}
