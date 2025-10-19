package bot.telegram.flashcards.controller;

import bot.telegram.flashcards.service.StopService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Controller
@AllArgsConstructor
public class StopController {
    private final StopService stopService;

    public SendMessage stopCommandReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        return stopService.stopLearningSession(chatId);
    }
}
