package bot.telegram.flashcards;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TgFlashcardsBotApplication {

    private static final Logger log = LoggerFactory.getLogger(TgFlashcardsBotApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TgFlashcardsBotApplication.class, args);

        log.info("Bot started");
    }

}
