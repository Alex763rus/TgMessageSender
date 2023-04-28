package com.example.tgmessagesender.service;

import com.example.tgmessagesender.api.bot.DistributionService;
import com.example.tgmessagesender.api.rest.RestService;
import com.example.tgmessagesender.model.sender.setting.Client;
import com.example.tgmessagesender.model.sender.setting.SenderSettings;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AutoSenderService implements Runnable {

    @Autowired
    private DistributionService distributionService;

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
        if (postResult != null && postResult.getStatusCode().isError()) {
            val errorMessage = new StringBuilder("Ошибка во время отправки сообщения:");
            errorMessage.append(postResult.getBody()).append(" Чат:").append(chat);
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
