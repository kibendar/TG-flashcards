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

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Test class for UserService
 * Tests user retrieval and persistence operations
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private static final long USER_ID = 12345L;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(USER_ID);
        user.setCurrentFlashcard(null);
    }

    @Test
    @DisplayName("Should successfully retrieve user by ID")
    void testGetUser_WhenUserExists_ReturnsUser() {
        // Given
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        // When
        User result = userService.getUser(USER_ID);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(USER_ID);
        verify(userRepository, times(1)).findById(USER_ID);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void testGetUser_WhenUserNotExists_ThrowsException() {
        // Given
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.getUser(USER_ID))
                .isInstanceOf(NoSuchElementException.class);

        verify(userRepository, times(1)).findById(USER_ID);
    }

    @Test
    @DisplayName("Should successfully save user")
    void testSave_WhenValidUser_SavesSuccessfully() {
        // Given
        when(userRepository.save(user)).thenReturn(user);

        // When
        userService.save(user);

        // Then
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should save user with current flashcard")
    void testSave_WhenUserHasCurrentFlashcard_SavesWithFlashcard() {
        // Given
        user.setCurrentFlashcard(5L);
        when(userRepository.save(user)).thenReturn(user);

        // When
        userService.save(user);

        // Then
        verify(userRepository, times(1)).save(user);
        assertThat(user.getCurrentFlashcard()).isEqualTo(5L);
    }

    @Test
    @DisplayName("Should save user with hard and hardest card counts")
    void testSave_WhenUserHasCardCounts_SavesWithCounts() {
        // Given
        user.addHardCard(3L);
        user.addHardestCard(2L);
        when(userRepository.save(user)).thenReturn(user);

        // When
        userService.save(user);

        // Then
        verify(userRepository, times(1)).save(user);
        assertThat(user.getHardCard()).isEqualTo(3L);
        assertThat(user.getHardestCard()).isEqualTo(2L);
    }
}
