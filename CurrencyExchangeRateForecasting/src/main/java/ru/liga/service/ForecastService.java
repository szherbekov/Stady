package ru.liga.service;

import ru.liga.model.Currency;
import ru.liga.model.Period;
import ru.liga.model.Rate;

import java.util.List;

public interface ForecastService {

    List<Rate> getRates(Currency currency, Period period);
}
