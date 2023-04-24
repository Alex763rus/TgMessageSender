package com.example.tgmessagesender.service.autosend;

import com.example.tgmessagesender.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class DelayService {

    @Autowired
    private BotConfig botConfig;

    public Long getDelay(){
        int hourNow = LocalDateTime.now().getHour();
        Long delay = 0L;
        if(hourNow >= botConfig.getDayStartHour() && hourNow < botConfig.getNightStartHour()){
            delay = botConfig.getDayDelay();
        } else{
            delay =  botConfig.getNightDelay();
        }
        return delay;
    }

}
