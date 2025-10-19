package bot.telegram.flashcards.service.interfaces;

import bot.telegram.flashcards.models.User;

/**
 * Interface for user service operations.
 * Following Dependency Inversion Principle - depend on abstractions, not concretions.
 */
public interface IUserService {
    
    /**
     * Retrieves a user by their chat ID
     * @param chatId the Telegram chat ID
     * @return the User entity
     * @throws java.util.NoSuchElementException if user not found
     */
    User getUser(long chatId);
    
    /**
     * Saves or updates a user in the database
     * @param user the user entity to save
     */
    void save(User user);
}
