package bot.telegram.flashcards.repository;

import bot.telegram.flashcards.models.User;
import bot.telegram.flashcards.models.temporary.FlashcardStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FlashcardStatusRepository
        extends CrudRepository<FlashcardStatus, FlashcardStatus.FlashcardStatusPK> {
    @Transactional
    void deleteAllByFlashcardStatusPK_User(User user);
}