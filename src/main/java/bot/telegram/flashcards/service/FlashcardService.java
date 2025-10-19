package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.Flashcard;
import bot.telegram.flashcards.models.FlashcardPackage;
import bot.telegram.flashcards.repository.FlashcardPackageRepository;
import bot.telegram.flashcards.repository.FlashcardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing flashcard and flashcard package operations.
 * Provides methods for retrieving flashcards and packages from the database.
 * Follows Single Responsibility Principle - only handles flashcard data operations.
 */
@Service
@AllArgsConstructor
public class FlashcardService {
    private final FlashcardRepository flashcardRepository;
    private final FlashcardPackageRepository flashcardPackageRepository;

    /**
     * Retrieves all flashcard IDs that belong to a specific flashcard package.
     *
     * @param flashcardsId the ID of the flashcard to look up
     * @return list of flashcard IDs belonging to the package, or empty list if package is null
     * @throws java.util.NoSuchElementException if flashcard with given ID is not found
     * @deprecated This method has a confusing parameter name and may throw NullPointerException.
     *             Consider refactoring to accept packageId directly.
     */
    public List<Long> getFlashcardIdsByFlashcardsId(Long flashcardsId) {
        //TODO: this method show NullPointerException if flashcardsId == null, even if we have flashcardsId in test
        FlashcardPackage flashcards = flashcardRepository.findById(flashcardsId).orElseThrow().getFlashcardPackage();
        List<Long> flashcardIds = new ArrayList<>();

        if(flashcards != null){
            List<Flashcard> flashcardList = flashcards.getFlashcardList();
            for(Flashcard flashcard : flashcardList)
                flashcardIds.add(flashcard.getId());
        }
        return flashcardIds;
    }

    /**
     * Retrieves a flashcard package by its ID.
     *
     * @param packageId the ID of the flashcard package
     * @return the FlashcardPackage entity
     * @throws java.util.NoSuchElementException if package with given ID is not found
     */
    public FlashcardPackage getFlashcardPackage(long packageId) {
        return flashcardPackageRepository.findById(packageId).orElseThrow();
    }
}
