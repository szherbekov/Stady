package ru.liga.util;

import ru.liga.model.Currency;
import ru.liga.model.Rate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * Класс для парсинга CSV файлов и маппинга этих данных в модель
 */
public class ParseRateCsv {
    private static final String NOMINAL = "nominal";
    private static final String DATA = "data";
    private static final String CURS = "curs";
    private static final String CDX = "cdx";
    private static final String DELIMITER = ";";

    public static List<Rate> readCsvParseLinesToRatesList(String filePath) throws IOException, ParseException {
        //Загружаем строки из файла
        List<String> fileLines;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ParseRateCsv.class.getResourceAsStream(filePath))))) {
            fileLines = reader.lines().toList();
        }
        LocalDate lastDate = null;

        List<String> headlines = Arrays.stream(fileLines.get(0).split(DELIMITER)).toList();
        int nominal = headlines.indexOf(NOMINAL);
        int data = headlines.indexOf(DATA);
        int curs = headlines.indexOf(CURS);
        int cdx = headlines.indexOf(CDX);
        List<Rate> rateList = new ArrayList<>();
        int FIRST_LINE_WITH_VALUES = 1;
        for (int i = FIRST_LINE_WITH_VALUES; i < fileLines.size(); i++) {
            String fileLine = fileLines.get(i);
            String[] splitText = fileLine.split(DELIMITER);
            List<String> columnList = new ArrayList<>(Arrays.asList(splitText));

            int currentNominal = Integer.parseInt(columnList.get(nominal).replace(".", ""));
            LocalDate currentDate = LocalDate.parse(columnList.get(data), DateTimeConstants.PARSE_DATE_FORMATTER_FROM_CSV);
            String stringRate = columnList.get(curs).replace("\"", "");
            BigDecimal currentRate = BigDecimal.valueOf(NumberFormat.getInstance().parse(stringRate).doubleValue());

            if (currentNominal != 1) {
                currentRate = currentRate.divide(BigDecimal.valueOf(currentNominal), RoundingMode.FLOOR);
            }
            Currency currency = getCurrency(columnList.get(cdx));

            while (lastDate != null && !currentDate.equals(lastDate.minusDays(1))) {
                lastDate = lastDate.minusDays(1);
                Rate rate = new Rate(lastDate, currentRate, currency);
                rateList.add(rate);
            }
            rateList.add(new Rate(currentDate, currentRate, currency));
            lastDate = currentDate;
        }
        return rateList;
    }

    private static Currency getCurrency(String currencyTitle) {
        return switch (currencyTitle) {
            case "Доллар США" -> Currency.USD;
            case "Евро" -> Currency.EUR;
            case "Турецкая лира" -> Currency.TRY;
            case "Армянский драм" -> Currency.AMD;
            case "Болгарский лев" -> Currency.BGN;
            default -> null;
        };
    }
}
