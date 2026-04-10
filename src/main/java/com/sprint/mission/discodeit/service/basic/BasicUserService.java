package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {
    private final UserRepository ur;

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
    public User save(User user) {
//        Objects.requireNonNull(user, "user is null.");
        if (user == null) throw new IllegalArgumentException("user is null.");

        if (user.getUsername() == null) throw new IllegalArgumentException("username is null.");
        if (user.getEmail() == null) throw new IllegalArgumentException("email is null.");
        if (user.getPassword() == null) throw new IllegalArgumentException("password is null.");

        if (user.getUsername().isBlank()) throw new IllegalArgumentException("username is blank.");
        if (user.getEmail().isBlank()) throw new IllegalArgumentException("email is blank.");
        if (user.getPassword().isBlank()) throw new IllegalArgumentException("password is blank.");

        if (!isUniqueUsername(user.getUsername())) throw new IllegalStateException("username is duplicate.");
        if (!isUniqueEmail(user.getEmail())) throw new IllegalStateException("email is duplicate.");

        return ur.save(user);
    }

    @Override
    public User findById(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is null.");

        return ur.findById(id).orElseThrow(() -> new NoSuchElementException("user not found."));
    }

    @Override
    public User findByUsername(String username) {
        if (username == null) throw new IllegalArgumentException("username is null.");

        return ur.findByUsername(username).orElseThrow(() -> new NoSuchElementException("user not found."));
    }

    @Override
    public List<User> findAll() {
        return ur.findAll();
    }

    @Override
    public boolean update(UUID srcUserId, UUID dstUserId, User userData) {
        if (srcUserId == null) throw new IllegalArgumentException("srcUserId is null.");
        if (dstUserId == null) throw new IllegalArgumentException("dstUserId is null.");
        if (userData == null) throw new IllegalArgumentException("userData is null.");

        User loginUser = findById(srcUserId);

        if (!Objects.equals(srcUserId, dstUserId)) return false;

        User tempUser = User.copyOf(loginUser);

        String username = loginUser.getUsername();
        String email = loginUser.getEmail();
        String password = loginUser.getPassword();

        if (userData.getUsername() != null && !userData.getUsername().isEmpty()) {
            if (!isUniqueUsername(userData.getUsername()) &&
                    !Objects.equals(userData.getUsername(), loginUser.getUsername())) {
                throw new IllegalStateException("username is duplicate.");
            }
            username = userData.getUsername();
        }

        if (userData.getEmail() != null && !userData.getEmail().isEmpty()) {
            if (!isUniqueEmail(userData.getEmail()) &&
                    !Objects.equals(userData.getEmail(), loginUser.getEmail())) {
                throw new IllegalStateException("email is duplicate.");
            }
            email = userData.getEmail();
        }

        if (userData.getPassword() != null && !userData.getPassword().isEmpty()) {
            password = userData.getPassword();
        }

        tempUser.update(username, email, password);
        ur.save(tempUser);
        return true;
    }

    @Override
    public boolean delete(UUID srcUserId, UUID dstUserId) {
        if (srcUserId == null) throw new IllegalArgumentException("srcUserId is null.");
        if (dstUserId == null) throw new IllegalArgumentException("dstUserId is null.");

        findById(srcUserId);

        if (!Objects.equals(srcUserId, dstUserId)) return false;

        ur.delete(dstUserId);
        return true;
    }
}