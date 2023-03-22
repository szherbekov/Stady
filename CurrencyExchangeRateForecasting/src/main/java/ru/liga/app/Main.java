package ru.liga.app;
import ru.liga.consol_controller.CommandController;
import ru.liga.resource_reader.ResourceReader;

public class Main {

    public static void main(String[] args) {
        ResourceReader resourceReader = new ResourceReader();
        CommandController commandController = new CommandController();
        System.out.println("Application Currency Exchange Rate Forecasting is start");
        resourceReader.readFile();
        commandController.readCommand();

    }
}
