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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class StartServiceTest {

    @InjectMocks
    private StartService startService;

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
        Mockito.reset(userRepository); // Reset the mock
    }

    @Test
    void addUserIfNotInRepo_userNotInRepo_savesUserAndReturnsFalse()  {

        long chatId = 1L;
        when(userRepository.existsById(chatId)).thenReturn(false);

        boolean result = startService.addUserIfNotInRepo(chatId);

        Assertions.assertThat(result).isFalse();
        verify(userRepository, Mockito.times(1)).save(mockUser);
    }

    @Test
    void addUserIfNotInRepo_userInRepo_returnsTrue()  {
        long chatId = 1L;
        when(userRepository.existsById(chatId)).thenReturn(true);

        boolean result = startService.addUserIfNotInRepo(chatId);

        Assertions.assertThat(result).isTrue();
        verify(userRepository, never()).save(mockUser);
    }

    @Test
    void createWelcomeAndGuideMessages_validInput_returnsMessages() {
        long chatId = 1L;
        String userFirstName = "test";

        List<SendMessage> messages = startService.createWelcomeAndGuideMessages(chatId, userFirstName);

        Assertions.assertThat(messages).isNotNull();
        Assertions.assertThat(messages).hasSize(2);
        Assertions.assertThat(messages.get(0).getText()).contains(userFirstName);
        Assertions.assertThat(messages.get(1).getText()).isEqualTo("GUIDE_MESSAGE");
    }

    @Test
    void createWelcomeMessageWithGetGuideButton_validInput_returnsMessage() {
        long chatId = 1L;
        String userFirstName = "test";

        List<SendMessage> messages = startService.createWelcomeAndGuideMessages(chatId, userFirstName);

        Assertions.assertThat(messages).isNotNull();
        Assertions.assertThat(messages).hasSize(2);
        Assertions.assertThat(messages.get(0).getText()).contains(userFirstName);
        Assertions.assertThat(messages.get(0).getReplyMarkup()).isNotNull();
    }

    @Test
    void createGuideMessage() {
        long chatId = 1L;

        SendMessage message = startService.createGuideMessage(chatId);

        Assertions.assertThat(message).isNotNull();
        Assertions.assertThat(message.getText()).isEqualTo("GUIDE_MESSAGE");
    }
}