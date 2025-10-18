package bot.telegram.flashcards.repository;

import bot.telegram.flashcards.models.Flashcard;
import bot.telegram.flashcards.models.FlashcardPackage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashcardPackageRepository extends CrudRepository<FlashcardPackage, Long> {
}
