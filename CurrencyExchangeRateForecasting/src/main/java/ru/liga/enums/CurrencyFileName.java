package ru.liga.enums;

public enum CurrencyFileName {
    FILEEUR("EUR.csv"),
    FILEUSD("USD.csv"),
    FILETRY("TRY.csv");

    private final String fileName;

    CurrencyFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return this.fileName;
    }
}
