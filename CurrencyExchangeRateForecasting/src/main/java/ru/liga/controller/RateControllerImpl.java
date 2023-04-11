package ru.liga.controller;

import lombok.AllArgsConstructor;
import ru.liga.model.*;
import ru.liga.model.command.Command;
import ru.liga.repository.RatesRepository;
import ru.liga.service.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс контроллера обрабатывающего запросы о прогнозе курсов валют
 */

@AllArgsConstructor
public class RateControllerImpl implements Controller {
    private final Command command;
    private final RatesRepository repository;


    @Override
    public Answer operate() {
        ForecastService service = getService(command.getAlgorithm());
        boolean output = command.getOutput() == Output.GRAPH;
        return new Answer(getRatesFromPeriod(service, command.getPeriod()), output);
    }

    private ForecastService getService(Algorithm algorithm) {
        if (algorithm == Algorithm.LR) {
            return new LinearRegressionForecastServiceImpl(repository);
        } else if (algorithm == Algorithm.LAST) {
            return new LastYearAlgorithmForecastServiceImpl(repository);
        } else if (algorithm == Algorithm.OLD) {
            return new OldAlgorithmForecastServiceImpl(repository);
        } else {
            return new MysticAlgorithmForecastServiceImpl(repository);
        }
    }

    private Map<Currency, List<Rate>> getRatesFromPeriod(ForecastService service, Period period) {
        Map<Currency, List<Rate>> ratesMap = new HashMap<>();
        for (Currency currency : command.getCurrency()) {
            ratesMap.put(currency, service.getRates(currency, period));
        }
        return ratesMap;
    }


}
