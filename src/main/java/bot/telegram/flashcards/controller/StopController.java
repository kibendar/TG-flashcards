package bot.telegram.flashcards.controller;

import bot.telegram.flashcards.service.StopService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Controller for handling /stop command.
 * Allows users to terminate their current learning session.
 */
@Controller
@AllArgsConstructor
public class StopController {
    private final StopService stopService;

    /**
     * Handles the /stop command.
     * Stops the user's current learning session and clears temporary resources.
     *
     * @param update the update containing the /stop command
     * @return SendMessage with confirmation or error message
     */
    public SendMessage stopCommandReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        return stopService.stopLearningSession(chatId);
    }
}
