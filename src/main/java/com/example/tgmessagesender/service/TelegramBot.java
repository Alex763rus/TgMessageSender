package com.example.tgmessagesender.service;

import com.example.tgmessagesender.config.BotConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.example.tgmessagesender.constant.Constant.PARSE_MODE;


@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private BotConfig botConfig;

    @Autowired
    private SenderSettingCreater senderSettingCreater;

    @PostConstruct
    public void init() {
        log.info("==" + "Server was starded. Version: " + botConfig.getBotVersion() + "==================================================================================================");
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUserName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!securityOk(update)) {
            return;
        }
        try {
            val json = senderSettingCreater.createJson(update.getMessage().getText(), botConfig.getSettingCreaterApikey());
            log.info(json);
            sendMessage(botConfig.getAdminChatId(), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(String chatId, String message) {
        try {
            SendMessage sendMessage = new SendMessage(chatId, message);
            sendMessage.setParseMode(PARSE_MODE);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private boolean securityOk(Update update) {
        if (update.hasMessage() && String.valueOf(update.getMessage().getChatId()).equals(botConfig.getAdminChatId())) {
            return true;
        }
        if (update.hasCallbackQuery() && String.valueOf(update.getCallbackQuery().getMessage().getChatId()).equals(botConfig.getAdminChatId())) {
            return true;
        }
        return false;
    }
}
