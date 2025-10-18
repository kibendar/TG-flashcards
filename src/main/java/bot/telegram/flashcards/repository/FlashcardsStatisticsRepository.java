package bot.telegram.flashcards.repository;

import bot.telegram.flashcards.models.FlashcardPackage;
import bot.telegram.flashcards.models.FlashcardsStatistics;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashcardsStatisticsRepository extends CrudRepository<FlashcardsStatistics, FlashcardsStatistics.FlashcardsStatisticsPK> {
    List<FlashcardsStatistics> findFlashcardsStatisticsByFlashcardPackage(FlashcardPackage flashcardPackage);
}
