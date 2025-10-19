package bot.telegram.flashcards.controller;

import bot.telegram.flashcards.service.StartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * Controller for handling /start command and user onboarding.
 * Differentiates between new and returning users.
 */
@Controller
@AllArgsConstructor
public class StartController {
    private final StartService startService;

    /**
     * Handles the /start command.
     * New users receive both welcome and guide messages immediately.
     * Returning users receive a welcome message with a button to access the guide.
     *
     * @param update the update containing the /start command
     * @return list of SendMessage objects (1 or 2 messages depending on user status)
     */
    public List<SendMessage> startCommandReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        String userFirstName = update.getMessage().getChat().getFirstName();

        if (!startService.addUserIfNotInRepo(update.getMessage().getChatId())) {
            return startService.createWelcomeAndGuideMessages(chatId, userFirstName);
        } else {
            return startService.createWelcomeMessageWithGetGuideButton(chatId, userFirstName);
        }
    }

    /**
     * Handles the "Get Guide" button click.
     * Sends the comprehensive quick start guide to the user.
     *
     * @param callbackQuery the callback query from the "Get Guide" button
     * @return list containing a single SendMessage with the guide
     */
    public List<SendMessage> getGuideButtonClicked(CallbackQuery callbackQuery) {
        return List.of(startService.createGuideMessage(((Message) callbackQuery.getMessage()).getChatId()));
    }
}
