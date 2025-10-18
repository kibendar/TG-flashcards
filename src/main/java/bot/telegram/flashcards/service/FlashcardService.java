package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.Flashcard;
import bot.telegram.flashcards.models.FlashcardPackage;
import bot.telegram.flashcards.repository.FlashcardPackageRepository;
import bot.telegram.flashcards.repository.FlashcardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FlashcardService {
    private final FlashcardRepository flashcardRepository;
    private final FlashcardPackageRepository flashcardPackageRepository;

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

    public FlashcardPackage getFlashcardPackage(long packageId) {
        return flashcardPackageRepository.findById(packageId).orElseThrow();
    }
}
