package ru.liga.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Класс описывоющий модель объекта Rate, хранящего информацию о курсе валюты в определенную дату
 */


@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Rate {
    private int nominal;
    private LocalDate date;
    private BigDecimal rate;
    private Currency currency;

    public Rate(LocalDate date, BigDecimal rate, Currency currency) {
        this.date = date;
        this.rate = rate;
        this.currency = currency;
        this.nominal = 1;
    }
}

