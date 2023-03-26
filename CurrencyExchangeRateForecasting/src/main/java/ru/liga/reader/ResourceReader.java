package ru.liga.reader;
import lombok.Data;
import ru.liga.enums.CurrencyFileName;
import ru.liga.enums.CurrencyType;
import ru.liga.model.ExchangeCurrencyObj;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Data
public class ResourceReader {
    private final DateTimeFormatter FORMATER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final List<ExchangeCurrencyObj> listEUR = readFile(CurrencyFileName.FILEEUR);
    private final List<ExchangeCurrencyObj> listUSD = readFile(CurrencyFileName.FILEUSD);
    private final List<ExchangeCurrencyObj> listTRY = readFile(CurrencyFileName.FILETRY);

    public List<ExchangeCurrencyObj> readFile(CurrencyFileName fileName) {
        List<ExchangeCurrencyObj> list = new ArrayList<>();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName.toString());
            Scanner scanner = new Scanner(Objects.requireNonNull(inputStream))) {
            scanner.nextLine();
            while ((scanner.hasNext())) {
                String nextLine = scanner.nextLine();
                if (nextLine.isEmpty()) continue;
                String[] strings = nextLine.split(";");
                ExchangeCurrencyObj obj = new ExchangeCurrencyObj();
                obj.setDate(LocalDate.parse(strings[1], FORMATER));
                obj.setRate(Double.parseDouble(strings[2]));
                list.add(obj);
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public List<ExchangeCurrencyObj> giveListByCurrencyType(CurrencyType type) {
        if (type.equals(CurrencyType.EUR)) return listEUR;
        if (type.equals(CurrencyType.USD)) return listUSD;
        if (type.equals(CurrencyType.TRY)) return listTRY;
        return Collections.emptyList();
    }
}
