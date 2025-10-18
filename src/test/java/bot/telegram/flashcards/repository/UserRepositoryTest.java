package bot.telegram.flashcards.repository;

import bot.telegram.flashcards.models.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void add_user_into_user_repository() {

        User user = User.builder()
                        .id(1L)
                        .build();

        User savedUser = userRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    void get_all_users_from_user_repository() {

        User user = User.builder()
                        .id(1L)
                        .build();

        User user2 = User.builder()
                .id(2L)
                .build();

        userRepository.save(user);
        userRepository.save(user2);

        List<User> userList = (List<User>) userRepository.findAll();

        Assertions.assertThat(userList).isNotNull();
        Assertions.assertThat(userList.size()).isEqualTo(3);
    }

    @Test
    void show_if_user_in_user_repository_is_not_null() {
        User user = User.builder()
                .id(1L)
                .build();

        userRepository.save(user);

        User userId = userRepository.findById(user.getId()).get();

        Assertions.assertThat(userId).isNotNull();
    }

    @Test
    void take_user_from_user_repository_by_id() {
        User user = User.builder()
                .id(1L)
                .build();

        userRepository.save(user);

        User userId = userRepository.findById(user.getId()).get();

        Assertions.assertThat(userId).isNotNull();
        Assertions.assertThat(userId.getId()).isEqualTo(1L);
    }

    @Test
    void delete_user_from_user_repository() {
        User user = User.builder()
                .id(1L)
                .build();

        userRepository.save(user);

        userRepository.delete(user);

        User deletedUser = userRepository.findById(user.getId()).orElse(null);

        Assertions.assertThat(deletedUser).isNull();
    }
}