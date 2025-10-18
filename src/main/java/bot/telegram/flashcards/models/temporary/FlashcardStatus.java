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
public class FlashcardStatus {
    @EmbeddedId
    private FlashcardStatusPK flashcardStatusPK;

    @Column
    private Integer numberOfDuplicatedCards;

    @Column
    private String difficultyStatus;// TODO: temporary unused, will be used in statistics after learning class

    @Embeddable
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class FlashcardStatusPK implements Serializable {
        @ManyToOne
        @JoinColumn(name = "userId")
        private User user;

        @OneToOne
        @JoinColumn(name = "flashcardId")
        private Flashcard flashcard;
    }
}
