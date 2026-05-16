package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DuplicateException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final ChannelMapper channelMapper;
  private final MessageRepository messageRepository;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public ChannelDto create(PublicChannelCreateRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("request is null.");
    }

    if (!isUniqueName(request.name())) {
      throw new DuplicateException("name is duplicate.");
    }

    Channel channel = channelMapper.toEntity(request);
    return toDto(channelRepository.save(channel));
  }

  @Override
  @Transactional
  public ChannelDto create(PrivateChannelCreateRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("request is null.");
    }

    Channel channel = new Channel(null, null, ChannelType.PRIVATE);
    Channel savedChannel = channelRepository.save(channel);

    List<User> participants = userRepository.findAllById(request.participantIds());
    if (participants.size() != request.participantIds().size()) {
      throw new NotFoundException("user not found.");
    }

    List<ReadStatus> readStatuses = participants.stream()
        .map(user -> new ReadStatus(user, savedChannel, savedChannel.getCreatedAt()))
        .toList();

    readStatusRepository.saveAll(readStatuses);

    return toDto(savedChannel);
  }

  @Override
  @Transactional(readOnly = true)
  public ChannelDto findById(UUID channelId) {
    if (channelId == null) {
      throw new IllegalArgumentException("channelId is null.");
    }

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NotFoundException("channel not found."));
    return toDto(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelDto> findAllByUserId(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("userId is null.");
    }

    List<Channel> channels = channelRepository
        .findAllByUserIdAndPublicChannelsWithParticipants(userId);

    List<UUID> channelIds = channels.stream()
        .map(Channel::getId)
        .toList();

    if (channelIds.isEmpty()) {
      return List.of();
    }

    Map<UUID, Instant> lastMessageMap = new HashMap<>();
    messageRepository.findLastMessageAtByChannelIds(channelIds)
        .forEach(obj -> lastMessageMap.put((UUID) obj[0], (Instant) obj[1]));

    return channels.stream()
        .map(channel -> {
          List<UserDto> participants = channel.getReadStatuses().stream()
              .map(ReadStatus::getUser)
              .map(userMapper::toDto)
              .toList();
          return channelMapper.toDto(channel, lastMessageMap.get(channel.getId()), participants);
        })
        .toList();
  }

  @Override
  @Transactional
  public ChannelDto update(UUID channelId, ChannelUpdateRequest request) {
    if (channelId == null) {
      throw new IllegalArgumentException("channelId is null.");
    }
    if (request == null) {
      throw new IllegalArgumentException("channelUpdateRequest is null.");
    }

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NotFoundException("channel not found."));

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalStateException("private channel cannot be updated.");
    }

    if (!Objects.equals(channel.getName(), request.newName())
        && !isUniqueName(request.newName())) {
      throw new DuplicateException("name is duplicate.");
    }

    channel.setName(request.newName());
    channel.setDescription(request.newDescription());
    return toDto(channel);
  }

  @Override
  @Transactional
  public void delete(UUID channelId) {
    if (channelId == null) {
      throw new IllegalArgumentException("channelId is null.");
    }

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NotFoundException("channel not found."));

    channelRepository.delete(channel);
  }

  private boolean isUniqueName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("name is null.");
    }

    return !channelRepository.existsByName(name);
  }

  private ChannelDto toDto(Channel channel) {
    Instant lastMessageAt = messageRepository
        .findTopByChannelIdOrderByCreatedAtDesc(channel.getId())
        .map(Message::getCreatedAt)
        .orElse(null);

    List<UserDto> participants = readStatusRepository
        .findAllByChannelId(channel.getId()).stream()
        .map(ReadStatus::getUser)
        .map(userMapper::toDto)
        .toList();

    return channelMapper.toDto(channel, lastMessageAt, participants);
  }
}
