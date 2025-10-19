package bot.telegram.flashcards.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for HelpService
 * Tests help message generation
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("HelpService Tests")
class HelpServiceTest {

    @InjectMocks
    private HelpService helpService;

    private static final long CHAT_ID = 12345L;

    @Test
    @DisplayName("Should create help message with all commands")
    void testCreateHelpMessage_ReturnsCompleteHelpText() {
        // When
        SendMessage result = helpService.createHelpMessage(CHAT_ID);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getChatId()).isEqualTo(String.valueOf(CHAT_ID));
        assertThat(result.getText()).isNotNull();
    }

    @Test
    @DisplayName("Should include all bot commands in help message")
    void testCreateHelpMessage_IncludesAllCommands() {
        // When
        SendMessage result = helpService.createHelpMessage(CHAT_ID);

        // Then
        String text = result.getText();
        assertThat(text).contains("/start");
        assertThat(text).contains("/showallpackages");
        assertThat(text).contains("/stop");
        assertThat(text).contains("/help");
    }

    @Test
    @DisplayName("Should use Markdown parse mode")
    void testCreateHelpMessage_UsesMarkdownParseMode() {
        // When
        SendMessage result = helpService.createHelpMessage(CHAT_ID);

        // Then
        assertThat(result.getParseMode()).isEqualTo("Markdown");
    }

    @Test
    @DisplayName("Should include usage instructions")
    void testCreateHelpMessage_IncludesUsageInstructions() {
        // When
        SendMessage result = helpService.createHelpMessage(CHAT_ID);

        // Then
        String text = result.getText();
        assertThat(text).contains("How to use");
        assertThat(text).contains("flashcard");
    }

    @Test
    @DisplayName("Should include tips for learning")
    void testCreateHelpMessage_IncludesTips() {
        // When
        SendMessage result = helpService.createHelpMessage(CHAT_ID);

        // Then
        String text = result.getText();
        assertThat(text).contains("Tips");
        assertThat(text).contains("0-25%");
        assertThat(text).contains("75-100%");
    }

    @Test
    @DisplayName("Should work with different chat IDs")
    void testCreateHelpMessage_WorksWithDifferentChatIds() {
        // When
        SendMessage result1 = helpService.createHelpMessage(111L);
        SendMessage result2 = helpService.createHelpMessage(222L);

        // Then
        assertThat(result1.getChatId()).isEqualTo("111");
        assertThat(result2.getChatId()).isEqualTo("222");
        assertThat(result1.getText()).isEqualTo(result2.getText());
    }
}
