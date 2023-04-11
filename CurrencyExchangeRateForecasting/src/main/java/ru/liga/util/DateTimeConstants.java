package ru.liga.util;

import java.time.format.DateTimeFormatter;


/**
 * Класс содержащий инструменты для работы с парсингом и форматом времени и даты
 */
public class DateTimeConstants {
    public static final DateTimeFormatter PARSE_DATE_FORMATTER_FROM_CSV = DateTimeFormatter.ofPattern("d.MM.yyyy");
    public static final DateTimeFormatter PRINT_DATE_FORMATTER_TO_VIEW = DateTimeFormatter.ofPattern("E dd.MM.yyyy");
}
