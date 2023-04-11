package ru.liga.service;

import lombok.AllArgsConstructor;
import ru.liga.exception.ExceptionMessage;
import ru.liga.exception.IllegalDateException;
import ru.liga.model.Currency;
import ru.liga.model.Period;
import ru.liga.model.Rate;
import ru.liga.repository.RatesRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * Класс отвечающий за работу алгоритма - "Старый"
 */
@AllArgsConstructor
public class OldAlgorithmForecastServiceImpl implements ForecastService {
    private final RatesRepository repository;
    private final int ONE_YEARS = 365;
    private final int SEVEN_OLD_CURSES = 7;

    @Override
    public List<Rate> getRates(Currency currency, Period period) {

        List<Rate> rates = repository.getRates(currency, ONE_YEARS);
        if (period.getDate().isAfter(rates.get(rates.size() - 1).getDate().plusYears(2))) {
            throw new IllegalDateException(ExceptionMessage.ILLEGAL_DATE_FROM_ACTUAL + rates.get(rates.size() - 1).getDate());
        }
        List<Rate> resultRates = new ArrayList<>();
        if (period.isPeriod()) {
            LocalDate lastDate = LocalDate.now();
            while (!lastDate.equals(period.getDate())) {
                resultRates.add(getPeriodRate(rates, lastDate.plusDays(1), currency));
                lastDate = lastDate.plusDays(1);
            }
        } else {
            resultRates.add(getDateRate(rates, period.getDate(), currency));
        }
        return resultRates;
    }

    private Rate getDateRate(List<Rate> rates, LocalDate date, Currency currency) {

        BigDecimal rate = BigDecimal.ZERO;
        for (int i = 0; i < SEVEN_OLD_CURSES; i++) {
            rate = rate.add(rates.get(rates.size() - 1 - i).getRate());
        }
        Rate dateRate = new Rate();
        dateRate.setDate(date);
        dateRate.setRate(rate.divide(BigDecimal.valueOf(SEVEN_OLD_CURSES), RoundingMode.CEILING));
        dateRate.setCurrency(currency);
        return dateRate;
    }

    private Rate getPeriodRate(List<Rate> rates, LocalDate date, Currency currency) {
        int days = java.time.Period.between(LocalDate.now(), date.plusDays(30)).getDays();
        BigDecimal rate = BigDecimal.ZERO;
        for (int i = 0; i < days; i++) {
            rate = rate.add(rates.get(rates.size() - 1 - i).getRate());
        }
        Rate periodRate = new Rate();
        periodRate.setDate(date);
        periodRate.setRate(rate.divide(BigDecimal.valueOf(days), RoundingMode.CEILING));
        periodRate.setCurrency(currency);
        return periodRate;
    }
}
