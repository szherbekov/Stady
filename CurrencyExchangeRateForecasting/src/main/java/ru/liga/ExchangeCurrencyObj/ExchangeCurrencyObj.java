package ru.liga.ExchangeCurrencyObj;

import lombok.Data;

import java.time.LocalDate;
@Data
public class ExchangeCurrencyObj {
    private LocalDate date;
    private float rate;
}
