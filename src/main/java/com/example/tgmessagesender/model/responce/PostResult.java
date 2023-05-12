package com.example.tgmessagesender.model.responce;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PostResult {
    private Long postResultCode;
    private Long apiId;
    private String description;

    @Override
    public String toString() {
        return "PostResult{" +
                "postResultCode=" + postResultCode +
                ", apiId=" + apiId +
                ", description='" + description + '\'' +
                '}';
    }
}
