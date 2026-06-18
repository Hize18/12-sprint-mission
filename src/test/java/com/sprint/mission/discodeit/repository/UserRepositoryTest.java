package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.sprint.mission.discodeit.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
//@EnableJpaAuditing
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  private User createUser(String username, String email, String password) {
    return User.builder()
        .username(username)
        .email(email)
        .password(password)
        .build();
  }

  @Test
  @DisplayName("findByUsername_success")
  void findByUsername_success() {
    userRepository.save(createUser("test_username",
        "test_email",
        "test_password")
    );
    testEntityManager.flush();
    testEntityManager.clear();

    Optional<User> user = userRepository.findByUsername("test_username");

    assertThat(user).isPresent();
    assertThat(user.get().getUsername()).isEqualTo("test_username");
    assertThat(user.get().getEmail()).isEqualTo("test_email");
    assertThat(user.get().getPassword()).isEqualTo("test_password");
  }

  @Test
  @DisplayName("findByUsername_failed")
  void findByUsername_failed() {
    assertThat(userRepository.findByUsername("no_user")).isEmpty();
  }

  @Test
  @DisplayName("findDetailById_success")
  void findDetailById_success() {
    User createdUser = userRepository.save(createUser("test_username",
        "test_email",
        "test_password")
    );
    testEntityManager.flush();
    testEntityManager.clear();

    Optional<User> user = userRepository.findDetailById(createdUser.getId());

    assertThat(user).isPresent();
    assertThat(user.get().getUsername()).isEqualTo("test_username");
    assertThat(user.get().getEmail()).isEqualTo("test_email");
    assertThat(user.get().getPassword()).isEqualTo("test_password");
  }

  @Test
  @DisplayName("findDetailById_failed")
  void findDetailById_failed() {
    assertThat(userRepository.findDetailById(UUID.randomUUID()).isEmpty());
  }
}
