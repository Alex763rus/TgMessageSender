package com.example.tgmessagesender.api.responce;

import com.example.tgmessagesender.enums.ResponceResult;
import lombok.Getter;
import lombok.experimental.SuperBuilder;


@Getter
@SuperBuilder(setterPrefix = "set", builderMethodName = "init", toBuilder = true)
public class PostResult {

    private ResponceResult responceResult;

    private String description;

}
