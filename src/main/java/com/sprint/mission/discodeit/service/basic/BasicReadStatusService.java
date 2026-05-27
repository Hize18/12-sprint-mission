package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DuplicateException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;

  @Override
  @Transactional
  public ReadStatusDto create(ReadStatusCreateRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("request is null.");
    }

    if (readStatusRepository.existsByUserIdAndChannelId(request.userId(), request.channelId())) {
      throw new DuplicateException("readStatus already exist.");
    }

    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new NotFoundException("user not found"));

    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> new NotFoundException("channel not found"));

    ReadStatus readStatus = readStatusMapper.toEntity(request, user, channel);
    return readStatusMapper.toDto(readStatusRepository.save(readStatus));
  }

  @Override
  @Transactional(readOnly = true)
  public ReadStatusDto findById(UUID readStatusId) {
    if (readStatusId == null) {
      throw new IllegalArgumentException("readStatusId is null.");
    }

    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new NotFoundException("readStatus not found."));
    return readStatusMapper.toDto(readStatus);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("userId is null.");
    }

    return readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
    if (readStatusId == null) {
      throw new IllegalArgumentException("readStatusId is null.");
    }
    if (request == null) {
      throw new IllegalArgumentException("request is null.");
    }

    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new NotFoundException("readStatus not found."));

//    굳이 update가 필요한지, 시간을 현재가 아니라 임의의 시간을 하려고?
    readStatus.setLastReadAt(request.newLastReadAt());
    return readStatusMapper.toDto(readStatus);
  }

  @Override
  @Transactional
  public void delete(UUID readStatusId) {
    if (readStatusId == null) {
      throw new IllegalArgumentException("id is null.");
    }

    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new NotFoundException("readStatus not found."));

    readStatusRepository.delete(readStatus);
  }
}
