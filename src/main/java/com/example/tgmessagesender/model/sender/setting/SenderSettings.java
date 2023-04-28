package com.example.tgmessagesender.model.sender.setting;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SenderSettings {

    private List<Client> clientList;

}
