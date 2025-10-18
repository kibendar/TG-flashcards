package bot.telegram.flashcards.controller;

import bot.telegram.flashcards.misc.FlashcardAnswerStatus;
import bot.telegram.flashcards.models.FlashcardPackage;
import bot.telegram.flashcards.models.User;
import bot.telegram.flashcards.service.EducationService;
import bot.telegram.flashcards.service.FlashcardService;
import bot.telegram.flashcards.service.UserService;
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

@Controller
@AllArgsConstructor
@Slf4j
public class EducationController {

    private final EducationService educationService;
    private final UserService userService;


    public SendMessage startEducationCommandReceived(Update update) {
        try {
            long chatId = update.getMessage().getChatId();
            List<FlashcardPackage> flashcardPackageList =
                    educationService.getFlashcardPackageListByUser(chatId);

            return getChoiceMessage(chatId, flashcardPackageList);
        }catch (Exception e){
            log.error("Error: cannot start education command received", e);
            return null;
        }
    }

    private SendMessage getChoiceMessage(long chatId, List<FlashcardPackage> flashcardPackageList) {
        SendMessage packageChoiceMessage = new SendMessage();
        packageChoiceMessage.setChatId(chatId);

        if (flashcardPackageList.isEmpty()) {
            packageChoiceMessage.setText("There is no flashcard packages created yet, you can create one using /something command!");
        } else {
            packageChoiceMessage.setText("Choose flashcard package you want to practise with:");
            packageChoiceMessage.setReplyMarkup(createChoiceMessageReplyMarkup(flashcardPackageList));
        }
        return packageChoiceMessage;
    }

    private InlineKeyboardMarkup createChoiceMessageReplyMarkup(List<FlashcardPackage> flashcardPackageList) {
        List<List<InlineKeyboardButton>> buttonRowList = new ArrayList<>();
        flashcardPackageList.forEach((f) -> {
            InlineKeyboardButton button = InlineKeyboardButton.builder()
                    .text(f.getTitle())
                    .callbackData("FLASHCARD_PACKAGE_%d_SELECTED".formatted(f.getId()))
                    .build();
            buttonRowList.add(List.of(button));
        });

        return new InlineKeyboardMarkup(buttonRowList);
    }

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

        return educationService.nextRepetitionFlashcard(chatId, messageId);
    }

    public EditMessageText showAnswerRepetition(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = ((Message) callbackQuery.getMessage()).getMessageId();

        return educationService.changeMsgToMsgWithShownAnswerRepetition(chatId, messageId);
    }

}

