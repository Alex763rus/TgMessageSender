package com.example.tgmessagesender.model.telegrammService;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class TgMessage {
    private Long apiId;
    private Long chatId;
    private String message;

    @Override
    public String toString() {
        return "TgMessage{" +
                "apiId=" + apiId +
                ", chatId=" + chatId +
                ", message='" + message + '\'' +
                '}';
    }
}