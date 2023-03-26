package ru.liga.model;
import lombok.Data;
import java.time.LocalDate;
@Data
public class ExchangeCurrencyObj {
    private LocalDate date;
    private double rate;
}
