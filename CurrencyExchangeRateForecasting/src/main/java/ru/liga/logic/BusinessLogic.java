package ru.liga.logic;
import org.jetbrains.annotations.NotNull;
import ru.liga.ExchangeCurrencyObj.ExchangeCurrencyObj;
import ru.liga.enums.CurrencyType;
import ru.liga.resource_reader.ResourceReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.System.*;

public class BusinessLogic {
    LocalDate today = LocalDate.now();
    Locale locale = new Locale("ru", "RU");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", locale);

    public void getTomorrowRate(@NotNull CurrencyType currencyType) {
        if (currencyType.equals(CurrencyType.EUR)) {
            getTomorrowRateByCurrencyType(ResourceReader.listEUR, today);
        } else if (currencyType.equals(CurrencyType.USD)) {
            getTomorrowRateByCurrencyType(ResourceReader.listUSD, today);
        } else if (currencyType.equals(CurrencyType.TRY)) {
            getTomorrowRateByCurrencyType(ResourceReader.listTRY, today);
        }
    }

    public ExchangeCurrencyObj getTomorrowRateByCurrencyType(List<ExchangeCurrencyObj> list, LocalDate today) {
        float rate7Days = 0.0f;
        for (int i = 0; i < 7; i++) {
            rate7Days += list.get(i).getRate();
        }
        ExchangeCurrencyObj obj = new ExchangeCurrencyObj();
        obj.setDate(today.plusDays(1));
        obj.setRate(rate7Days / 7);
        printRate(obj);
        return obj;
    }

    public void getWeekRate(@NotNull CurrencyType currencyType) {
        if (currencyType.equals(CurrencyType.EUR)) {
            getWeekRateByCurrencyType(ResourceReader.listEUR, today);
        } else if (currencyType.equals(CurrencyType.USD)) {
            getWeekRateByCurrencyType(ResourceReader.listUSD, today);
        } else if (currencyType.equals(CurrencyType.TRY)) {
            getWeekRateByCurrencyType(ResourceReader.listTRY, today);
        }
    }

    public void getWeekRateByCurrencyType(List<ExchangeCurrencyObj> list, LocalDate today) {
        List<ExchangeCurrencyObj> outputListObjects = new ArrayList<>();
        for (int j = 0; j < 7; j++) {
            outputListObjects.add(list.get(j));
        }
        int i = 7;
        int g = 0;
        while (i > 0) {
            i--;
            ExchangeCurrencyObj obj = getTomorrowRateByCurrencyType(outputListObjects, today.plusDays(g));
            outputListObjects.remove(i);
            outputListObjects.add(0, obj);
            g++;
        }
    }

    public void printRate(ExchangeCurrencyObj obj) {
        float count = 100.0f;
        out.println(obj.getDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, locale)
                + " " + obj.getDate().format(formatter) + " - " + Math.round(obj.getRate() * count) / count);
    }
}
