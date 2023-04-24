package com.example.tgmessagesender.model.mainMenu;

import com.example.tgmessagesender.model.jpa.User;
import com.example.tgmessagesender.model.wpapper.SendMessageWrap;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class MainMenuStart extends MainMenu {

    final String MENU_NAME = "/start";

    @Override
    public String getMenuName() {
        return MENU_NAME;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        return Arrays.asList(
                SendMessageWrap.init()
                        .setChatIdLong(user.getChatId())
                        .setText(EmojiParser.parseToUnicode("Привет, " + user.getFirstName() + "!" + " :blush:"))
                        .build().createSendMessage());
    }

    @Override
    public String getDescription() {
        return " Поехали!";
    }
}
