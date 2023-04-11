package ru.liga.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.liga.controller.Controller;
import ru.liga.model.Answer;
import ru.liga.model.Currency;
import ru.liga.model.Rate;
import ru.liga.model.command.Command;
import ru.liga.repository.RatesRepository;
import ru.liga.util.CommandBuilder;
import ru.liga.util.CommandParser;
import ru.liga.util.ControllerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LinearRegressionForecastServiceImplTest {
    private static RatesRepository ratesRepository = mock(RatesRepository.class);

    @BeforeAll
    static void setRepositoryData() {
        List<Rate> rates = new ArrayList<>();
        for(int i = 29; i >= 0; i--) {
            rates.add(new Rate(LocalDate.now().minusDays(i), new BigDecimal(30-i), Currency.USD));
        }
        when(ratesRepository.getRates(Currency.USD, 30)).thenReturn(rates);
    }

    @Test
    void whenTargetIsTomorrowThenRateIs31() {
        String commandString = "rate USD -date tomorrow -alg lr";
        Command command = new CommandBuilder().build(new CommandParser().parse(commandString));
        Controller controller = ControllerFactory.getController(command, ratesRepository);
        Answer operate = controller.operate();
        assertThat(operate.getRatesMap().get(Currency.USD).get(0).getRate().doubleValue()).isEqualTo(31d);
    }
}