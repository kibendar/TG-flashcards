package bot.telegram.flashcards.models.temporary;

import bot.telegram.flashcards.models.Flashcard;
import jakarta.persistence.*;
import bot.telegram.flashcards.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class FlashcardEducationList {
    @EmbeddedId
    private FlashcardEducationListPK flashcardEducationListPK;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flashcardId")
    private Flashcard flashcard;

    @Embeddable
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class FlashcardEducationListPK implements Serializable {
        private long id;

        @ManyToOne
        @JoinColumn(name = "userId")
        private User user;
    }
}
