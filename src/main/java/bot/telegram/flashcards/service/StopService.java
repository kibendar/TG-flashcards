package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.User;
import bot.telegram.flashcards.service.interfaces.IEducationService;
import bot.telegram.flashcards.service.interfaces.IUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Service for stopping learning sessions.
 * Follows Single Responsibility Principle - only handles session termination.
 * Follows Dependency Inversion Principle - depends on abstractions (interfaces).
 */
@Service
@AllArgsConstructor
@Slf4j
public class StopService {
    private final IEducationService educationService;
    private final IUserService userService;

    public SendMessage stopLearningSession(long chatId) {
        try {
            User user = userService.getUser(chatId);

            // Check if user is actually in a learning session
            if (user.getCurrentFlashcard() == null) {
                return SendMessage.builder()
                        .chatId(chatId)
                        .text("You are not currently in a learning session.\n\n" +
                              "To start learning, use /showallpackages to browse and select a flashcard package.")
                        .build();
            }

            // Clear all temporary learning resources
            educationService.clearTemporaryResourcesAfterEducation(chatId);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Your learning session has been stopped.\n\n" +
                          "Your progress was not saved. You can start a new session anytime by selecting a flashcard package from /showallpackages.")
                    .build();
        } catch (Exception e) {
            log.error("Error stopping learning session for user {}", chatId, e);
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("An error occurred while stopping your session. Please try again.")
                    .build();
        }
    }
}
