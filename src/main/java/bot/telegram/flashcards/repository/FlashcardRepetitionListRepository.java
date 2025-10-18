package bot.telegram.flashcards.repository;

import bot.telegram.flashcards.models.Flashcard;
import bot.telegram.flashcards.models.User;
import bot.telegram.flashcards.models.temporary.FlashcardRepetitionList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface FlashcardRepetitionListRepository
        extends CrudRepository<FlashcardRepetitionList, FlashcardRepetitionList.FlashcardRepetitionListPK> {
    @Query(nativeQuery = true, value = "SELECT id from flashcard_repetition_list where user_id = :userId order by id desc")
    List<Long> findIds(long userId);
    List<FlashcardRepetitionList> findAllByFlashcard(Flashcard flashcard);
    List<FlashcardRepetitionList> findAllByFlashcardRepetitionListPK_User(User user);

    long countFlashcardRepetitionListByFlashcardRepetitionListPK_User(User user);
    @Transactional
    void deleteAllByFlashcardRepetitionListPK_User(User user);
}
