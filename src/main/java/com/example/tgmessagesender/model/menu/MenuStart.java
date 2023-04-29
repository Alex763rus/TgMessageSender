package com.example.tgmessagesender.model.menu;

import com.example.tgmessagesender.model.security.User;
import com.example.tgmessagesender.model.sender.setting.SenderSettings;
import com.example.tgmessagesender.model.wpapper.SendMessageWrap;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

import static com.example.tgmessagesender.utils.StringUtils.getShield;

@Component
@Slf4j
public class MenuStart extends Menu {

    final String MENU_NAME = "/start";

    @Override
    public String getMenuName() {
        return MENU_NAME;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        return Arrays.asList(
                SendMessageWrap.init()
                        .setChatIdString(user.getChatId())
                        .setText(EmojiParser.parseToUnicode("Привет, " + getShield(user.getUserName()) + "!" + " :blush:"))
                        .build().createSendMessage());
    }

    @Override
    public String getDescription() {
        return " Поехали!";
    }
}
