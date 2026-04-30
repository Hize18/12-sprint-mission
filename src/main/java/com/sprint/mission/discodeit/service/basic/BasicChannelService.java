package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.DuplicateException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;

  @Override
  public Channel create(PublicChannelCreateRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("request is null.");
    }

    if (!isUniqueName(request.name())) {
      throw new DuplicateException("name is duplicate.");
    }

    Channel channel = new Channel(request.name(), request.description(), ChannelType.PUBLIC);
    return channelRepository.save(channel);
  }

  @Override
  public Channel create(PrivateChannelCreateRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("request is null.");
    }

    for (UUID uuid : request.participantIds()) {
      if (!userRepository.existsById(uuid)) {
        throw new NotFoundException("user not found.");
      }
    }

    Channel channel = new Channel(null, null, ChannelType.PRIVATE);

    for (UUID uuid : request.participantIds()) {
      if (readStatusRepository.findByUserIdAndChannelId(uuid, channel.getId()).isEmpty()) {
        readStatusRepository.save(new ReadStatus(uuid, channel.getId(), Instant.now()));
      }
    }
    return channelRepository.save(channel);
  }

  @Override
  public ChannelDto findById(UUID channelId) {
    if (channelId == null) {
      throw new IllegalArgumentException("channelId is null.");
    }

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NotFoundException("channel not found."));

    return toChannelResponse(channel);
  }

  private Channel findEntityById(UUID channelId) {
    if (channelId == null) {
      throw new IllegalArgumentException("channelId is null.");
    }

    return channelRepository.findById(channelId)
        .orElseThrow(() -> new NotFoundException("channel not found."));
  }

  @Override
  public List<ChannelDto> findAll() {
    return channelRepository.findAll().stream()
        .filter(channel -> channel.getType() == ChannelType.PUBLIC)
        .map(this::toChannelResponse)
        .toList();
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("userId is null.");
    }

    List<UUID> mySubscribedChannelIds = readStatusRepository.findByUserId(userId).stream()
        .map(ReadStatus::getChannelId)
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel ->
            channel.getType().equals(ChannelType.PUBLIC)
                || mySubscribedChannelIds.contains(channel.getId())
        )
        .map(this::toChannelResponse)
        .toList();
  }

  @Override
  public Channel update(UUID channelId, ChannelUpdateRequest request) {
    if (channelId == null) {
      throw new IllegalArgumentException("channelId is null.");
    }
    if (request == null) {
      throw new IllegalArgumentException("channelUpdateRequest is null.");
    }

    Channel channel = findEntityById(channelId);

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalStateException("private channel cannot be updated.");
    }

    if (Objects.equals(channel.getName(), request.newName())) {
      throw new IllegalArgumentException("name is same.");
    }

    if (!isUniqueName(request.newName())) {
      throw new DuplicateException("name is duplicate.");
    }

    Channel tempChannel = Channel.copyOf(channel);
    tempChannel.update(request.newName(), request.newDescription());

    return channelRepository.save(tempChannel);
  }

  @Override
  public void delete(UUID channelId) {
    if (channelId == null) {
      throw new IllegalArgumentException("channelId is null.");
    }

    Channel channel = findEntityById(channelId);

    messageRepository.findByChannelId(channelId).forEach(c -> messageRepository.delete(c.getId()));
    readStatusRepository.findByChannelId(channelId)
        .forEach(r -> readStatusRepository.delete(r.getId()));
    channelRepository.delete(channelId);
  }

  private boolean isUniqueName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("name is null.");
    }

    return channelRepository.findAll().stream()
        .noneMatch(channel -> Objects.equals(channel.getName(), name));
  }

  private ChannelDto toChannelResponse(Channel channel) {
    List<Message> messageList = messageRepository.findByChannelId(channel.getId());

    Instant lastMessageAt = messageList.isEmpty()
        ? null
        : messageList.get(messageList.size() - 1).getUpdatedAt();

    List<UUID> userIdList = null;
    if (channel.getType() == ChannelType.PRIVATE) {
      userIdList = readStatusRepository.findByChannelId(channel.getId()).stream()
          .map(ReadStatus::getUserId)
          .toList();
    }

    return ChannelDto.from(channel, lastMessageAt, userIdList);
  }
}
