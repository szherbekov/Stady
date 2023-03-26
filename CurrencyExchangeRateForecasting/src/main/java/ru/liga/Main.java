package ru.liga;

import ru.liga.controller.CommandController;

public class Main {

    public static void main(String[] args) {
        CommandController commandController = new CommandController();
        System.out.println("Application Currency Exchange Rate Forecasting is start");
        commandController.readCommand();


    }
}
