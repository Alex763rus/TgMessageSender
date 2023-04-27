package com.example.tgmessagesender.service.security;

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
