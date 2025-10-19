package bot.telegram.flashcards.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@Slf4j
public class HelpService {

    public SendMessage createHelpMessage(long chatId) {
        try {
            String helpText = """
                    📚 *Flashcards Bot - User Manual*

                    *Available Commands:*

                    /start - Get a welcome message and start using the bot

                    /showallpackages - Browse all available flashcard packages, view their descriptions, and start learning by selecting a package

                    /stop - Stop your current learning session without saving progress

                    /help - Display this help message with command information and usage instructions

                    *How to use:*
                    1. Use /showallpackages to browse and select a flashcard package to start learning
                    2. Click on a package to view its description and contents
                    3. Click "Start Learning" button within the package to begin your session
                    4. Answer each flashcard and rate your knowledge (0% - 100%)
                    5. Cards you find difficult will be repeated for better learning
                    6. Complete all cards to finish your learning session
                    7. Use /stop anytime to stop learning (progress will not be saved)

                    *Tips:*
                    - Rate 0-25% for very difficult cards (they'll repeat more)
                    - Rate 50% for moderately difficult cards
                    - Rate 75-100% for easy cards you've mastered

                    Good luck with your learning! 🎓
                    """;

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(helpText)
                    .parseMode("Markdown")
                    .build();
        }catch (Exception e){
            log.error("Error with create help message", e);
            return null;
        }
    }
}
