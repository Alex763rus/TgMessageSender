package com.example.tgmessagesender.service.security;

import com.example.tgmessagesender.api.responce.PostResult;
import com.example.tgmessagesender.service.rest.RestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.tgmessagesender.enums.ResponceResult.ERROR;
import static com.example.tgmessagesender.enums.ResponceResult.OK;

@Service
@Slf4j
public class SenderSettingCreater {

    @Autowired
    private RestService restService;

    public PostResult createJson(String tgNiks, String apiKey) {
        val users = tgNiks.replace("https://t.me/", "@").replace("\r\n", ",").split(",");
        val senderSettings = new SenderSettings();
        val client = new Client();
        val chats = new ArrayList<Chat>();
        client.setApiKey(apiKey);
        client.setChats(chats);
        senderSettings.setClientList(Arrays.asList(client));

        val objectMapper = new ObjectMapper();
        for (String user : users) {
            val postResult = restService.getChatInfo(apiKey, user);
            if (postResult != null && postResult.getResponceResult() == ERROR) {
                val errorMessage = new StringBuilder("Ошибка во время отправки сообщения:");
                errorMessage.append(postResult.getDescription()).append(" Чат:").append(user);
                log.error(errorMessage.toString());
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(postResult.getDescription());
                    val chat = new Chat();
                    chat.setUserName(user);
                    chat.setChatId(jsonObject.getLong("id"));
                    chats.add(chat);
                } catch (Exception ex) {
                    log.error("Ошибка при поиске ID. Сообщение:" + postResult.getDescription());
                }
            }
        }
        try {
            log.info(objectMapper.writeValueAsString(senderSettings));
            return PostResult.init().setResponceResult(OK)
                    .setDescription("OK")
                    .build();
        } catch (JsonProcessingException e) {
            return PostResult.init().setResponceResult(ERROR)
                    .setDescription("Ошибка во время парсинга контактов: " + e.getMessage())
                    .build();
        }
    }
}
