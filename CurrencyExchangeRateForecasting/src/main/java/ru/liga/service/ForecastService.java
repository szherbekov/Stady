package ru.liga.service;
import ru.liga.controller.CommandController;
import ru.liga.enums.CurrencyType;
import ru.liga.model.ExchangeCurrencyObj;
import ru.liga.reader.ResourceReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ForecastService {
    ResourceReader resourceReader = new ResourceReader();

    public void getTomorrowRate(CurrencyType currencyType) {
        LocalDate today = LocalDate.now();
        if (currencyType.equals(CurrencyType.EUR)) {
            getTomorrowRateByCurrencyType(resourceReader.giveListByCurrencyType(CurrencyType.EUR), today);
        } else if (currencyType.equals(CurrencyType.USD)) {
            getTomorrowRateByCurrencyType(resourceReader.giveListByCurrencyType(CurrencyType.USD), today);
        } else if (currencyType.equals(CurrencyType.TRY)) {
            getTomorrowRateByCurrencyType(resourceReader.giveListByCurrencyType(CurrencyType.TRY), today);
        }
    }

    public ExchangeCurrencyObj getTomorrowRateByCurrencyType(List<ExchangeCurrencyObj> list, LocalDate today) {
        double rate7Days = 0.0;
        for (int i = 0; i < 7; i++) {
            rate7Days += list.get(i).getRate();
        }
        ExchangeCurrencyObj obj = new ExchangeCurrencyObj();
        obj.setDate(today.plusDays(1));
        obj.setRate(rate7Days / 7);
        CommandController.printRate(obj);
        return obj;
    }

    public void getWeekRate(CurrencyType currencyType) {
        LocalDate today = LocalDate.now();
        if (currencyType.equals(CurrencyType.EUR)) {
            getWeekRateByCurrencyType(resourceReader.giveListByCurrencyType(CurrencyType.EUR), today);
        } else if (currencyType.equals(CurrencyType.USD)) {
            getWeekRateByCurrencyType(resourceReader.giveListByCurrencyType(CurrencyType.USD), today);
        } else if (currencyType.equals(CurrencyType.TRY)) {
            getWeekRateByCurrencyType(resourceReader.giveListByCurrencyType(CurrencyType.TRY), today);
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
}
