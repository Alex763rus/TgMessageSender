package com.example.tgmessagesender.api.rest;

import com.example.tgmessagesender.config.BotConfig;
import com.example.tgmessagesender.model.telegrammService.TgMessage;
import com.example.tgmessagesender.model.tgmessage.Text;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class RestService {

    @Autowired
    BotConfig botConfig;

    @Autowired
    TgMessage tgMessage;
    @Autowired
    Text text;

    public ResponseEntity<String> sendMessage(String apiKey, String chatId, String message) throws Exception {
        tgMessage.setApiId(Long.parseLong(apiKey));
        tgMessage.setChatId(Long.parseLong(chatId));
        tgMessage.setMessage(message);
        return sendPostRequest("sendMessage", HttpMethod.POST, tgMessage);
    }

    private ResponseEntity<String> sendPostRequest(String endPoint, HttpMethod httpMethod, Object obj) throws Exception {
        val restTemplate = new RestTemplate();
        val json = (new ObjectMapper().writeValueAsString(obj)).replace("\"type\"", "\"@type\"");
        val headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        val httpEntity = new HttpEntity<>(json, headers);
        return restTemplate.exchange(botConfig.getTelegrammSenderServiceApi() + endPoint, httpMethod, httpEntity, String.class);
    }
}
