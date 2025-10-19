package bot.telegram.flashcards.misc;

/**
 * Enum representing the difficulty rating a user gives to a flashcard.
 * Determines how the card is handled in the learning algorithm:
 * - EASY (75-100%): Card moved to repetition list for final review
 * - HARD (25-50%): Card duplicated once in the learning queue
 * - HARDEST (0-25%): Card duplicated twice in the learning queue
 */
public enum FlashcardAnswerStatus {
    /**
     * User found the card easy to remember (75-100% confidence).
     * Card will be moved to the repetition list for a final review at the end.
     */
    EASY,

    /**
     * User found the card moderately difficult (25-50% confidence).
     * Card will be duplicated once to appear again later in the session.
     */
    HARD,

    /**
     * User found the card very difficult or didn't know it (0-25% confidence).
     * Card will be duplicated twice to ensure maximum reinforcement.
     */
    HARDEST
}
