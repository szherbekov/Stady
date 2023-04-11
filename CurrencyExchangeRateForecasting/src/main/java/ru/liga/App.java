package ru.liga;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

    public static void main(String[] args) {
        try {
            Bot bot = new Bot();
            bot.connectApi();
        } catch (Exception e) {
            log.debug("Ошибка в классе App: " + e.getMessage());
        }
    }
}


