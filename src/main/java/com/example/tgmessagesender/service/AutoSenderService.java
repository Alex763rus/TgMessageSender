package com.example.tgmessagesender.service;

import com.example.tgmessagesender.api.bot.DistributionService;
import com.example.tgmessagesender.api.rest.RestService;
import com.example.tgmessagesender.exception.TelegrammServiceException;
import com.example.tgmessagesender.model.responce.PostResult;
import com.example.tgmessagesender.model.sender.setting.Client;
import com.example.tgmessagesender.model.sender.setting.SenderSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;

import static javax.management.timer.Timer.ONE_HOUR;

@Service
@Slf4j
public class AutoSenderService implements Runnable {

    @Autowired
    private DistributionService distributionService;

    @Autowired
    private SenderSettings senderSettings;
    @Autowired
    private RestService restService;

    @Autowired
    ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        val thread = new Thread(this::run);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void run() {
        val maxCountChat = getMaxCountChat();
        val delayCheckCommand = 10000;
        Long delay = ONE_HOUR / maxCountChat;
        log.info("Время между сообщениями: " + delay);
        try {
            while (true) {
                boolean hasEnabledClient = false;
                for (Client client : senderSettings.getClientList()) {
                    if (client.getChats().size() == client.getPointer()) {
                        client.setPointer(0);
                        log.info("Закончился список контактов для клиента:" + client.getChatIdOwner());
                    }
                    if (client.isEnabled()) {
                        sendOneMessage(client);
                        client.setPointer(client.getPointer() + 1);
                        hasEnabledClient = true;
                    }
                }
                if (hasEnabledClient) {
                    Thread.sleep(delay);
                } else {
                    Thread.sleep(delayCheckCommand);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendOneMessage(Client client) {
        val chat = client.getChats().get(client.getPointer());
        try {
            log.info("Try send:" + client.getApiId() + " " + chat.getChatId() + " " + chat.getUserName());
            val responce = restService.sendMessage(client.getApiId(), String.valueOf(chat.getChatId()), client.getMessage());
            val postResult = objectMapper.readValue(responce.getBody(), PostResult.class);
            if(postResult.getPostResultCode() != 0){
                throw new TelegrammServiceException(postResult.toString());
            }
        } catch (Exception e) {
            val errorMessage = new StringBuilder("Ошибка во время отправки сообщения:");
            errorMessage.append(e.getMessage()).append(" Чат:").append(chat);
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
