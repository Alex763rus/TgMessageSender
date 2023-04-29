package com.example.tgmessagesender.model.menu;

import com.example.tgmessagesender.model.wpapper.SendMessageWrap;
import jakarta.persistence.MappedSuperclass;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

@MappedSuperclass
public abstract class Menu implements MenuActivity {

//    @Autowired
//    protected UserService userService;
//
//    @Autowired
//    protected StateService stateService;
//
//    @Autowired
//    protected ButtonService buttonService;
//
//    @Autowired
//    protected ContactRepository contactRepository;
//    @Autowired
//    protected FolderRepository folderRepository;
//
//    @Autowired
//    protected ExcelService excelService;
//
//    @Autowired
//    protected RestService restService;

    private static final String DEFAULT_TEXT_ERROR = "Ошибка! Команда не найдена";

    protected List<PartialBotApiMethod> errorMessageDefault(Update update) {
        return Arrays.asList(SendMessageWrap.init()
                .setChatIdLong(update.getMessage().getChatId())
                .setText(DEFAULT_TEXT_ERROR)
                .build().createSendMessage());
    }

    protected List<PartialBotApiMethod> errorMessage(Update update, String message) {
        return Arrays.asList(SendMessageWrap.init()
                .setChatIdLong(update.getMessage().getChatId())
                .setText(message)
                .build().createSendMessage());
    }
}
