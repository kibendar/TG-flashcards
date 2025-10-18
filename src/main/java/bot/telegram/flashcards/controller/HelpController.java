package bot.telegram.flashcards.controller;

import bot.telegram.flashcards.service.HelpService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Controller
@AllArgsConstructor
public class HelpController {
    private final HelpService helpService;
    public SendMessage helpCommandReceived(Update update) {
        return helpService.createHelpMessage(update.getMessage().getChatId());
    }
}