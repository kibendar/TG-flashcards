package bot.telegram.flashcards.service.interfaces;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

/**
 * Interface for education/learning service operations.
 * Following Dependency Inversion Principle.
 */
public interface IEducationService {
    
    /**
     * Generates a shuffled flashcard list and starts the learning session
     * @param flashcardPackageId the ID of the package to learn
     * @param chatId the user's chat ID
     * @param messageId the message ID to edit
     * @return EditMessageText with the first flashcard
     */
    EditMessageText generateFlashcardList(long flashcardPackageId, long chatId, int messageId);
    
    /**
     * Shows the answer for the current flashcard
     * @param chatId the user's chat ID
     * @param messageId the message ID to edit
     * @return EditMessageText with answer and difficulty buttons
     */
    EditMessageText changeMsgToMsgWithShownAnswer(long chatId, int messageId);
    
    /**
     * Shows the answer for the current repetition flashcard
     * @param chatId the user's chat ID
     * @param messageId the message ID to edit
     * @return EditMessageText with answer
     */
    EditMessageText changeMsgToMsgWithShownAnswerRepetition(long chatId, int messageId);
    
    /**
     * Duplicates a flashcard in the learning queue based on difficulty
     * @param chatId the user's chat ID
     * @param numberOfDuplicates how many times to duplicate
     */
    void duplicateFlashcard(long chatId, int numberOfDuplicates);
    
    /**
     * Moves the current flashcard to the repetition list
     * @param chatId the user's chat ID
     */
    void moveFlashcardToRepetitionList(long chatId);
    
    /**
     * Decreases the number of duplicates for the current flashcard if it exists
     * @param chatId the user's chat ID
     */
    void decreaseNumberOfDuplicatesIfExists(long chatId);
    
    /**
     * Advances to the next flashcard in the learning session
     * @param chatId the user's chat ID
     * @param messageId the message ID to edit
     * @return EditMessageText with next flashcard or repetition phase
     */
    EditMessageText nextFlashcard(long chatId, int messageId);
    
    /**
     * Advances to the next flashcard in the repetition phase
     * @param chatId the user's chat ID
     * @param messageId the message ID to edit
     * @return EditMessageText with next repetition flashcard or completion
     */
    EditMessageText nextRepetitionFlashcard(long chatId, int messageId);
    
    /**
     * Clears all temporary learning resources for a user
     * @param chatId the user's chat ID
     */
    void clearTemporaryResourcesAfterEducation(long chatId);
}
