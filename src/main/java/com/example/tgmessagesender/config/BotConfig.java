package com.example.tgmessagesender.config;

import com.example.tgmessagesender.model.security.WhiteListUser;
import com.example.tgmessagesender.model.sender.setting.SenderSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.File;

import static com.example.tgmessagesender.constant.Constant.*;

@Configuration
@Data
@PropertySource("application.properties")
public class BotConfig {

    @Value("${bot.version}")
    String botVersion;

    @Value("${bot.username}")
    String botUserName;

    @Value("${bot.token}")
    String botToken;

    @Value("${admin.chatid}")
    String adminChatId;

    @Value("${message.delay}")
    Long messageDelay;

    @Autowired
    ObjectMapper objectMapper;

    @SneakyThrows
    @Bean
    SenderSettings senderSettings() {
        val filePath = getCurrentPath() + SENDER_SETTING_FILE_NAME;
        val senderSettings = objectMapper.readValue(new File(filePath), SenderSettings.class);
        return senderSettings;
    }

    @SneakyThrows
    @Bean
    WhiteListUser whiteListUser() {
        val filePath = getCurrentPath() + WHITE_LIST_FILE_NAME;
        val whiteListUser = objectMapper.readValue(new File(filePath), WhiteListUser.class);
        return whiteListUser;
    }

    private String getCurrentPath() {
        return System.getProperty(USER_DIR) + SHIELD;
    }
}
