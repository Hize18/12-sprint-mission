package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  public ReadStatus create(ReadStatusCreateRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("request is null.");
    }

    if (!userRepository.existsById(request.userId())) {
      throw new NoSuchElementException("user not found.");
    }

    if (!channelRepository.existsById(request.channelId())) {
      throw new NoSuchElementException("channel not found.");
    }

    if (readStatusRepository.findByUserIdAndChannelId(request.userId(), request.channelId())
        .isPresent()) {
      throw new IllegalStateException("readStatus already exists.");
    }

    ReadStatus status = new ReadStatus(request.userId(), request.channelId(), request.lastReadAt());

    return readStatusRepository.save(status);
  }

  @Override
  public ReadStatus findById(UUID readStatusId) {
    if (readStatusId == null) {
      throw new IllegalArgumentException("id is null.");
    }

    return readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new NoSuchElementException("readStatus not found."));
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("userId is null.");
    }

    return readStatusRepository.findByUserId(userId);
  }

  @Override
  public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request) {
    if (readStatusId == null) {
      throw new IllegalArgumentException("readStatusId is null.");
    }
    if (request == null) {
      throw new IllegalArgumentException("request is null.");
    }

    ReadStatus status = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new NoSuchElementException("readStatus not found."));

    status.updateTime(request.newLastReadAt());
    return readStatusRepository.save(status);
  }

  @Override
  public void delete(UUID readStatusId) {
    if (readStatusId == null) {
      throw new IllegalArgumentException("id is null.");
    }

    if (!readStatusRepository.existsById(readStatusId)) {
      throw new NoSuchElementException("readStatus not found.");
    }
    readStatusRepository.delete(readStatusId);
  }
}
