package com.example.tgmessagesender.model.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@ToString
public class WhiteListUser {
    private Set<User> users;

}
