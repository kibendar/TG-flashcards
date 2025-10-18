package bot.telegram.flashcards.controller;


import bot.telegram.flashcards.misc.FlashcardAnswerStatus;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import bot.telegram.flashcards.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.*;

@Slf4j
@Controller
public class MainController extends TelegramLongPollingBot {
    final BotConfig config;
    private final StartController startController;
    private final HelpController helpController;
    private final EducationController educationController;
    private final StatisticsController statisticsController;
    private final ShowAllPackagesController showAllPackagesController;

    @Autowired
    public MainController(BotConfig config, StartController startController, EducationController educationController, HelpController helpController, StatisticsController statisticsController, ShowAllPackagesController ShowAllPackagesController) {
        super(config.getToken());
        this.config = config;
        this.startController = startController;
        this.educationController = educationController;
        this.helpController = helpController;
        this.statisticsController = statisticsController;
        this.showAllPackagesController = ShowAllPackagesController;


        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcome message"));
        listOfCommands.add(new BotCommand("/starteducation", "start learning cards"));
        listOfCommands.add(new BotCommand("/showallpackages", "show all cards to learn"));
        listOfCommands.add(new BotCommand("/statistics", "show learning statistics"));
        listOfCommands.add(new BotCommand("/help", "show commands info and usages"));


        try {
            execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            onMessageReceived(update);
        } else if (update.hasCallbackQuery()) {
            onCallbackQueryReceived(update);
        }
    }


    private void onMessageReceived(Update update) {
        Message msg = update.getMessage();
        String msgText = msg.getText();

        switch (msgText) {
            case "/start" -> startController.startCommandReceived(update)
                    .forEach(this::executeMessage);

            case "/starteducation" -> executeMessage(educationController.startEducationCommandReceived(update));
            case "/statistics" -> executeMessage(statisticsController.statisticsCommandReceived(update));
            case "/help" -> executeMessage(helpController.helpCommandReceived(update));
            case "/showallpackages" -> executeMessage(showAllPackagesController.showAllPackagesCommandReceived(update));
            default -> defaultMessage(msg.getChatId());
        }
    }

    private void onCallbackQueryReceived(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String callbackQueryData = callbackQuery.getData();

            switch (callbackQueryData) {
                case "GET_GUIDE_BUTTON_CLICKED" -> startController.getGuideButtonClicked(callbackQuery)
                        .forEach(this::executeMessage);
                case "SHOW_ANSWER_CLICKED" -> executeMessage(educationController.showAnswer(callbackQuery));
                case "0%_BUTTON_CLICKED" -> executeMessage(educationController.answerButtonClicked(callbackQuery, FlashcardAnswerStatus.HARDEST));
                case "25%_BUTTON_CLICKED", "50%_BUTTON_CLICKED" -> executeMessage(educationController.answerButtonClicked(callbackQuery, FlashcardAnswerStatus.HARD));
                case "75%_BUTTON_CLICKED", "100%_BUTTON_CLICKED" -> executeMessage(educationController.answerButtonClicked(callbackQuery, FlashcardAnswerStatus.EASY));
                case "SHOW_ANSWER_REPETITION_CLICKED" -> executeMessage(educationController.showAnswerRepetition(callbackQuery));
                case "NEXT_QUESTION_REPETITION_CLICKED" -> executeMessage(educationController.nextQuestionRepetition(callbackQuery));
                default -> {
                    if (callbackQueryData.matches("FLASHCARD_PACKAGE_\\d+_SELECTED")) {
                        executeMessage(educationController.startEducation(callbackQuery));
                    } else if (callbackQueryData.matches("SHOW_ALL_PACKAGES_\\d+_SELECTED")) {
                        executeMessage(showAllPackagesController.showPackageDescription(callbackQuery));
                    } else if(callbackQueryData.matches("(FIRST|PREVIOUS|NEXT)_CARD_\\d+_OF_PACKAGE_\\d+_CLICKED")){
                        executeMessage(showAllPackagesController.showPreviousOrNextCard(callbackQuery));
                    }
                }
            }
        }


    private <T extends Serializable, Method extends BotApiMethod<T>> void executeMessage(Method message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void defaultMessage(long chatId) {
        SendMessage commandNotFoundMessage = SendMessage.builder()
                .chatId(chatId)
                .text("Command was not recognized, please use commands from menu list!")
                .build();

        executeMessage(commandNotFoundMessage);
    }
}
