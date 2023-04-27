package com.example.tgmessagesender.service.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Client {

    private String apiKey;

    private String message;

    private List<Chat> chats;

    private int pointer;

}
