package com.example.tgmessagesender.service.autosend;

import com.example.tgmessagesender.model.jpa.ContactRepository;
import com.example.tgmessagesender.model.jpa.Folder;
import com.example.tgmessagesender.model.jpa.FolderRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.tgmessagesender.constant.Constant.MESSAGE;

@Service
@Slf4j
public class CheckUpdateService implements Runnable {

    @Autowired
    private DistributionService distributionService;

    @Autowired
    private DelayService delayService;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private FolderRepository folderRepository;
    private Map<Folder, String> consumers;

    @PostConstruct
    public void init() {
        consumers = new HashMap<>();
        consumers.put(folderRepository.getFoldersByNameIsAndIsDelete("VERA", false), MESSAGE);
        val thread = new Thread(this::run);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                log.info("Start post request process.");
                //TODO
                //contactRepository.getContatsByFolderAndIsDelete(consumers.get)
                log.info("End post request process. Next post request after:" + delayService.getDelay());
                Thread.sleep(delayService.getDelay());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
