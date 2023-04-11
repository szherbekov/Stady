package ru.liga.model;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class Answer {
    private final String text;
    private final Map<Currency, List<Rate>> ratesMap;
    private final boolean outputGraph;


    public Answer(String text) {
        this.text = text;
        ratesMap = null;
        outputGraph = false;
    }

    public Answer(Map<Currency, List<Rate>> ratesMap, boolean output) {
        this.text = null;
        this.ratesMap = ratesMap;
        this.outputGraph = output;

    }

    public Answer(Map<Currency, List<Rate>> ratesMap) {
        this.text = null;
        this.ratesMap = ratesMap;
        this.outputGraph = false;
    }
}
