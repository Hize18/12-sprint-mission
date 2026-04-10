package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {
    private final ChannelRepository cr;
    private final UserRepository ur;

    @Override
    public boolean isUniqueName(String name) {
        if (name == null) throw new IllegalArgumentException("name is null.");

        return cr.findAll().stream()
                .noneMatch(channel -> Objects.equals(channel.getName(), name));
    }

    @Override
    public Channel save(Channel channel) {
        if (channel == null) throw new IllegalArgumentException("channel is null.");
        if (channel.getName() == null) throw new IllegalArgumentException("name is null.");
        if (channel.getOwnerId() == null) throw new IllegalArgumentException("ownerId is null.");

        if (channel.getName().isBlank()) throw new IllegalArgumentException("name is blank.");

        if (!isUniqueName(channel.getName())) throw new IllegalStateException("name is duplicate.");

        ur.findById(channel.getOwnerId()).orElseThrow(() -> new NoSuchElementException("owner not found."));

        return cr.save(channel);
    }

    @Override
    public Channel findById(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is null.");

        return cr.findById(id).orElseThrow(() -> new NoSuchElementException("channel not found."));
    }

    @Override
    public Channel findByName(String name) {
        if (name == null) throw new IllegalArgumentException("name is null.");

        return cr.findByName(name).orElseThrow(() -> new NoSuchElementException("channel not found."));
    }

    @Override
    public List<Channel> findByOwner(UUID ownerId) {
        if (ownerId == null) throw new IllegalArgumentException("ownerId is null.");

        return cr.findByOwnerId(ownerId);
    }

    @Override
    public List<Channel> findAll() {
        return cr.findAll();
    }

    @Override
    public boolean update(UUID userId, UUID channelId, String name) {
        if (userId == null) throw new IllegalArgumentException("userId is null.");
        if (channelId == null) throw new IllegalArgumentException("channelId is null.");
        if (name == null) throw new IllegalArgumentException("name is null.");

        Channel channel = findById(channelId);

        if (name.isBlank()) {
            throw new IllegalArgumentException("name is blank.");
        }

        if (Objects.equals(channel.getName(), name)) {
            throw new IllegalStateException("name is same.");
        }

        if (!isUniqueName(name)) {
            throw new IllegalStateException("name is duplicate.");
        }

        if (!Objects.equals(channel.getOwnerId(), userId)) return false;

        Channel tempChannel = Channel.copyOf(channel);

        tempChannel.update(name);
        cr.save(tempChannel);
        return true;
    }

    @Override
    public boolean delete(UUID userId, UUID channelId) {
        if (userId == null) throw new IllegalArgumentException("userId is null.");
        if (channelId == null) throw new IllegalArgumentException("channelId is null.");

        Channel channel = findById(channelId);

        if (!Objects.equals(channel.getOwnerId(), userId)) return false;

        cr.delete(channelId);
        return true;
    }
}
