package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.FlashcardsStatistics;
import bot.telegram.flashcards.repository.FlashcardsStatisticsRepository;
import bot.telegram.flashcards.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Service
@AllArgsConstructor
public class StatisticsService {
    private FlashcardsStatisticsRepository statisticsRepository;
    private UserRepository userRepository;

    public SendMessage createMonthStatisticsByAllDecksMessage(long chatId) {
        List<FlashcardsStatistics> statisticsList =
                statisticsRepository.findFlashcardsStatisticsByFlashcardPackage(
                        //TODO: show NullPointerException in test
                        userRepository.findById(chatId).orElse(null).getFlashcardPackageList().get(0));// TODO: change to something more adequate

        long failedCards, hardCards, studiedCards;
        failedCards = hardCards = studiedCards = 0;

        for (FlashcardsStatistics statistics : statisticsList) {
            failedCards += statistics.getFailedCards();
            hardCards += statistics.getHardCards();
            studiedCards += statistics.getStudiedCards();
        }

        return SendMessage.builder()
                .chatId(chatId)
                .text("Month statistics by all decks:\nfailed cards: %d\nhard cards: %d\nstudied cards: %d"
                        .formatted(failedCards, hardCards, studiedCards))
                .build();
    }
}
