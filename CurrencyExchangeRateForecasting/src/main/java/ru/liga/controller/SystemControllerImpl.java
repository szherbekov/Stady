package ru.liga.controller;

import lombok.AllArgsConstructor;
import ru.liga.model.Answer;
import ru.liga.model.command.Command;

/**
 * Класс контроллера обрабатывающий команды системного характера
 */
@AllArgsConstructor
public class SystemControllerImpl implements Controller {
    private final Command command;

    @Override
    public Answer operate() {
        return (switch (command.getName()) {
            case HELP -> new Answer("""                
                    Список доступных команд:
                        help     - помощь
                        rate     - прогноз курсов валют, с этой командой необходимо указывать валюту (USD - доллар США, EUR - евро, TRY - турецкая лира,
                                       AMD армянский драм, BGN - Болгарский лев),
                                       период прогноза или дату, а также необходимо указывать алгоритм прогнозирования. 
                                       Периодом может быть неделя или месяц (week или month). Необходимо использовать команды: "-date или -period". 
                                       Варианты выбора алгоритма (-alg) - "Старый", "Прошлогодний", "Мистический", "Из интернета".
                                       Этим алгоритмам соответствуют следующие команды "old, last, mist, lr". 
                                       Данные курсов могут выводиться в виде списка и графика (-output list, -output graph).
                                       Параметры команды необходимо указывать через пробел (пример: 
                                       1) rate TRY -date tomorrow -alg mist. 2) rate USD -date 20.06.2023 -alg old.
                                       3) rate EUR -period week -alg mist -output list. 4) rate TRY, USD - period month -alg lr -output graph)
                        
                        """);
            default -> throw new IllegalStateException("Unexpected value: " + command.getName());
        });
    }


}
