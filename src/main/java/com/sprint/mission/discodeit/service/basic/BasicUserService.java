package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {
    private final UserRepository ur;

    public BasicUserService(UserRepository userRepository) {
        this.ur = userRepository;
    }

    @Override
    public boolean isUniqueUsername(String username) {
        if(username == null) throw new IllegalArgumentException("username is null.");

        for (User user : ur.findAll()) {
            if(user.getUsername().equals(username)) return false;
        }
        return true;
    }

    @Override
    public boolean isUniqueEmail(String email) {
        if(email == null) throw new IllegalArgumentException("email is null.");

        for (User user : ur.findAll()) {
            if(user.getEmail().equals(email)) return false;
        }
        return true;
    }

    @Override
    public User save(User user) {
//        Objects.requireNonNull(user, "user is null.");
        if (user == null) throw new IllegalArgumentException("user is null.");
        if (!isUniqueUsername(user.getUsername())) throw new IllegalStateException("username is duplicate.");
        if (!isUniqueEmail(user.getEmail())) throw new IllegalStateException("email is duplicate.");

        return ur.save(user);
    }

    @Override
    public User findById(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is null.");

        return ur.findById(id);
    }

    @Override
    public User findByUsername(String username) {
        if(username == null) throw new IllegalArgumentException("username is null.");

        return ur.findByUsername(username);
    }

    @Override
    public List<User> findByNickname(String nickname) {
        if(nickname == null) throw new IllegalArgumentException("nickname is null.");

        return ur.findByNickname(nickname);
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
        if (loginUser == null) throw new IllegalStateException("src user not found.");

        if (!srcUserId.equals(dstUserId)) return false;

        User tempUser = new User(loginUser);

        String username = loginUser.getUsername();
        String email = loginUser.getEmail();
        String password = loginUser.getPassword();
        String nickname = loginUser.getNickname();

        if (userData.getUsername() != null && !userData.getUsername().isEmpty()) {
            if (!isUniqueUsername(userData.getUsername()) &&
                    !userData.getUsername().equals(loginUser.getUsername())) {
                throw new IllegalStateException("username is duplicate.");
            }
            username = userData.getUsername();
        }

        if (userData.getEmail() != null && !userData.getEmail().isEmpty()) {
            if (!isUniqueEmail(userData.getEmail()) &&
                    !userData.getEmail().equals(loginUser.getEmail())) {
                throw new IllegalStateException("email is duplicate.");
            }
            email = userData.getEmail();
        }

        if (userData.getPassword() != null && !userData.getPassword().isEmpty()) {
            password = userData.getPassword();
        }

        if (userData.getNickname() != null && !userData.getNickname().isEmpty()) {
            nickname = userData.getNickname();
        }

        tempUser.update(username, email, password, nickname);
        ur.save(tempUser);
        return true;
    }

//    따로 메서드 만드는게 좋음.
    public boolean updatePassword(UUID srcUserId, UUID dstUserId, String password){
        User srcUser = findById(srcUserId);
        if(srcUser == null) throw new IllegalStateException("src user not found.");

        User tempUser = new User(srcUser);
        tempUser.update(tempUser.getUsername(), tempUser.getEmail(), password, tempUser.getNickname());

        return update(srcUserId,dstUserId, tempUser);
    }

    @Override
    public boolean delete(UUID srcUserId, UUID dstUserId) {
        if (srcUserId == null) throw new IllegalArgumentException("srcUserId is null.");
        if (dstUserId == null) throw new IllegalArgumentException("dstUserId is null.");

        User loginUser = findById(srcUserId);
        if (loginUser == null) throw new IllegalStateException("src user not found.");

        if (!srcUserId.equals(dstUserId)) return false;

        ur.delete(dstUserId);
        return true;
    }
}