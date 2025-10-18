package bot.telegram.flashcards.controller;

import bot.telegram.flashcards.service.StatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Controller
@AllArgsConstructor
public class StatisticsController {
    private StatisticsService statisticsService;

    public SendMessage statisticsCommandReceived(Update update) {
        return statisticsService.createMonthStatisticsByAllDecksMessage(update.getMessage().getChatId());
    }
}
