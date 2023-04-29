package com.example.tgmessagesender.service;

import com.example.tgmessagesender.api.rest.RestService;
import com.example.tgmessagesender.model.sender.setting.Chat;
import com.example.tgmessagesender.model.sender.setting.Client;
import com.example.tgmessagesender.model.sender.setting.SenderSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;


@Service
@Slf4j
public class SenderSettingCreater {

    @Autowired
    private RestService restService;

    public SenderSettings createSenderSettings(String tgNiks, String apiKey) {
        val users = tgNiks.replace("https://t.me/", "@").replace("\r\n", ",").split(",");
        val senderSettings = new SenderSettings();
        val client = new Client();
        val chats = new ArrayList<Chat>();
        client.setApiKey(apiKey);
        client.setChats(chats);
        senderSettings.setClientList(Arrays.asList(client));
        for (String user : users) {
            ResponseEntity<String> postResult = null;
            try {
                postResult = restService.getChatInfo(apiKey, user);
                val jsonObject = new JSONObject(postResult.getBody());
                val chat = new Chat();
                chat.setUserName(user);
                chat.setChatId(jsonObject.getLong("id"));
                chats.add(chat);
            } catch (Exception e) {
                val errorMessage = new StringBuilder("Ошибка во время отправки сообщения:");
                errorMessage.append(postResult.getBody()).append(" Чат:").append(user);
                log.error(errorMessage.toString());
            }
        }
        return senderSettings;
    }
}
