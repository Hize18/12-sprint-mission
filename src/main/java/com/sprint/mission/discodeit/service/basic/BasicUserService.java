package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public User create(UserCreateRequest request) {
    validateCreateUser(request);

    UUID profileImageId = checkProfileImageId(request.profileImage());

    User user = new User(
        request.username(),
        request.email(),
        request.password(),
        profileImageId
    );
    userRepository.save(user);

    UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
    userStatusRepository.save(userStatus);

    return user;
  }

  @Override
  public User findById(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("id is null.");
    }

    return userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("user not found."));
  }

  private User findEntityById(UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("id is null.");
    }

    return userRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("user not found."));
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll().stream()
        .map(this::toResponse)
        .toList();
  }

  @Override
  public User update(UUID userId, UserUpdateRequest request) {
    if (userId == null) {
      throw new IllegalArgumentException("userId is null.");
    }
    if (request == null) {
      throw new IllegalArgumentException("userUpdateRequest is null.");
    }

    User targetUser = findEntityById(userId);

    User tempUser = User.copyOf(targetUser);

    String username = targetUser.getUsername();
    String email = targetUser.getEmail();
    String password = targetUser.getPassword();
    UUID profileImageId = targetUser.getProfileId();

    if (request.newUsername() != null) {
      validateDuplicateUsername(request.newUsername(), targetUser.getUsername());
      username = request.newUsername();
    }

    if (request.newEmail() != null) {
      validateDuplicateEmail(request.newEmail(), targetUser.getEmail());
      email = request.newEmail();
    }

    if (request.newPassword() != null) {
      password = request.newPassword();
    }

    if (request.profileImage() != null) {
      UUID oldProfileImageId = profileImageId;

      profileImageId = checkProfileImageId(request.profileImage());

      if (oldProfileImageId != null) {
        binaryContentRepository.delete(oldProfileImageId);
      }
    }

    tempUser.update(username, email, password, profileImageId);
    return userRepository.save(tempUser);
  }

  @Override
  public void delete(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("userId is null.");
    }

    User targetUser = findEntityById(userId);

    UserStatus userStatus = userStatusRepository.findByUserId(targetUser.getId())
        .orElseThrow(() -> new NoSuchElementException("userStatus not found."));

    UUID profileImageId = targetUser.getProfileId();
    if (profileImageId != null) {
      binaryContentRepository.delete(profileImageId);
    }

    userRepository.delete(userId);
    userStatusRepository.delete(userStatus.getId());
  }

  private boolean isUniqueUsername(String username) {
    if (username == null) {
      throw new IllegalArgumentException("username is null.");
    }

    return userRepository.findAll().stream()
        .noneMatch(user -> Objects.equals(user.getUsername(), username));
  }

  private boolean isUniqueEmail(String email) {
    if (email == null) {
      throw new IllegalArgumentException("email is null.");
    }

    return userRepository.findAll().stream()
        .noneMatch(user -> Objects.equals(user.getEmail(), email));
  }

  private UUID checkProfileImageId(BinaryContentCreateRequest profileImage) {
    UUID profileImageId = null;

    if (profileImage != null) {
      BinaryContent bc = new BinaryContent(
          profileImage.fileName(),
          profileImage.contentType(),
          profileImage.bytes()
      );
      binaryContentRepository.save(bc);
      profileImageId = bc.getId();
    }
    return profileImageId;
  }

  private UserDto toResponse(User user) {
    boolean online = userStatusRepository.findByUserId(user.getId())
        .map(UserStatus::isActive)
        .orElseThrow(() -> new NoSuchElementException("userStatus not found."));

    return UserDto.from(user, online);
  }

  private void validateCreateUser(UserCreateRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("user is null.");
    }

    if (!isUniqueUsername(request.username())) {
      throw new IllegalStateException("username is duplicate.");
    }
    if (!isUniqueEmail(request.email())) {
      throw new IllegalStateException("email is duplicate.");
    }
  }

  private void validateDuplicateUsername(String newUsername, String oldUsername) {
    boolean isChangedUsername = !Objects.equals(newUsername, oldUsername);
    if (isChangedUsername && !isUniqueUsername(newUsername)) {
      throw new IllegalStateException("username is duplicate.");
    }
  }

  private void validateDuplicateEmail(String newEmail, String oldEmail) {
    boolean isChangedEmail = !Objects.equals(newEmail, oldEmail);
    if (isChangedEmail && !isUniqueEmail(newEmail)) {
      throw new IllegalStateException("email is duplicate.");
    }
  }
}