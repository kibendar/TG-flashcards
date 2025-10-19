package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Test class for StopService
 * Tests the functionality of stopping learning sessions
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("StopService Tests")
class StopServiceTest {

    @Mock
    private EducationService educationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private StopService stopService;

    private User user;
    private static final long CHAT_ID = 12345L;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(CHAT_ID);
    }

    @Test
    @DisplayName("Should return error message when user is not in a learning session")
    void testStopLearningSession_WhenUserNotInSession_ReturnsErrorMessage() {
        // Given
        user.setCurrentFlashcard(null);
        when(userService.getUser(CHAT_ID)).thenReturn(user);

        // When
        SendMessage result = stopService.stopLearningSession(CHAT_ID);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getChatId()).isEqualTo(String.valueOf(CHAT_ID));
        assertThat(result.getText()).contains("You are not currently in a learning session");
        assertThat(result.getText()).contains("/showallpackages");

        // Verify that clearTemporaryResources was never called
        verify(educationService, never()).clearTemporaryResourcesAfterEducation(anyLong());
    }

    @Test
    @DisplayName("Should successfully stop learning session when user is in session")
    void testStopLearningSession_WhenUserInSession_ReturnsSuccessMessage() {
        // Given
        user.setCurrentFlashcard(5L);
        when(userService.getUser(CHAT_ID)).thenReturn(user);
        doNothing().when(educationService).clearTemporaryResourcesAfterEducation(CHAT_ID);

        // When
        SendMessage result = stopService.stopLearningSession(CHAT_ID);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getChatId()).isEqualTo(String.valueOf(CHAT_ID));
        assertThat(result.getText()).contains("Your learning session has been stopped");
        assertThat(result.getText()).contains("Your progress was not saved");

        // Verify that clearTemporaryResources was called
        verify(educationService, times(1)).clearTemporaryResourcesAfterEducation(CHAT_ID);
    }

    @Test
    @DisplayName("Should handle exception and return error message")
    void testStopLearningSession_WhenExceptionThrown_ReturnsErrorMessage() {
        // Given
        when(userService.getUser(CHAT_ID)).thenThrow(new RuntimeException("Database error"));

        // When
        SendMessage result = stopService.stopLearningSession(CHAT_ID);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getChatId()).isEqualTo(String.valueOf(CHAT_ID));
        assertThat(result.getText()).contains("An error occurred");
        assertThat(result.getText()).contains("Please try again");
    }

    @Test
    @DisplayName("Should clear temporary resources when stopping active session")
    void testStopLearningSession_WhenActive_ClearsTemporaryResources() {
        // Given
        user.setCurrentFlashcard(10L);
        when(userService.getUser(CHAT_ID)).thenReturn(user);

        // When
        stopService.stopLearningSession(CHAT_ID);

        // Then
        verify(educationService).clearTemporaryResourcesAfterEducation(CHAT_ID);
    }

    @Test
    @DisplayName("Should not clear resources when user not in session")
    void testStopLearningSession_WhenNotActive_DoesNotClearResources() {
        // Given
        user.setCurrentFlashcard(null);
        when(userService.getUser(CHAT_ID)).thenReturn(user);

        // When
        stopService.stopLearningSession(CHAT_ID);

        // Then
        verify(educationService, never()).clearTemporaryResourcesAfterEducation(anyLong());
    }
}
