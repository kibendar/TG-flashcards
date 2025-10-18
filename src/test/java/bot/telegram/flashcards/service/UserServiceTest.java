package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.User;
import bot.telegram.flashcards.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User mockUser;
    private long userId;

    @BeforeEach
    void setUp() {
        userId = 1L;
        mockUser = new User();
        mockUser.setId(userId);
    }

    @AfterEach
    void tearDown() {
        // Any cleanup can be done here
        reset(userRepository); // Reset the mock
    }

    @Test
    void getUser() {
        // Define the behavior of the userRepository mock
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Test the getUser method
        User user = userService.getUser(userId);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userId);

        // Test the case when the user is not found
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.getUser(userId))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void save() {
        User user = new User();

        // Call the save method
        userService.save(user);

        // Verify that the save method of the repository was called with the correct user
        verify(userRepository, times(1)).save(user);
    }
}