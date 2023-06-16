package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BotConfiguration extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();

            InlineKeyboardMarkup markUpInLine = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();
            var button1 = new InlineKeyboardButton();
            button1.setText("Мужской");
            button1.setCallbackData("М");
            var button2 = new InlineKeyboardButton();
            button2.setText("Женский");
            button2.setCallbackData("Ж");
            rowInLine.add(button1);
            rowInLine.add(button2);
            rowsInLine.add(rowInLine);
            markUpInLine.setKeyboard(rowsInLine);
            message.setReplyMarkup(markUpInLine);

            if (message != null && message.hasText()) {
                try {
                    execute(
                            SendMessage.builder()
                                    .text("Привет! \nДанный бот подскажет примерное время, когда можно сесть за руль после испития алкогольных напитков. \n" +
                                            "Для получения информации следуйте инструкции. \n"+
                                            "Бот носит развлекательный характер. Алкоголь вредит вашему здоровью.\uD83D\uDD1E\n\n"
                                    + "Для начала выберите ваш пол:")
                                    .chatId(message.getChatId().toString())
                                    .replyMarkup(markUpInLine)
                                    .build());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }


    @Override
    public String getBotUsername() = getName
    @Override
    public String getBotToken(){
    return "6133954224:AAE-rz1CtGNUhwc7fXspcFxmSOTvWeipzas";
    }

    public static void main(String[] args) throws TelegramApiException {
        BotConfiguration bot = new BotConfiguration();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }
}