package ru.liga.service;

import com.github.sh0nk.matplotlib4j.NumpyUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.liga.model.Currency;
import ru.liga.model.Period;
import ru.liga.model.Rate;
import ru.liga.repository.RatesRepository;
import ru.liga.util.LinearRegression;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Класс отвечающий за работу алгоритма - "Из интернета" (линейная регрессия)
 */
@RequiredArgsConstructor
public class LinearRegressionForecastServiceImpl implements ForecastService {
    @NonNull
    private final RatesRepository repository;
    private final int MONTH = 30;

    @Override
    public List<Rate> getRates(Currency currency, Period period) {
        List<Rate> rates = repository.getRates(currency, MONTH);

        LinearRegression linearRegression = getLinearRegressionFromMonth(rates);

        LocalDate lastDate = rates.get(rates.size() - 1).getDate();
        int dateCounter = rates.size();
        while (!Objects.equals(lastDate, LocalDate.now())) {
            lastDate = lastDate.plusDays(1);
            dateCounter++;
        }
        List<Rate> result = new ArrayList<>();
        while (!lastDate.equals(period.getDate())) {
            lastDate = lastDate.plusDays(1);
            dateCounter++;
            result.add(new Rate(lastDate, BigDecimal.valueOf(linearRegression.predict(dateCounter)), currency));
        }
        if (period.isPeriod()) {
            return result;
        } else
            return result.stream()
                    .sorted(Comparator.comparing(Rate::getDate).reversed()).limit(1)
                    .collect(toList());
    }


    private LinearRegression getLinearRegressionFromMonth(List<Rate> rates) {
        List<Double> days = NumpyUtils.linspace(1, 30, 30);
        return new LinearRegression(days, rates.stream()
                .map(r -> r.getRate().doubleValue())
                .collect(toList()));
    }

}
