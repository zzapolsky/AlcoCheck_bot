package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlcoCheckerBot extends TelegramLongPollingBot {
    Formula formula = new Formula();

    private static final String START = "/start";


    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasCallbackQuery()) {
            handleCallBack(update.getCallbackQuery());


        } else if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        String name = update.getMessage().getFrom().getUserName();


        if (START.equals(message)) {
            InlineKeyboardMarkup markUpInLine = new InlineKeyboardMarkup();      // создал клавиатуру с выбором пола
            List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();
            var button1 = new InlineKeyboardButton();
            button1.setText("Мужской 👨");
            button1.setCallbackData("M");
            var button2 = new InlineKeyboardButton();
            button2.setText("Женский 👩");
            button2.setCallbackData("F");
            rowInLine.add(button1);
            rowInLine.add(button2);
            rowsInLine.add(rowInLine);
            markUpInLine.setKeyboard(rowsInLine);


            try {
                execute(                                // стартовое сообщение с началом алгоритма бота
                        SendMessage.builder()
                                .text("Привет, " + name + "!\n\n" +
                                        "Данный бот подскажет примерное время, когда можно сесть за руль после испития алкогольных напитков.\n" +
                                        "Для получения информации следуйте инструкции.\n\n" +
                                        "Бот носит развлекательный характер.\n" +
                                        "Алкоголь вредит вашему здоровью.\uD83D\uDD1E \n" +
                                        "Для начала выберите ваш пол:")
                                .chatId(chatId)
                                .replyMarkup(markUpInLine)
                                .build());


            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else {
            boolean contains = message.contains("мл");
            if (contains) {

                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(message);


                while (matcher.find()) {
                    formula.setVolume(Integer.parseInt(matcher.group()));
                }
                    int proverka = (int) formula.getVolume();
                    if (proverka > 14000) {
                        sendMessage(chatId, "Столько даже пива не выпить. Доказано.\n" + "Но мы посчитаем.");
                    }
                    if (proverka < 13) {
                        sendMessage(chatId, "Вы почти трезвы.\nОстальное, видимо, разлили.\n" + "Считаем.");
                    }


                    else sendMessage(chatId, "Выпито " + formula.getVolume() + " миллилитров. Считаем.");


                double drinks = formula.getVolume() / formula.getDrink();                // количество стандартных дринков  (проверено)

                double verh = drinks * 0.07 * 100 * 1.055;                              //  верхняя часть формулы ( проверено)

                double ves = formula.getWeight() * 10 + 5;                             //  вес с кнопки и допилен до верхней границы(проверено)
                double kef;
                double vivod;
                if (Objects.equals(formula.getSex(), "M")) {
                    kef = ves * 0.7;
                    vivod = 0.1;
                } else {                                                               // вес, умноженный на коэф веса м и ж. низ формулы (проверено)
                    kef = ves * 0.6;
                    vivod = 0.09;
                }
                formula.setResult((verh / kef));                                     // деление верха формулы на низ (результат в промилле)

                double value1 = formula.getResult();                                 // выведение результата в приличной форме
                String result = String.format("%.3f",value1);

                sendMessage(chatId, "Примерное опьянения - около " + result + " ‰");



                double trezvo = formula.getResult() / vivod;                         //  выведение примерно 0.125 у м, и 0.9 у ж в час

                double end2 = trezvo * 60;
                int chas = (int) (end2/60);
               // String end = String.format("%.2f", trezvo);
                int min = (int) (trezvo  % 60);

                sendMessage(chatId, "До выведения алкоголя около " + chas + " ч. " + min + " мин.\n" +
                        "Чтобы совершить другой рассчет, нажмите /start.\n" +
                        "Доброго дня!");


            } else {
                sendMessage(chatId, "Извините, попробуйте ввести данные по другому.\n" +
                        "Либо нажмите /start для нового рассчёта.");
            }
        }

    }

    public void handleCallBack(CallbackQuery callbackQuery) {

        // клавиатура с весом
        InlineKeyboardMarkup markUpInLine2 = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine2 = new ArrayList<>();
        var button1 = new InlineKeyboardButton();
        button1.setText("до 59");
        button1.setCallbackData("5");
        var button2 = new InlineKeyboardButton();
        button2.setText("60-69");
        button2.setCallbackData("6");
        var button3 = new InlineKeyboardButton();
        button3.setText("70-79");
        button3.setCallbackData("7");
        var button4 = new InlineKeyboardButton();
        button4.setText("80-89");
        button4.setCallbackData("8");
        var button5 = new InlineKeyboardButton();
        button5.setText("90-99");
        button5.setCallbackData("9");
        var button6 = new InlineKeyboardButton();
        button6.setText("свыше 100");
        button6.setCallbackData("10");
        rowInLine.add(button1);
        rowInLine.add(button2);
        rowInLine.add(button3);
        rowInLine2.add(button4);
        rowInLine2.add(button5);
        rowInLine2.add(button6);
        rowsInLine.add(rowInLine);
        rowsInLine.add(rowInLine2);
        markUpInLine2.setKeyboard(rowsInLine);


        // клавиатура с напитками
        InlineKeyboardMarkup markUpInLine3 = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rrowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rrowInLine = new ArrayList<>();
        List<InlineKeyboardButton> rrowInLine2 = new ArrayList<>();
        var button11 = new InlineKeyboardButton();
        button11.setText("Пиво \uD83C\uDF7A");
        button11.setCallbackData("300");
        var button12 = new InlineKeyboardButton();
        button12.setText("Водка \uD83E\uDD5B");
        button12.setCallbackData("32");
        var button13 = new InlineKeyboardButton();
        button13.setText("Коньяк \uD83E\uDD43");
        button13.setCallbackData("27");
        var button14 = new InlineKeyboardButton();
        button14.setText("Вино \uD83C\uDF77");
        button14.setCallbackData("120");
        var button15 = new InlineKeyboardButton();
        button15.setText("Ликёр \uD83C\uDF78");
        button15.setCallbackData("40");
        var button16 = new InlineKeyboardButton();
        button16.setText("Мартини \uD83C\uDF79");
        button16.setCallbackData("100");
        rrowInLine.add(button11);
        rrowInLine.add(button12);
        rrowInLine.add(button13);
        rrowInLine2.add(button14);
        rrowInLine2.add(button15);
        rrowInLine2.add(button16);
        rrowsInLine.add(rrowInLine);
        rrowsInLine.add(rrowInLine2);
        markUpInLine3.setKeyboard(rrowsInLine);
        Message message = callbackQuery.getMessage();
        String data = callbackQuery.getData();

        switch (data) {


            //обработка нажатий кнопок с полом

            case "M" -> {
                formula.setSex ("M");                                   // записываю пол в класс Формула
                try {
                    execute(
                            SendMessage.builder()
                                    .text("""
                                            Ваш пол - мужской.
                                            Теперь выберите диапазон вашего веса:
                                            """)
                                    .chatId(callbackQuery.getMessage().getChatId())
                                    .replyMarkup(markUpInLine2)
                                    .build());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }


            case "F" -> {
                formula.setSex("F");

                try {
                    execute(
                            SendMessage.builder()
                                    .text("""
                                            Ваш пол - женский.
                                            Теперь выберите диапазон вашего веса:
                                            """)
                                    .chatId(callbackQuery.getMessage().getChatId())
                                    .replyMarkup(markUpInLine2)
                                    .build());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }


            //обработка нажатий кнопок с весом
            case "5" -> {
                                                                // записываю вес в класс Формула
                formula.setWeight(5);
                try {
                    execute(
                            SendMessage.builder()
                                    .text("""
                                            Вы выбрали вес до 59 кг.
                                            Перейдём к напиткам.
                                            Что выпивали?
                                            """ )
                                    .chatId(callbackQuery.getMessage().getChatId())
                                    .replyMarkup(markUpInLine3)
                                    .build());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            case "6" -> {

                formula.setWeight(6);
                try {
                    execute(
                            SendMessage.builder()
                                    .text("""
                                            Вы выбрали вес 60-69 кг.
                                            Перейдём к напиткам.
                                            Что выпивали?
                                            """)
                                    .chatId(callbackQuery.getMessage().getChatId())
                                    .replyMarkup(markUpInLine3)
                                    .build());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            case "7" -> {

                formula.setWeight(7);
                try {
                    execute(
                            SendMessage.builder()
                                    .text("""
                                            Вы выбрали вес 70-79 кг.
                                            Перейдём к напиткам.
                                            Что выпивали?
                                            """)
                                    .chatId(callbackQuery.getMessage().getChatId())
                                    .replyMarkup(markUpInLine3)
                                    .build());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            case "8" -> {

                formula.setWeight(8);
                try {
                    execute(
                            SendMessage.builder()
                                    .text("""
                                            Вы выбрали вес 80-89 кг.
                                            Перейдём к напиткам.
                                            Что выпивали?
                                            """)
                                    .chatId(callbackQuery.getMessage().getChatId())
                                    .replyMarkup(markUpInLine3)
                                    .build());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            case "9" -> {

                formula.setWeight(9);
                try {
                    execute(
                            SendMessage.builder()
                                    .text("""
                                            Вы выбрали вес 90-99 кг.
                                            Перейдём к напиткам.
                                            Что выпивали?
                                            """)
                                    .chatId(callbackQuery.getMessage().getChatId())
                                    .replyMarkup(markUpInLine3)
                                    .build());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            case "10" -> {

                formula.setWeight(10);
                try {
                    execute(
                            SendMessage.builder()
                                    .text("""
                                            Вы выбрали вес 100 кг. и более.
                                            Перейдём к напиткам.
                                            Что выпивали?
                                            """)
                                    .chatId(callbackQuery.getMessage().getChatId())
                                    .replyMarkup(markUpInLine3)
                                    .build());

                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            default -> {

                int a;
                a = Integer.parseInt(callbackQuery.getData());
                formula.setDrink(a);
                sendMessage(message.getChatId(), "Сколько было выпито напитка? \uD83E\uDED7\nНапример, 200 мл");


            }

        }
    }

    @Override
    public String getBotUsername() {
        return BotConfig.name;
    }

    // регистрация бота с именем и токеном из конфига
    @Override
    public String getBotToken() {
        return BotConfig.token;
    }

    public static void main(String[] args) throws TelegramApiException {
        AlcoCheckerBot bot = new AlcoCheckerBot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }


    private void sendMessage(Long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}