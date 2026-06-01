package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DuplicateException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final UserMapper userMapper;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  @Transactional
  public UserDto create(UserCreateRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("user is null.");
    }

    if (!isUniqueUsername(request.username())) {
      throw new DuplicateException("username is duplicate.");
    }

    if (!isUniqueEmail(request.email())) {
      throw new DuplicateException("email is duplicate.");
    }

    BinaryContent binaryContent = createProfile(request.profileImage());

    User user = userMapper.toEntity(request, binaryContent);
    UserStatus userStatus = new UserStatus(user, Instant.now());
    user.setStatus(userStatus);

    User savedUser = userRepository.save(user);

    return userMapper.toDto(savedUser);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto findDetailById(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("id is null.");
    }

    User user = userRepository.findDetailById(userId)
        .orElseThrow(() -> new NotFoundException("user not found."));

    return userMapper.toDto(user);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAllWithFetch() {
    return userRepository.findAllWithFetch().stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public UserDto update(UUID userId, UserUpdateRequest request) {
    if (userId == null) {
      throw new IllegalArgumentException("userId is null.");
    }
    if (request == null) {
      throw new IllegalArgumentException("userUpdateRequest is null.");
    }

    User user = userRepository.findDetailById(userId)
        .orElseThrow(() -> new NotFoundException("user not found."));

    if (request.newUsername() != null) {
      if (!Objects.equals(request.newUsername(), user.getUsername())
          && !isUniqueUsername(request.newUsername())) {
        throw new DuplicateException("username is duplicate.");
      }
      user.setUsername(request.newUsername());
    }
    if (request.newEmail() != null) {
      if (!Objects.equals(request.newEmail(), user.getEmail())
          && !isUniqueEmail(request.newEmail())) {
        throw new DuplicateException("email is duplicate.");
      }
      user.setEmail(request.newEmail());
    }

    if (request.newPassword() != null) {
      user.setPassword(request.newPassword());
    }

    BinaryContent oldProfileImage = null;
    if (request.profileImage() != null) {
      oldProfileImage = user.getProfile();

      BinaryContent newProfileImage = createProfile(request.profileImage());
      user.setProfile(newProfileImage);
    }

    User savedUser = userRepository.save(user);
    if (oldProfileImage != null) {
      binaryContentRepository.delete(oldProfileImage);
    }

    return userMapper.toDto(savedUser);
  }

  @Override
  @Transactional
  public void delete(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("userId is null.");
    }

    User user = userRepository.findDetailById(userId)
        .orElseThrow(() -> new NotFoundException("user not found."));

    BinaryContent profile = user.getProfile();

    userRepository.delete(user);

    if (profile != null) {
      binaryContentRepository.delete(profile);
    }
  }

  private boolean isUniqueUsername(String username) {
    if (username == null) {
      throw new IllegalArgumentException("username is null.");
    }
    return !userRepository.existsByUsername(username);
  }

  private boolean isUniqueEmail(String email) {
    if (email == null) {
      throw new IllegalArgumentException("email is null.");
    }

    return !userRepository.existsByEmail(email);
  }

  private BinaryContent createProfile(BinaryContentCreateRequest profileImage) {
    BinaryContent binaryContent = null;

    if (profileImage != null) {
      binaryContent = binaryContentRepository.save(binaryContentMapper.toEntity(profileImage));
      binaryContentStorage.put(binaryContent.getId(), profileImage.bytes());
    }

    return binaryContent;
  }
}