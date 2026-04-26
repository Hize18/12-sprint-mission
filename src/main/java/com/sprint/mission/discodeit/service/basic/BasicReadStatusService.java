package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponse;
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
  public ReadStatusResponse create(ReadStatusCreateRequest request) {
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

    ReadStatus status = new ReadStatus(request.userId(), request.channelId());

    readStatusRepository.save(status);
    return ReadStatusResponse.from(status);
  }

  @Override
  public ReadStatusResponse findById(UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("id is null.");
    }

    return ReadStatusResponse.from(
        readStatusRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("readStatus not found."))
    );
  }

  @Override
  public ReadStatusResponse findByUserIdAndChannelId(UUID userId, UUID channelId) {
    if (userId == null) {
      throw new IllegalArgumentException("userId is null.");
    }
    if (channelId == null) {
      throw new IllegalArgumentException("channelId is null.");
    }

    return ReadStatusResponse.from(
        readStatusRepository.findByUserIdAndChannelId(userId, channelId)
            .orElseThrow(() -> new NoSuchElementException("readStatus nof found."))
    );
  }

  @Override
  public List<ReadStatusResponse> findAllByUserId(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("userId is null.");
    }

    return readStatusRepository.findByUserId(userId).stream()
        .map(ReadStatusResponse::from)
        .toList();
  }

  @Override
  public ReadStatusResponse update(UUID userId, UUID channelId) {
    if (userId == null) {
      throw new IllegalArgumentException("userId is null.");
    }
    if (channelId == null) {
      throw new IllegalArgumentException("channelId is null.");
    }

    ReadStatus status = readStatusRepository.findByUserIdAndChannelId(userId, channelId)
        .orElseThrow(() -> new NoSuchElementException("readStatus not found."));

    status.updateTime();
    return ReadStatusResponse.from(
        readStatusRepository.save(status)
    );
  }

  @Override
  public void delete(UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("id is null.");
    }

    if (!readStatusRepository.existsById(id)) {
      throw new NoSuchElementException("readStatus not found.");
    }
    readStatusRepository.delete(id);
  }
}
