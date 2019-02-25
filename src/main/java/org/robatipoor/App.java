package org.robatipoor;

import org.robatipoor.utils.Helpers;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class App {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(new DownloadBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

class DownloadBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId());
            String msg = update.getMessage().getText();
            if (msg.equals("/start")) {
                System.out.println("Start");
                message.setText("Please Send Url !");
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                return;
            } else {
                try {
                    Helpers.validateURL(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    message.setText("Please Send Valid Url !");
                    try {
                        execute(message); // Call method to send the message
                    } catch (TelegramApiException et) {
                        et.printStackTrace();
                    }
                    return;
                }
            }
            InstagramDownloader instagram = new InstagramDownloader();
            message.setText(instagram.getDownloadUrl(msg));
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return System.getenv("UERNAME_BOT");
    }

    @Override
    public String getBotToken() {
        return System.getenv("TELEGRAM_TOKEN");
    }
}