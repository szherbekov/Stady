package ru.liga.exception;

import lombok.Getter;

@Getter
public class ExceptionMessage {
    public static final String INVALID_COMMAND = "Неверный формат команды, воспользуйтесь командой Help";
    public static final String ILLEGAL_ALGORITHM = "Необходимо выбрать алгоритм предсказания, воспользуйтесь командой 'help'";
    public static final String ILLEGAL_OUTPUT = "При выборе формы вывода прогноза необходимо указать 'list' или 'graph'";
    public static final String ILLEGAL_PERIOD = "После команды период необходимо указать 'month' или 'week'";
    public static final String ILLEGAL_DATE = "Дата прогноза введена в неправильном формате";
    public static final String ILLEGAL_CURRENCY = "Название валюты указано в неправильном формате";
    public static final String ILLEGAL_LIST_OUTPUT = "Больше одного наименования валюты можно указывать только при выводе графика (-output graph)";
    public static final String ILLEGAL_GRAPH_OUTPUT = "Вывод данных в виде графика возможен только при предсказании на период, а не на конкретную дату";
    public static final String ILLEGAL_DATE_FROM_ACTUAL = "Введена слишком поздняя дата, последняя доступная для предсказания дата - ";
    public static final String REPEAT_COMMAND_PARAMETER = "Вы ввели один параметр больше одного раза. Параметр: ";
    public static final String INTERNAL_ERROR = "Произошла внутренняя ошибка приложения";
    public static final String CSV_PARSE_ERROR = "Ошибка загрузки данных из файлов CSV";
    public static final String PLOTTING_ERROR = "При построении графика произошла ошибка";

}
