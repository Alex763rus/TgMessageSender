package com.example.tgmessagesender.service.autosend;

import com.example.tgmessagesender.config.BotConfig;
import com.example.tgmessagesender.enums.Emoji;
import com.example.tgmessagesender.service.TelegramBot;
import com.vdurmont.emoji.EmojiParser;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.example.tgmessagesender.constant.Constant.PARSE_MODE;

@Service
@Slf4j
public class DistributionService {

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private BotConfig botConfig;

    @PostConstruct
    public void init() {
        StringBuilder message = new StringBuilder(EmojiParser.parseToUnicode(Emoji.ROBOT_FACE.getCode())).append(" telegramm bot was started!\n");
        message.append("Version: ").append(botConfig.getBotVersion());
        sendMessage(botConfig.getAdminChatId(), message.toString());
    }

    @PreDestroy
    public void squeezyExit() {
        StringBuilder message = new StringBuilder(EmojiParser.parseToUnicode(Emoji.ROBOT_FACE.getCode())).append(" telegramm bot will be *STOPPED*!\n");
        message.append("Version: ").append(botConfig.getBotVersion()).append("\n");
        message.append("*Buy!*");
        sendMessage(botConfig.getAdminChatId(), message.toString());
    }

    public void sendTgMessageToChanel(String message) {
        //sendMessage(botConfig.getTargetChatId().toString(), message); TODO
    }

    private void sendMessage(String chatId, String message){
        try {
            SendMessage sendMessage = new SendMessage(chatId, message);
            sendMessage.setParseMode(PARSE_MODE);
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

}
