package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreate;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponse;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdate;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository usr;
    private final UserRepository ur;

    @Override
    public UserStatusResponse create(UserStatusCreate request) {
        if (request == null) throw new IllegalArgumentException("request is null.");

        if(ur.findById(request.userId()).isEmpty()) throw new NoSuchElementException("user not found.");

        if(usr.findByUserId(request.userId()).isPresent()) throw new IllegalStateException("userStatus already exists.");

        UserStatus usrStatus = new UserStatus(request.userId());
        usr.save(usrStatus);
        return UserStatusResponse.from(usrStatus);
    }

    @Override
    public UserStatusResponse findById(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is null.");

        return UserStatusResponse.from(
                usr.findById(id).orElseThrow(() -> new NoSuchElementException("userStatus not found."))
        );
    }

    @Override
    public List<UserStatusResponse> findAll() {
        return usr.findAll().stream()
                .map(UserStatusResponse::from)
                .toList();
    }

    @Override
    public boolean update(UserStatusUpdate request) {
        if (request == null) throw new IllegalArgumentException("request is null.");

        UserStatus usrStatus = usr.findById(request.id())
                .orElseThrow(() -> new NoSuchElementException("userStatus not found."));

        usrStatus.update();
        usr.save(usrStatus);
        return true;
    }

    @Override
    public boolean updateByUserId(UUID userId) {
        if (userId == null) throw new IllegalArgumentException("userId is null.");

        UserStatus usrStatus = usr.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("userStatus not found."));

        usrStatus.update();
        usr.save(usrStatus);
        return true;
    }

    @Override
    public boolean delete(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is null.");

        usr.findById(id).orElseThrow(() -> new NoSuchElementException("userStatus not found."));
        usr.delete(id);
        return true;
    }
}
