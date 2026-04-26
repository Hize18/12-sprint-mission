package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
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
  public UserResponse create(UserCreateRequest request) {
    validateCreateUser(request);

    UUID profileImageId = checkProfileImageId(request.profileImage());

    User user = new User(
        request.username(),
        request.email(),
        request.password(),
        profileImageId
    );
    userRepository.save(user);

    UserStatus userStatus = new UserStatus(user.getId());
    userStatusRepository.save(userStatus);

    return toResponse(user);
  }

  @Override
  public UserResponse findById(UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("id is null.");
    }

    return userRepository.findById(id)
        .map(this::toResponse)
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
  public UserResponse findByUsername(String username) {
    if (username == null) {
      throw new IllegalArgumentException("username is null.");
    }

    return userRepository.findByUsername(username)
        .map(this::toResponse)
        .orElseThrow(() -> new NoSuchElementException("user not found."));
  }

  @Override
  public List<UserResponse> findAll() {
    return userRepository.findAll().stream()
        .map(this::toResponse)
        .toList();
  }

  @Override
  public UserResponse update(UUID srcUserId, UUID dstUserId, UserUpdateRequest request) {
    if (srcUserId == null) {
      throw new IllegalArgumentException("srcUserId is null.");
    }
    if (dstUserId == null) {
      throw new IllegalArgumentException("dstUserId is null.");
    }
    if (request == null) {
      throw new IllegalArgumentException("userUpdateRequest is null.");
    }

    if (!Objects.equals(srcUserId, dstUserId)) {
      throw new IllegalStateException("no permission");
    }

    User targetUser = findEntityById(dstUserId);

    User tempUser = User.copyOf(targetUser);

    String username = targetUser.getUsername();
    String email = targetUser.getEmail();
    String password = targetUser.getPassword();
    UUID profileImageId = targetUser.getProfileId();

    if (request.username() != null) {
      validateDuplicateUsername(request.username(), targetUser.getUsername());
      username = request.username();
    }

    if (request.email() != null) {
      validateDuplicateEmail(request.email(), targetUser.getEmail());
      email = request.email();
    }

    if (request.password() != null) {
      password = request.password();
    }

    if (request.profileImage() != null) {
      UUID oldProfileImageId = profileImageId;

      profileImageId = checkProfileImageId(request.profileImage());

      if (oldProfileImageId != null) {
        binaryContentRepository.delete(oldProfileImageId);
      }
    }

    tempUser.update(username, email, password, profileImageId);
    return toResponse(userRepository.save(tempUser));
  }

  @Override
  public void delete(UUID srcUserId, UUID dstUserId) {
    if (srcUserId == null) {
      throw new IllegalArgumentException("srcUserId is null.");
    }
    if (dstUserId == null) {
      throw new IllegalArgumentException("dstUserId is null.");
    }

    if (!Objects.equals(srcUserId, dstUserId)) {
      throw new IllegalStateException("no permission");
    }

    User targetUser = findEntityById(dstUserId);

    UserStatus userStatus = userStatusRepository.findByUserId(targetUser.getId())
        .orElseThrow(() -> new NoSuchElementException("userStatus not found."));

    UUID profileImageId = targetUser.getProfileId();
    if (profileImageId != null) {
      binaryContentRepository.delete(profileImageId);
    }

    userRepository.delete(dstUserId);
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

  private UserResponse toResponse(User user) {
    boolean online = userStatusRepository.findByUserId(user.getId())
        .map(UserStatus::isActive)
        .orElseThrow(() -> new NoSuchElementException("userStatus not found."));

    return UserResponse.from(user, online);
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