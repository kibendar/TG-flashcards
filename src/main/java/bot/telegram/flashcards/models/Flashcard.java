package bot.telegram.flashcards.models;

import bot.telegram.flashcards.models.temporary.FlashcardEducationList;
import bot.telegram.flashcards.models.temporary.FlashcardRepetitionList;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entity representing a single flashcard with a question and answer.
 * Each flashcard belongs to a FlashcardPackage and can appear in learning sessions.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flashcard {
    /**
     * Auto-generated unique identifier for the flashcard.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    /**
     * The package this flashcard belongs to.
     * Many flashcards can belong to one package.
     */
    @ManyToOne
    @JoinColumn(name = "packageId", referencedColumnName = "id")
    private FlashcardPackage flashcardPackage;

    /**
     * List of education list entries for this flashcard.
     * Tracks when this card appears in users' learning queues.
     */
    @OneToMany(mappedBy = "flashcard")
    private List<FlashcardEducationList> flashcardEducationList;

    /**
     * Repetition list entry for this flashcard.
     * Non-null when the card is marked for repetition in a session.
     */
    @OneToOne(mappedBy = "flashcard")
    private FlashcardRepetitionList flashcardRepetitionList;

    /**
     * The question displayed on the front of the flashcard.
     */
    @Column
    private String question;

    /**
     * The answer displayed when the user clicks "Show answer".
     */
    @Column
    private String answer;
}
