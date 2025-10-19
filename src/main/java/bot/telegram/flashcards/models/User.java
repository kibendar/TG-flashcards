package bot.telegram.flashcards.models;

import bot.telegram.flashcards.models.temporary.FlashcardEducationList;
import bot.telegram.flashcards.models.temporary.FlashcardRepetitionList;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a Telegram bot user.
 * Stores user session information, owned packages, and learning statistics.
 * Mapped to the "account" table in the database.
 */
@Entity
@Table(name = "account")
@Data
public class User {
    /**
     * The user's unique identifier, corresponding to their Telegram chat ID.
     */
    @Id
    @Column
    private long id;

    /**
     * List of flashcard packages owned/created by this user.
     * Eagerly fetched to ensure packages are always available.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<FlashcardPackage> flashcardPackageList;

    /**
     * List of flashcards in the user's current learning queue.
     * This is a temporary list that gets cleared after the session ends.
     */
    @OneToMany(mappedBy = "flashcardEducationListPK.user")
    private List<FlashcardEducationList> flashcardEducationList;

    /**
     * List of flashcards marked for repetition in the current session.
     * Contains cards the user rated as "easy" and will review at the end.
     */
    @OneToMany(mappedBy = "flashcardRepetitionListPK.user")
    private List<FlashcardRepetitionList> flashcardRepetitionList;

    /**
     * The position/index of the current flashcard in the learning session.
     * Null when the user is not in an active learning session.
     */
    @Column
    private Long currentFlashcard;

    /**
     * Timestamp when the current learning session started.
     */
    @Column
    private LocalDateTime startStudyTime;

    /**
     * Timestamp when the current learning session ended.
     */
    @Column
    private LocalDateTime endStudyTime;

    /**
     * Count of cards rated as "hard" (25-50% difficulty) in the current session.
     */
    @Column
    private Long hardCard;

    /**
     * Count of cards rated as "hardest" (0-25% difficulty) in the current session.
     */
    @Column
    private Long hardestCard;

    /**
     * Default constructor that initializes card counters to zero.
     */
    public User(){
        this.hardCard = 0L;
        this.hardestCard = 0L;
    }

    /**
     * Increments the count of hard cards encountered in the session.
     *
     * @param increase the amount to increase the counter by
     */
    public void addHardCard(Long increase) {
        this.hardCard += increase;
    }

    /**
     * Increments the count of hardest cards encountered in the session.
     *
     * @param increase the amount to increase the counter by
     */
    public void addHardestCard(Long increase) {
        this.hardestCard += increase;
    }

    /**
     * Resets both hard and hardest card counters to zero.
     * Called at the start of a new learning session.
     */
    public void setZeroForCards(){
        this.hardCard = 0L;
        this.hardestCard = 0L;
    }

    @Builder
    private static User createUserWithId(long id) {
        User user = new User();
        user.setId(id);
        return user;
    }
}
