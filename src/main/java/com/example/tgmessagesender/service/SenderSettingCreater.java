package com.example.tgmessagesender.service;

import com.example.tgmessagesender.api.rest.RestService;
import com.example.tgmessagesender.model.sender.setting.Chat;
import com.example.tgmessagesender.model.sender.setting.Client;
import com.example.tgmessagesender.model.sender.setting.SenderSettings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;


@Service
@Slf4j
public class SenderSettingCreater {

    @Autowired
    private RestService restService;

    public String createJson(String tgNiks, String apiKey) throws JsonProcessingException {
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
            if (postResult != null && postResult.getStatusCode().isError()) {
                val errorMessage = new StringBuilder("Ошибка во время отправки сообщения:");
                errorMessage.append(postResult.getBody()).append(" Чат:").append(user);
                log.error(errorMessage.toString());
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(postResult.getBody());
                    val chat = new Chat();
                    chat.setUserName(user);
                    chat.setChatId(jsonObject.getLong("id"));
                    chats.add(chat);
                } catch (Exception ex) {
                    log.error("Ошибка при поиске ID. Сообщение:" + postResult.getBody());
                }
            }
        }
        return objectMapper.writeValueAsString(senderSettings);
    }
}
