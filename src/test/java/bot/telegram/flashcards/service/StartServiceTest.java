package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.User;
import bot.telegram.flashcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for StartService
 * Tests user registration and welcome message functionality
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("StartService Tests")
class StartServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StartService startService;

    private static final long CHAT_ID = 12345L;
    private static final String USER_FIRST_NAME = "John";

    @BeforeEach
    void setUp() {
        // Reset mock behavior before each test
    }

    @Test
    @DisplayName("Should create new user when user does not exist")
    void testAddUserIfNotInRepo_WhenUserNotExists_CreatesNewUser() {
        // Given
        when(userRepository.existsById(CHAT_ID)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        boolean result = startService.addUserIfNotInRepo(CHAT_ID);

        // Then
        assertThat(result).isFalse(); // Returns false because user did not exist
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should not create user when user already exists")
    void testAddUserIfNotInRepo_WhenUserExists_DoesNotCreateUser() {
        // Given
        when(userRepository.existsById(CHAT_ID)).thenReturn(true);

        // When
        boolean result = startService.addUserIfNotInRepo(CHAT_ID);

        // Then
        assertThat(result).isTrue(); // Returns true because user already existed
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should create welcome and guide messages for new user")
    void testCreateWelcomeAndGuideMessages_ReturnsListOfTwoMessages() {
        // When
        List<SendMessage> result = startService.createWelcomeAndGuideMessages(CHAT_ID, USER_FIRST_NAME);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getChatId()).isEqualTo(String.valueOf(CHAT_ID));
        assertThat(result.get(0).getText()).contains("Hi, " + USER_FIRST_NAME);
        assertThat(result.get(1).getChatId()).isEqualTo(String.valueOf(CHAT_ID));
        assertThat(result.get(1).getText()).contains("Quick Start Guide");
    }

    @Test
    @DisplayName("Should create welcome message with get guide button for existing user")
    void testCreateWelcomeMessageWithGetGuideButton_ReturnsListWithButton() {
        // When
        List<SendMessage> result = startService.createWelcomeMessageWithGetGuideButton(CHAT_ID, USER_FIRST_NAME);

        // Then
        assertThat(result).hasSize(1);
        SendMessage message = result.get(0);
        assertThat(message.getChatId()).isEqualTo(String.valueOf(CHAT_ID));
        assertThat(message.getText()).contains("Hi, " + USER_FIRST_NAME);
        assertThat(message.getText()).contains("get guide");
        assertThat(message.getReplyMarkup()).isNotNull();
    }

    @Test
    @DisplayName("Should create guide message with comprehensive instructions")
    void testCreateGuideMessage_ReturnsDetailedGuide() {
        // When
        SendMessage result = startService.createGuideMessage(CHAT_ID);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getChatId()).isEqualTo(String.valueOf(CHAT_ID));
        assertThat(result.getText()).contains("Quick Start Guide");
        assertThat(result.getText()).contains("Step 1");
        assertThat(result.getText()).contains("/showallpackages");
        assertThat(result.getText()).contains("/help");
        assertThat(result.getText()).contains("/stop");
        assertThat(result.getParseMode()).isEqualTo("Markdown");
    }

    @Test
    @DisplayName("Should handle exception during user creation gracefully")
    void testAddUserIfNotInRepo_WhenExceptionThrown_ReturnsFalse() {
        // Given
        when(userRepository.existsById(CHAT_ID)).thenThrow(new RuntimeException("Database error"));

        // When
        boolean result = startService.addUserIfNotInRepo(CHAT_ID);

        // Then
        assertThat(result).isFalse();
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should include user first name in welcome message")
    void testCreateWelcomeAndGuideMessages_IncludesUserName() {
        // When
        List<SendMessage> result = startService.createWelcomeAndGuideMessages(CHAT_ID, "Alice");

        // Then
        assertThat(result.get(0).getText()).contains("Hi, Alice");
    }
}
