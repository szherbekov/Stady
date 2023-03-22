package ru.liga.consol_controller;
import ru.liga.enums.CurrencyType;
import ru.liga.logic.BusinessLogic;
import java.util.Scanner;

import static java.lang.System.*;

public class CommandController {
    BusinessLogic businessLogic = new BusinessLogic();
    public void readCommand() {
        out.println("Enter your command");
        out.println("Command example: rate USD(EUR,TRY) tomorrow(week), or use command - Exit for exit application");
        Scanner scanner = new Scanner(in);
        String command = scanner.nextLine();
        if (command.contains("tomorrow")) {
            businessLogic.getTomorrowRate(getCurrencyType(command));
            readCommand();
        } else if (command.contains("week")) {
            businessLogic.getWeekRate(getCurrencyType(command));
            readCommand();
        }else if (command.equals("exit")){
            out.println("Exit from Application");
        }else {
            out.println("Your enter unknown command, please repeat command");
            readCommand();
        }
    }

    public CurrencyType getCurrencyType(String command) {
        if (command.contains("EUR")) {
            return CurrencyType.EUR;
        } else if (command.contains("USD")) {
            return CurrencyType.USD;
        } else if (command.contains("TRY")) {
            return CurrencyType.TRY;
        }
        return null;
    }
}
