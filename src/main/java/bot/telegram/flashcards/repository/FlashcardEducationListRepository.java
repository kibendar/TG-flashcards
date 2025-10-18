package bot.telegram.flashcards.repository;

import bot.telegram.flashcards.models.User;
import bot.telegram.flashcards.models.temporary.FlashcardEducationList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FlashcardEducationListRepository
        extends CrudRepository<FlashcardEducationList, FlashcardEducationList.FlashcardEducationListPK> {
    long countFlashcardEducationListByFlashcardEducationListPK_User(User user);
    @Transactional
    void deleteAllByFlashcardEducationListPK_User(User user);
}
