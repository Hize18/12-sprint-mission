package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ChannelResponse createPublicChannel(PublicChannelCreateRequest request) {
        if (request == null) throw new IllegalArgumentException("request is null.");

        return create(new ChannelCreateRequest(ChannelType.PUBLIC, request.ownerId(), request.name()));
    }

    @Override
    public ChannelResponse createPrivateChannel(PrivateChannelCreateRequest request) {
        if (request == null) throw new IllegalArgumentException("request is null.");

        if(!userRepository.existsById(request.ownerId())) throw new NoSuchElementException("owner not found.");

        for (UUID uuid : request.participantIds()) {
            if (!userRepository.existsById(uuid)) throw new NoSuchElementException("user not found.");
        }

        ChannelResponse response = create(new ChannelCreateRequest(
                ChannelType.PRIVATE,
                request.ownerId(),
                null
        ));

        readStatusRepository.save(new ReadStatus(request.ownerId(), response.id()));
        for (UUID uuid : request.participantIds()) {
            if (readStatusRepository.findByUserIdAndChannelId(uuid, response.id()).isEmpty()) {
                readStatusRepository.save(new ReadStatus(uuid, response.id()));
            }
        }
        return response;
    }

    private ChannelResponse create(ChannelCreateRequest request) {
        if (request == null) throw new IllegalArgumentException("channelCreateRequest is null.");

        if(!userRepository.existsById(request.ownerId())) throw new NoSuchElementException("owner not found.");


        if (request.channelType() == ChannelType.PUBLIC) {
            if (!isUniqueName(request.name())) {
                throw new IllegalStateException("name is duplicate.");
            }
        }

        Channel channel = new Channel(
                request.ownerId(),
                request.name(),
                request.channelType()
        );
        channelRepository.save(channel);

        return ChannelResponse.from(channel);
    }

    @Override
    public ChannelResponse findById(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is null.");

        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("channel not found."));

        return toChannelResponse(channel);
    }

    private Channel findEntityById(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is null.");

        return channelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("channel not found."));
    }

    @Override
    public ChannelResponse findByName(String name) {
        if (name == null) throw new IllegalArgumentException("name is null.");

        Channel channel = channelRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("channel not found."));

        return toChannelResponse(channel);
    }

    @Override
    public List<ChannelResponse> findAll() {
        return channelRepository.findAll().stream()
                .filter(channel -> channel.getChannelType() == ChannelType.PUBLIC)
                .map(this::toChannelResponse)
                .toList();
    }

    @Override
    public List<ChannelResponse> findAllByUserId(UUID userId) {
        if (userId == null) throw new IllegalArgumentException("userId is null.");

        List<Channel> publicChannels = channelRepository.findAll().stream()
                .filter(c -> c.getChannelType() == ChannelType.PUBLIC)
                .toList();

        List<Channel> privateChannels = readStatusRepository.findByUserId(userId).stream()
                .map(ReadStatus::getChannelId)
                .map(this::findEntityById)
                .toList();

        List<Channel> resultList = new ArrayList<>(publicChannels);
        resultList.addAll(privateChannels);

        return resultList.stream()
                .map(this::toChannelResponse)
                .toList();
    }

    @Override
    public void update(UUID userId, UUID channelId, ChannelUpdateRequest request) {
        if (userId == null) throw new IllegalArgumentException("userId is null.");
        if (channelId == null) throw new IllegalArgumentException("channelId is null.");
        if (request == null) throw new IllegalArgumentException("channelUpdateRequest is null.");

        Channel channel = findEntityById(channelId);

        if (!Objects.equals(channel.getOwnerId(), userId)) throw new IllegalStateException("no permission");
        if (channel.getChannelType() == ChannelType.PRIVATE) {
            throw new IllegalStateException("private channel cannot be updated.");
        }

        if (Objects.equals(channel.getName(), request.name())) {
            throw new IllegalStateException("name is same.");
        }

        if (!isUniqueName(request.name())) {
            throw new IllegalStateException("name is duplicate.");
        }

        Channel tempChannel = Channel.copyOf(channel);
        tempChannel.update(request.name());
        channelRepository.save(tempChannel);
    }

    @Override
    public void delete(UUID userId, UUID channelId) {
        if (userId == null) throw new IllegalArgumentException("userId is null.");
        if (channelId == null) throw new IllegalArgumentException("channelId is null.");

        Channel channel = findEntityById(channelId);

        if (!Objects.equals(channel.getOwnerId(), userId)) throw new IllegalStateException("no permission");

        messageRepository.findByChannelId(channelId).forEach(c -> messageRepository.delete(c.getId()));
        readStatusRepository.findByChannelId(channelId).forEach(r -> readStatusRepository.delete(r.getId()));
        channelRepository.delete(channelId);
    }

    private boolean isUniqueName(String name) {
        if (name == null) throw new IllegalArgumentException("name is null.");

        return channelRepository.findAll().stream()
                .noneMatch(channel -> Objects.equals(channel.getName(), name));
    }

    private ChannelResponse toChannelResponse(Channel channel) {
        List<Message> messageList = messageRepository.findByChannelId(channel.getId());

        Instant lastMessageAt = messageList.isEmpty()
                ? null
                : messageList.get(messageList.size() - 1).getUpdatedAt();

        List<UUID> userIdList = null;
        if (channel.getChannelType() == ChannelType.PRIVATE) {
            userIdList = readStatusRepository.findByChannelId(channel.getId()).stream()
                    .map(ReadStatus::getUserId)
                    .toList();
        }

        return ChannelResponse.from(channel, lastMessageAt, userIdList);
    }
}
