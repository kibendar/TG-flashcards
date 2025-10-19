package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.User;
import bot.telegram.flashcards.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

/**
 * Service for handling user onboarding and welcome messages.
 * Follows Single Responsibility Principle - only handles start/welcome functionality.
 */
@Service
@AllArgsConstructor
@Slf4j
public class StartService {
    private final UserRepository userRepository;

    /**
     * Adds a new user to the database if they don't already exist.
     * This method is called when a user first interacts with the bot.
     *
     * @param chatId the Telegram chat ID of the user
     * @return true if the user already existed in the repository, false if they were newly added or an error occurred
     */
    public boolean addUserIfNotInRepo(long chatId) {
        try {
            boolean didUserExistInRepo = userRepository.existsById(chatId);
            if (!didUserExistInRepo) {
                userRepository.save(User.builder().id(chatId).build());
            }

            return didUserExistInRepo;
        }catch (Exception e){
            log.error("Cannot put into db user", e);
            return false;
        }
    }

    /**
     * Creates both welcome and guide messages for returning users.
     * This variant sends both messages immediately without requiring user interaction.
     *
     * @param chatId the Telegram chat ID
     * @param userFirstName the user's first name from Telegram
     * @return list containing welcome message and guide message, or empty list on error
     */
    public List<SendMessage> createWelcomeAndGuideMessages(long chatId, String userFirstName) {
        try {
            SendMessage welcomeMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text("Hi, " + userFirstName + "! This bot allows you to create and learn flashcards.")
                    .build();
            SendMessage guideMessage = createGuideMessage(chatId);
            return List.of(welcomeMessage, guideMessage);
        }catch (Exception e){
            log.error("Error in createWelcomeAndGuideMessages", e);
            return List.of();
        }
    }

    /**
     * Creates a welcome message with an inline button to access the guide.
     * This variant is used for new users to give them control over when to view the guide.
     *
     * @param chatId the Telegram chat ID
     * @param userFirstName the user's first name from Telegram
     * @return list containing a single welcome message with "Get Guide" button, or empty list on error
     */
    public List<SendMessage> createWelcomeMessageWithGetGuideButton(long chatId, String userFirstName) {
        try {
            SendMessage welcomeMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text("Hi, " + userFirstName + "! This bot allows you to create and learn flashcards. You can learn basics by clicking \"get guide\" button below.")
                    .replyMarkup(new InlineKeyboardMarkup(List.of
                            (List.of(InlineKeyboardButton.builder()
                                    .text("Get Guide")
                                    .callbackData("GET_GUIDE_BUTTON_CLICKED")
                                    .build()))))
                    .build();
            return List.of(welcomeMessage);
        }catch (Exception e){
            log.error("Error in createWelcomeMessageWithGetGuideButton", e);
            return List.of();
        }
    }

    /**
     * Creates a comprehensive quick start guide message with step-by-step instructions.
     * The guide includes how to browse packages, start learning, answer cards, and use commands.
     *
     * @param chatId the Telegram chat ID
     * @return SendMessage containing the formatted guide text, or null on error
     */
    public SendMessage createGuideMessage(long chatId) {
        try {
            String guideText = """
                    📖 *Quick Start Guide*

                    Welcome to the Flashcards Bot! Here's how to get started:

                    *Step 1: Browse Packages*
                    Use /showallpackages to see all available flashcard packages. Each package contains cards on a specific topic.

                    *Step 2: View Package Details*
                    Click on any package to see:
                    • Package description
                    • Number of cards
                    • Preview of the cards

                    *Step 3: Start Learning*
                    Click the "Start education" button to begin your learning session.

                    *Step 4: Answer Cards*
                    For each flashcard:
                    1. Read the question
                    2. Click "Show answer" to reveal the answer
                    3. Rate how well you knew it (0% to 100%)

                    *Step 5: Complete Your Session*
                    Cards you find difficult will repeat for better learning. Complete all cards to finish!

                    *Other Commands:*
                    /help - View all available commands
                    /stop - Stop your current learning session

                    Ready to start? Use /showallpackages now!
                    """;

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(guideText)
                    .parseMode("Markdown")
                    .build();
        }catch (Exception e){
            log.error("Error with create guide message", e);
            return null;
        }
    }
}
