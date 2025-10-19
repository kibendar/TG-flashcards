package bot.telegram.flashcards.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entity representing a collection of related flashcards.
 * Packages are owned by users and contain multiple flashcards on a specific topic.
 */
@Entity
@Data
public class FlashcardPackage {

    /**
     * Auto-generated unique identifier for the package.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    /**
     * The title/name of the flashcard package.
     * Displayed to users when browsing packages.
     */
    @Column
    private String title;

    /**
     * Detailed description of the package contents.
     * Helps users understand what topics are covered.
     */
    @Column
    private String description;

    /**
     * List of all flashcards belonging to this package.
     * Eagerly fetched to ensure cards are available when the package is loaded.
     */
    @OneToMany(mappedBy = "flashcardPackage", fetch = FetchType.EAGER)
    private List<Flashcard> flashcardList;

    /**
     * The user who owns/created this package.
     */
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
