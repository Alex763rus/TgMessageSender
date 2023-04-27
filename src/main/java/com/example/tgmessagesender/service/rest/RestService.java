package com.example.tgmessagesender.service.rest;

import com.example.tgmessagesender.api.responce.PostResult;
import com.example.tgmessagesender.api.searchchat.SearchPublicChats;
import com.example.tgmessagesender.api.sendmessage.SendTgMessage;
import com.example.tgmessagesender.api.sendmessage.Text;
import com.example.tgmessagesender.enums.ResponceResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.example.tgmessagesender.enums.ResponceResult.ERROR;
import static com.example.tgmessagesender.enums.ResponceResult.OK;

@Component
@Slf4j
public class RestService {

    private final String URL = "https://api.tdlib.org/client";

    @Autowired
    SendTgMessage sendTgMessage;

    @Autowired
    Text text;

    @Autowired
    SearchPublicChats searchPublicChats;

    public PostResult getChatInfo(String apiKey, String userName) {
        searchPublicChats.setUsername(userName);
        searchPublicChats.setApi_key(apiKey);
        return sendPostRequest(searchPublicChats);
    }

    public PostResult sendMessage(String apiKey, String chatId, String message) {
        sendTgMessage.setChat_id(chatId);
        sendTgMessage.setApi_key(apiKey);
        text.setText(message);
        return sendPostRequest(sendTgMessage);
    }

    private PostResult sendPostRequest(Object obj) {
        try {
            val restTemplate = new RestTemplate();
            val json = (new ObjectMapper().writeValueAsString(obj)).replace("\"type\"", "\"@type\"");
            val headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            val httpEntity = new HttpEntity<>(json, headers);
            val responseEntity = restTemplate.exchange(URL, HttpMethod.POST, httpEntity, String.class);
            return PostResult.init().setResponceResult(OK).setDescription(String.valueOf(responseEntity.getBody())).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return PostResult.init().setResponceResult(ERROR).setDescription(e.getMessage()).build();
        }
    }
}
