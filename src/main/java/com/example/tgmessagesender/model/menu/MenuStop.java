package com.example.tgmessagesender.model.menu;

import com.example.tgmessagesender.model.security.User;
import com.example.tgmessagesender.model.sender.setting.SenderSettings;
import com.example.tgmessagesender.model.wpapper.SendMessageWrap;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class MenuStop extends Menu {

    final String MENU_NAME = "/stop";
    @Autowired
    private SenderSettings senderSettings;

    @Override
    public String getMenuName() {
        return MENU_NAME;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        val client = senderSettings.getClientList().stream()
                .filter(e -> e.getChatIdOwner().equals(user.getChatId()))
                .findFirst().get();
        client.setEnabled(false);
        return Arrays.asList(
                SendMessageWrap.init()
                        .setChatIdLong(update.getMessage().getChatId())
                        .setText("Рассылка остановлена")
                        .build().createSendMessage());
    }

    @Override
    public String getDescription() {
        return MENU_NAME;
    }

}
