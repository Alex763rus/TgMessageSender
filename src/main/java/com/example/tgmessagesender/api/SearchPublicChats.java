package com.example.tgmessagesender.api;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class SearchPublicChats {

    @PostConstruct
    public void init() {
        setType("searchPublicChat");
    }

    private String api_key;
    private String type;
    private String username;
}
