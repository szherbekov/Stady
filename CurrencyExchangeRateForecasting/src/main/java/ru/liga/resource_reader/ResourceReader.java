package ru.liga.resource_reader;
import ru.liga.ExchangeCurrencyObj.ExchangeCurrencyObj;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ResourceReader {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final List<ExchangeCurrencyObj> listEUR = new ArrayList<>();
    public static final List<ExchangeCurrencyObj> listUSD = new ArrayList<>();
    public static final List<ExchangeCurrencyObj> listTRY = new ArrayList<>();

    public void readFile() {
        File fileEUR = new File("/home/szherbekov/IdeaProjects/CurrencyExchangeRateForecasting/src/main/resources/EUR.csv");
        File fileUSD = new File("/home/szherbekov/IdeaProjects/CurrencyExchangeRateForecasting/src/main/resources/USD.csv");
        File fileTRY = new File("/home/szherbekov/IdeaProjects/CurrencyExchangeRateForecasting/src/main/resources/TRY.csv");

        reading(fileEUR, listEUR);
        reading(fileUSD, listUSD);
        reading(fileTRY, listTRY);
    }

    public void reading(File file, List<ExchangeCurrencyObj> list) {
        try (FileReader fileReader = new FileReader((file)); Scanner scanner = new Scanner(fileReader)) {
            scanner.nextLine();
            while ((scanner.hasNext())) {
                String nextLine = scanner.nextLine();
                if (nextLine.isEmpty()) {
                    continue;
                }
                String[] strings = nextLine.split(";");
                ExchangeCurrencyObj obj = new ExchangeCurrencyObj();
                obj.setDate(LocalDate.parse(strings[1], formatter));
                obj.setRate(Float.parseFloat(strings[2]));
                list.add(obj);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        list.stream().sorted(Comparator.comparing(ExchangeCurrencyObj::getDate));
    }
}
