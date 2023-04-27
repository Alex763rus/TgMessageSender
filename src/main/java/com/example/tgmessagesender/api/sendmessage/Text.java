package com.example.tgmessagesender.api.sendmessage;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Text {
    @PostConstruct
    public void init() {
        setType("formattedText");
    }

    private String type;
    private String text;

    @Override
    public String toString() {
        return "Text{" +
                "type='" + type + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}