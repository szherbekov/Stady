package ru.liga.controller;
import ru.liga.enums.CurrencyType;
import ru.liga.model.ExchangeCurrencyObj;
import ru.liga.service.ForecastService;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class CommandController {
    ForecastService forecastService = new ForecastService();
    private static final Locale locale = new Locale("ru", "RU");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", locale);


    public void readCommand() {
        boolean readCommand = true;
        while (readCommand) {
            out.println("Enter your command");
            out.println("Command example: rate USD(EUR,TRY) tomorrow(week), or use command - Exit for exit application");
            Scanner scanner = new Scanner(in);
            String command = scanner.nextLine();
            if (command.contains("tomorrow") && command.contains("rate")) {
                forecastService.getTomorrowRate(getCurrencyType(command));
            } else if (command.contains("week") && command.contains("rate")) {
                forecastService.getWeekRate(getCurrencyType(command));
            } else if (command.equals("exit")) {
                readCommand = false;
                out.println("Exit from Application");
            } else {
                out.println("Your enter unknown command, please repeat command");
            }
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

    public static void printRate(ExchangeCurrencyObj obj) {
        double count = 100.0;
        out.println(obj.getDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, locale)
                + " " + obj.getDate().format(formatter) + " - " + Math.round(obj.getRate() * count) / count);
    }
}
