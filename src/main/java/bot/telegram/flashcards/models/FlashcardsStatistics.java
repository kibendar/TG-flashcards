package bot.telegram.flashcards.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
public class FlashcardsStatistics {
    @EmbeddedId
    private FlashcardsStatisticsPK id;

    @ManyToOne
    @JoinColumn(name = "packageId")
    private FlashcardPackage flashcardPackage;

    @Column
    private long hardCards;

    @Column
    private long failedCards;

    @Column
    private long studiedCards;

    @Embeddable
    @EqualsAndHashCode
    public static class FlashcardsStatisticsPK implements Serializable {
        @Column
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @Column
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private LocalDate date;
    }

}
