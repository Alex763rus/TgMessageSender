package com.example.tgmessagesender.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class WhiteListUser {
    private Map<Long, String> whiteListChatsID;

}
