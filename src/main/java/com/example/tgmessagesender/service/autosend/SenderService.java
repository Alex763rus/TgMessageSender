package com.example.tgmessagesender.service.autosend;

import com.example.tgmessagesender.service.rest.RestService;
import com.example.tgmessagesender.service.security.Chat;
import com.example.tgmessagesender.service.security.Client;
import com.example.tgmessagesender.service.security.SenderSettings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

import static com.example.tgmessagesender.enums.ResponceResult.ERROR;

@Service
@Slf4j
public class SenderService implements Runnable {

    @Autowired
    private DistributionService distributionService;

    @Autowired
    private DelayService delayService;

    @Autowired
    private SenderSettings senderSettings;
    @Autowired
    private RestService restService;

    @PostConstruct
    public void init() {
        val thread = new Thread(this::run);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void run() {
        val maxCountChat = getMaxCountChat();
        Long delay = 1000L * 60L * 60L / maxCountChat;
        try {
            while (true) {
                log.info("Start send message process.");
                for (Client client : senderSettings.getClientList()) {
                    if (client.getChats().size() == client.getPointer()) {
                        client.setPointer(0);
                    }
                    sendOneMessage(client);
                    client.setPointer(client.getPointer() + 1);
                }
                log.info("End post request process. Next post request after:" + delay);
                Thread.sleep(delay);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendOneMessage(Client client) {
        val chat = client.getChats().get(client.getPointer());
        val postResult = restService.sendMessage(client.getApiKey(), String.valueOf(chat.getChatId()), client.getMessage());
        if (postResult != null && postResult.getResponceResult() == ERROR) {
            val errorMessage = new StringBuilder("Ошибка во время отправки сообщения:");
            errorMessage.append(postResult.getDescription()).append(" Чат:").append(chat);
            log.error(errorMessage.toString());
            distributionService.sendTgMessageToAdmin(errorMessage.toString());
        }
    }

    private int getMaxCountChat() {
        int maxChats = 0;
        for (Client client : senderSettings.getClientList()) {
            if (client.getChats().size() > maxChats) {
                maxChats = client.getChats().size();
            }
        }
        return maxChats;
    }
}
