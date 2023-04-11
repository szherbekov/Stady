package ru.liga.view;

import ru.liga.model.Answer;
import ru.liga.model.command.Command;

public interface View {
    void printMessage(Answer answer, Long chatId, Command command);

    void sendText(String answer, Long chatId);
}
