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

/**
 * Main controller for the Telegram bot.
 * Handles all incoming updates (messages and callback queries) and routes them to appropriate controllers.
 * Extends TelegramLongPollingBot to receive updates via long polling.
 */
@Slf4j
@Controller
public class MainController extends TelegramLongPollingBot {
    final BotConfig config;
    private final StartController startController;
    private final HelpController helpController;
    private final EducationController educationController;
    private final ShowAllPackagesController showAllPackagesController;
    private final StopController stopController;

    @Autowired
    public MainController(BotConfig config, StartController startController, EducationController educationController, HelpController helpController, ShowAllPackagesController ShowAllPackagesController, StopController stopController) {
        super(config.getToken());
        this.config = config;
        this.startController = startController;
        this.educationController = educationController;
        this.helpController = helpController;
        this.showAllPackagesController = ShowAllPackagesController;
        this.stopController = stopController;


        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcome message"));
        listOfCommands.add(new BotCommand("/showallpackages", "show all cards to learn"));
        listOfCommands.add(new BotCommand("/stop", "stop current learning session"));
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

    /**
     * Main entry point for all bot updates.
     * Routes updates to appropriate handlers based on update type.
     *
     * @param update the incoming Telegram update
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            onMessageReceived(update);
        } else if (update.hasCallbackQuery()) {
            onCallbackQueryReceived(update);
        }
    }

    /**
     * Handles incoming text messages (commands).
     * Routes to appropriate controller based on command text.
     *
     * @param update the update containing the message
     */
    private void onMessageReceived(Update update) {
        Message msg = update.getMessage();
        String msgText = msg.getText();

        switch (msgText) {
            case "/start" -> startController.startCommandReceived(update)
                    .forEach(this::executeMessage);

            case "/help" -> executeMessage(helpController.helpCommandReceived(update));
            case "/showallpackages" -> executeMessage(showAllPackagesController.showAllPackagesCommandReceived(update));
            case "/stop" -> executeMessage(stopController.stopCommandReceived(update));
            default -> defaultMessage(msg.getChatId());
        }
    }

    /**
     * Handles incoming callback queries from inline keyboard buttons.
     * Routes to appropriate controller based on callback data.
     *
     * @param update the update containing the callback query
     */
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


    /**
     * Executes a Telegram API method (sending or editing a message).
     * Handles any Telegram API exceptions by wrapping them in RuntimeException.
     *
     * @param <T> the return type of the method
     * @param <Method> the type of BotApiMethod being executed
     * @param message the message to execute
     * @throws RuntimeException if a TelegramApiException occurs
     */
    private <T extends Serializable, Method extends BotApiMethod<T>> void executeMessage(Method message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a default error message when an unrecognized command is received.
     *
     * @param chatId the Telegram chat ID to send the message to
     */
    private void defaultMessage(long chatId) {
        SendMessage commandNotFoundMessage = SendMessage.builder()
                .chatId(chatId)
                .text("Command was not recognized, please use commands from menu list!")
                .build();

        executeMessage(commandNotFoundMessage);
    }
}
