package ru.liga.util;

import ru.liga.controller.Controller;
import ru.liga.controller.RateControllerImpl;
import ru.liga.controller.SystemControllerImpl;
import ru.liga.model.command.Command;
import ru.liga.repository.RatesRepository;

/**
 * Класс отвечающий за выбор контроллера, который будет работать с поступившей командой
 *
 * @see ru.liga.App
 */

public class ControllerFactory {

    public static Controller getController(Command command, RatesRepository repository) {

        return switch (command.getName()) {
            case HELP -> new SystemControllerImpl(command);
            case RATE -> new RateControllerImpl(command, repository);
        };
    }

    private ControllerFactory() {
    }
}
