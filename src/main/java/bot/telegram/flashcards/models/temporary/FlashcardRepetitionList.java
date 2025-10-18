package bot.telegram.flashcards.models.temporary;

import bot.telegram.flashcards.models.Flashcard;
import bot.telegram.flashcards.models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class FlashcardRepetitionList {
    @EmbeddedId
    private FlashcardRepetitionList.FlashcardRepetitionListPK flashcardRepetitionListPK;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flashcardId")
    private Flashcard flashcard;

    @Embeddable
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class FlashcardRepetitionListPK implements Serializable {
        private long id;

        @ManyToOne
        @JoinColumn(name = "userId")
        private User user;
    }
}
