package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdate;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository rsr;
    private final UserRepository ur;
    private final ChannelRepository cr;

    @Override
    public ReadStatusResponse create(ReadStatusCreateRequest request) {
        if(request == null) {throw new IllegalArgumentException("request is null.");}

        if(ur.findById(request.userId()).isEmpty()) throw new NoSuchElementException("user not found.");
        if(cr.findById(request.channelId()).isEmpty()) throw new NoSuchElementException("channel not found.");

        if(rsr.findByUserIdAndChannelId(request.userId(), request.channelId()).isPresent()) throw new IllegalStateException("readStatus already exists.");

        ReadStatus status = new ReadStatus(request.userId(), request.channelId());

        rsr.save(status);
        return ReadStatusResponse.from(status);
    }

    @Override
    public ReadStatusResponse findById(UUID id) {
        if(id == null) throw new IllegalArgumentException("id is null.");

        return ReadStatusResponse.from(
                rsr.findById(id).orElseThrow(() -> new NoSuchElementException("readStatus not found."))
        );
    }

    @Override
    public List<ReadStatusResponse> findAllByUserId(UUID userId) {
        if(userId == null) throw new IllegalArgumentException("userId is null.");

        return rsr.findByUserId(userId).stream()
                .map(ReadStatusResponse::from)
                .toList();
    }

    @Override
    public void update(ReadStatusUpdate request) {
        if(request == null) throw new IllegalArgumentException("request is null.");

        ReadStatus status = rsr.findByUserIdAndChannelId(request.userId(), request.channelId())
                .orElseThrow(() -> new NoSuchElementException("readStatus not found."));

        status.updateTime();
        rsr.save(status);
    }

    @Override
    public boolean delete(UUID id) {
        if(id == null) throw new IllegalArgumentException("id is null.");

        rsr.findById(id).orElseThrow(() -> new NoSuchElementException("readStatus not found."));
        rsr.delete(id);
        return true;
    }
}
