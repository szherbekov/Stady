package ru.liga.view;

import com.github.sh0nk.matplotlib4j.NumpyUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.Bot;
import ru.liga.exception.PlottingException;
import ru.liga.model.Answer;
import ru.liga.model.Currency;
import ru.liga.model.Rate;
import ru.liga.model.command.Command;
import ru.liga.util.DateTimeConstants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static ru.liga.exception.ExceptionMessage.PLOTTING_ERROR;

/**
 * Класс отвечающий за отправку сообщений в телеграмм
 */
@Slf4j
@AllArgsConstructor
public class TelegramViewImpl implements View {
    private final Bot bot;
    private final String FILE_PATH = "src/main/resources/graph.png";
    private final String FILE_PATH_NAME = "graph.png";
    private final String FILE_NAME = "graph";
    private final int width = 1920;
    private final int height = 1080;


    @Override
    public void printMessage(Answer answer, Long chatId, Command command) {
        if (answer.getText() != null) {
            sendText(answer.getText(), chatId);
        } else if (answer.isOutputGraph()) {
            sendPhoto(getGraph(answer.getRatesMap()), chatId);
        } else {
            sendText(printRates(answer.getRatesMap()), chatId);
        }
    }

    @Override
    public void sendText(String answer, Long chatId) {
        SendMessage message = new SendMessage();
        message.setText(answer);
        message.setChatId(chatId.toString());
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.debug("Не удалось отправить сообщение: " + e.getMessage());
        }
    }

    private void sendPhoto(String fileName, Long chatId) {
        SendPhoto photo = new SendPhoto();
        photo.setPhoto(new InputFile(TelegramViewImpl.class.getResourceAsStream(fileName), FILE_NAME));
        photo.setChatId(chatId.toString());
        try {
            bot.execute(photo);
        } catch (TelegramApiException e) {
            log.debug("Не удалось отправить график: " + e.getMessage());
        }
    }

    private String printDayRate(Rate rate) {
        return String.format("%s - %s", rate.getDate().format(DateTimeConstants.PRINT_DATE_FORMATTER_TO_VIEW), String.format("%.2f", rate.getRate()));
    }

    private String printRates(Map<Currency, List<Rate>> ratesMap) {
        StringBuilder ratesString = new StringBuilder();
        List<Rate> rates = ratesMap.values().stream().findFirst().orElse(null);
        assert rates != null;
        for (Rate rate : rates) {
            ratesString.append(printDayRate(rate)).append("\n");
        }
        return ratesString.toString();
    }

    private String getGraph(Map<Currency, List<Rate>> ratesMap) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        int days = ratesMap.values().stream().findFirst().get().size();

        List<Double> x = NumpyUtils.linspace(1, days, days);
        for (Map.Entry<Currency, List<Rate>> entry : ratesMap.entrySet()) {
            Currency currency = entry.getKey();
            List<Rate> currencyRates = entry.getValue();
            List<Double> rates = currencyRates.stream()
                    .map(r -> r.getRate().doubleValue()).toList();

            XYSeries xySeries = new XYSeries(currency.name());

            for (int i = 0; i < days; i++) {
                xySeries.add(x.get(i), rates.get(i));
            }

            dataset.addSeries(xySeries);
        }

        JFreeChart jFreeChart = ChartFactory.createXYLineChart("Курс валют", "Число", "Курс", dataset);

        BufferedImage bufferedImage = jFreeChart.createBufferedImage(width, height);

        File outputfile = new File(FILE_PATH);
        try {
            ImageIO.write(bufferedImage, "png", outputfile);
        } catch (IOException e) {
            log.debug(PLOTTING_ERROR + e.getMessage());
            throw new PlottingException(PLOTTING_ERROR);
        }

        log.info("build predication finished");
        return FILE_PATH;
    }
}
