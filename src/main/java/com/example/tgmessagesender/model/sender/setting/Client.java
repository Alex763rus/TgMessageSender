package com.example.tgmessagesender.model.sender.setting;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Client {

    private String chatIdOwner;

    private volatile boolean enabled;
    private String apiKey;

    private String message;

    private List<Chat> chats;

    private int pointer;

}
