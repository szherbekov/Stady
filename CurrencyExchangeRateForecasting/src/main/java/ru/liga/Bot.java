package ru.liga;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.liga.controller.Controller;
import ru.liga.exception.BaseException;
import ru.liga.model.Answer;
import ru.liga.model.command.Command;
import ru.liga.repository.InMemoryRatesRepositoryImpl;
import ru.liga.repository.RatesRepository;
import ru.liga.util.CommandBuilder;
import ru.liga.util.CommandParser;
import ru.liga.util.ControllerFactory;
import ru.liga.view.TelegramViewImpl;
import ru.liga.view.View;

import java.io.IOException;
import java.util.Properties;

import static ru.liga.exception.ExceptionMessage.INTERNAL_ERROR;

@Slf4j
public class Bot extends TelegramLongPollingBot {
    private final RatesRepository repository = new InMemoryRatesRepositoryImpl();
    private final View view = new TelegramViewImpl(this);
    private final String BOT_NAME;
    private final String BOT_TOKEN;

    public Bot() {
        super();
        Properties prop = new Properties();
        try {
            prop.load(App.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException ex) {
            log.debug("Ошибка получения имени или токена бота " + ex.getMessage());
        }
        this.BOT_NAME = prop.getProperty("userName");
        this.BOT_TOKEN = prop.getProperty("token");
    }

    public void connectApi() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            log.info("Приложение запущено. Ожидаются сообщения");
        } catch (TelegramApiException e) {
            log.error("Приложению не удалось подключиться: " + e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Получен новый Update. updateID: " + update.getUpdateId());
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        try {
            Command command = new CommandBuilder().build(new CommandParser().parse(message.getText()));
            Controller controller = ControllerFactory.getController(command, repository);
            Answer answer = controller.operate();
            view.printMessage(answer, chatId, command);
        } catch (BaseException e) {
            log.debug("ERROR: " + e.getMessage());
            view.sendText(e.getMessage(), chatId);
        } catch (Exception ex) {
            log.debug("ERROR: " + ex.getMessage());
            view.sendText(INTERNAL_ERROR, chatId);
        }
    }


    public String getBotUsername() {
        return BOT_NAME;
    }

    public String getBotToken() {
        return BOT_TOKEN;
    }
}
