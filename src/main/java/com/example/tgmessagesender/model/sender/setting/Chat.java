package com.example.tgmessagesender.model.sender.setting;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
public class Chat {

    private String userName;
    private Long chatId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return Objects.equals(userName, chat.userName) && Objects.equals(chatId, chat.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, chatId);
    }
}
