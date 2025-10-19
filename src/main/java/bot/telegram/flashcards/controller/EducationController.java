package bot.telegram.flashcards.controller;

import bot.telegram.flashcards.misc.FlashcardAnswerStatus;
import bot.telegram.flashcards.models.FlashcardPackage;
import bot.telegram.flashcards.models.User;
import bot.telegram.flashcards.service.EducationService;
import bot.telegram.flashcards.service.FlashcardService;
import bot.telegram.flashcards.service.interfaces.IEducationService;
import bot.telegram.flashcards.service.interfaces.IUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for education/learning related operations.
 * Follows Dependency Inversion Principle - depends on service interfaces.
 */
@Controller
@AllArgsConstructor
@Slf4j
public class EducationController {

    private final IEducationService educationService;
    private final IUserService userService;

    public EditMessageText startEducation(CallbackQuery callbackQuery) {
        long flashcardPackageId = Long.parseLong(callbackQuery.getData().split("_")[2]);
        int messageId = ((Message) callbackQuery.getMessage()).getMessageId();
        long chatId = callbackQuery.getMessage().getChatId();

        User user = userService.getUser(chatId);

        user.setStartStudyTime(LocalDateTime.now());

        user.setZeroForCards();

        userService.save(user);

        return educationService.generateFlashcardList(flashcardPackageId, chatId, messageId);
    }

    public EditMessageText showAnswer(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = ((Message) callbackQuery.getMessage()).getMessageId();

        return educationService.changeMsgToMsgWithShownAnswer(chatId, messageId);
    }

    public EditMessageText answerButtonClicked(CallbackQuery callbackQuery, FlashcardAnswerStatus answerStatus) {
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = ((Message) callbackQuery.getMessage()).getMessageId();

        // Validate that user is in a learning session
        User user = userService.getUser(chatId);
        if (user.getCurrentFlashcard() == null) {
            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text("You are not currently in a learning session.\n\n" +
                          "Your session may have been stopped or expired. Please use /showallpackages to start a new learning session.")
                    .build();
        }

        switch (answerStatus) {
            case HARDEST -> educationService.duplicateFlashcard(chatId, 2);
            case HARD -> educationService.duplicateFlashcard(chatId, 1);
            case EASY -> {
                educationService.decreaseNumberOfDuplicatesIfExists(chatId);
                educationService.moveFlashcardToRepetitionList(chatId);
            }
        }
        return educationService.nextFlashcard(chatId, messageId);
    }

    public EditMessageText nextQuestionRepetition(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = ((Message) callbackQuery.getMessage()).getMessageId();

        // Validate that user is in a learning session
        User user = userService.getUser(chatId);
        if (user.getCurrentFlashcard() == null) {
            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text("You are not currently in a learning session.\n\n" +
                          "Your session may have been stopped or expired. Please use /showallpackages to start a new learning session.")
                    .build();
        }

        return educationService.nextRepetitionFlashcard(chatId, messageId);
    }

    public EditMessageText showAnswerRepetition(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = ((Message) callbackQuery.getMessage()).getMessageId();

        return educationService.changeMsgToMsgWithShownAnswerRepetition(chatId, messageId);
    }

}

