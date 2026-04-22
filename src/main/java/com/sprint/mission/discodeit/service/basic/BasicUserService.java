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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository ur;
    private final BinaryContentRepository bcr;
    private final UserStatusRepository usr;

    @Override
    public boolean isUniqueUsername(String username) {
        if (username == null) throw new IllegalArgumentException("username is null.");

        return ur.findAll().stream()
                .noneMatch(user -> Objects.equals(user.getUsername(), username));
    }

    @Override
    public boolean isUniqueEmail(String email) {
        if (email == null) throw new IllegalArgumentException("email is null.");

        return ur.findAll().stream()
                .noneMatch(user -> Objects.equals(user.getEmail(), email));
    }

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
        ur.save(user);

        UserStatus userStatus = new UserStatus(user.getId());
        usr.save(userStatus);

        return UserResponse.from(user);
    }

    @Override
    public UserResponse findById(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is null.");

        return UserResponse.from(
                ur.findById(id).orElseThrow(() -> new NoSuchElementException("user not found."))
        );
    }

    private User findEntityById(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is null.");

        return ur.findById(id).orElseThrow(() -> new NoSuchElementException("user not found."));
    }

    @Override
    public UserResponse findByUsername(String username) {
        if (username == null) throw new IllegalArgumentException("username is null.");

        return UserResponse.from(
                ur.findByUsername(username).orElseThrow(() -> new NoSuchElementException("user not found."))
        );
    }

    @Override
    public List<UserResponse> findAll() {
        return ur.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }

    @Override
    public boolean update(UUID srcUserId, UUID dstUserId, UserUpdateRequest request) {
        if (srcUserId == null) throw new IllegalArgumentException("srcUserId is null.");
        if (dstUserId == null) throw new IllegalArgumentException("dstUserId is null.");
        if (request == null) throw new IllegalArgumentException("userUpdateRequest is null.");

        if (!Objects.equals(srcUserId, dstUserId)) return false;

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

        if(request.profileImage() != null) {
            UUID oldProfileImageId = profileImageId;

            profileImageId = checkProfileImageId(request.profileImage());

            if (oldProfileImageId != null) {
                bcr.delete(oldProfileImageId);
            }
        }

        tempUser.update(username, email, password, profileImageId);
        ur.save(tempUser);
        return true;
    }

    @Override
    public boolean delete(UUID srcUserId, UUID dstUserId) {
        if (srcUserId == null) throw new IllegalArgumentException("srcUserId is null.");
        if (dstUserId == null) throw new IllegalArgumentException("dstUserId is null.");

        if (!Objects.equals(srcUserId, dstUserId)) return false;

        User targetUser = findEntityById(dstUserId);

        UserStatus userStatus = usr.findByUserId(targetUser.getId())
                .orElseThrow(() -> new NoSuchElementException("userStatus not found."));

        UUID profileImageId = targetUser.getProfileId();
        if (profileImageId != null) {
            bcr.delete(profileImageId);
        }

        ur.delete(dstUserId);
        usr.delete(userStatus.getId());
        return true;
    }

    private UUID checkProfileImageId(BinaryContentCreateRequest profileImage) {
        UUID profileImageId = null;

        if(profileImage != null){
            BinaryContent bc = new BinaryContent(
                    profileImage.fileName(),
                    profileImage.contentType(),
                    profileImage.data()
            );
            bcr.save(bc);
            profileImageId = bc.getId();
        }
        return profileImageId;
    }

    private void validateCreateUser(UserCreateRequest request) {
        if (request == null) throw new IllegalArgumentException("user is null.");

        if (!isUniqueUsername(request.username())) throw new IllegalStateException("username is duplicate.");
        if (!isUniqueEmail(request.email())) throw new IllegalStateException("email is duplicate.");
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