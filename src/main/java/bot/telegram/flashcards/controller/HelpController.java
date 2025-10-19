package bot.telegram.flashcards.controller;

import bot.telegram.flashcards.service.HelpService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Controller for handling /help command.
 * Provides users with comprehensive command documentation and usage instructions.
 */
@Controller
@AllArgsConstructor
public class HelpController {
    private final HelpService helpService;

    /**
     * Handles the /help command.
     * Sends a formatted help message with all available commands and usage tips.
     *
     * @param update the update containing the /help command
     * @return SendMessage with comprehensive help documentation
     */
    public SendMessage helpCommandReceived(Update update) {
        return helpService.createHelpMessage(update.getMessage().getChatId());
    }
}