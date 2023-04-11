package ru.liga.repository;

import ru.liga.model.Currency;
import ru.liga.model.Rate;

import java.util.List;


public interface RatesRepository {
    List<Rate> getAllRates(Currency currency);

    List<Rate> getRates(Currency currency, int numberOfDays);


}

