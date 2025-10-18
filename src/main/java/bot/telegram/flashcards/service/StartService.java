package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.User;
import bot.telegram.flashcards.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class StartService {
    private final UserRepository userRepository;

    public boolean addUserIfNotInRepo(long chatId) {
        try {
            boolean didUserExistInRepo = userRepository.existsById(chatId);
            if (!didUserExistInRepo) {
                userRepository.save(User.builder().id(chatId).build());
            }

            return didUserExistInRepo;
        }catch (Exception e){
            log.error("Cannot put into db user", e);
            return false;
        }
    }

    public List<SendMessage> createWelcomeAndGuideMessages(long chatId, String userFirstName) {
        try {
            SendMessage welcomeMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text("Hi, " + userFirstName + "! This bot allows you to create and learn flashcards.")
                    .build();
            SendMessage guideMessage = createGuideMessage(chatId);
            return List.of(welcomeMessage, guideMessage);
        }catch (Exception e){
            log.error("Error in createWelcomeAndGuideMessages", e);
            return List.of();
        }
    }

    public List<SendMessage> createWelcomeMessageWithGetGuideButton(long chatId, String userFirstName) {
        try {
            SendMessage welcomeMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text("Hi, " + userFirstName + "! This bot allows you to create and learn flashcards. You can learn basics by clicking \"get guide\" button below.")
                    .replyMarkup(new InlineKeyboardMarkup(List.of
                            (List.of(InlineKeyboardButton.builder()
                                    .text("Get Guide")
                                    .callbackData("GET_GUIDE_BUTTON_CLICKED")
                                    .build()))))
                    .build();
            return List.of(welcomeMessage);
        }catch (Exception e){
            log.error("Error in createWelcomeMessageWithGetGuideButton", e);
            return List.of();
        }
    }

    public SendMessage createGuideMessage(long chatId) {
        try {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("GUIDE_MESSAGE")
                    .build();
        }catch (Exception e){
            log.error("Error with create guide message", e);
            return null;
        }
    }
}
