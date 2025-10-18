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
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("HELP_MESSAGE")
                    .build();
        }catch (Exception e){
            log.error("Error with create help message", e);
            return null;
        }
    }
}
