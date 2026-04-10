package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;
    private final UserService us;

    public JCFChannelService(UserService us) {
        this.data = new HashMap<>();
        this.us = us;
    }

    @Override
    public boolean isUniqueName(String name) {
        if (name == null) throw new IllegalArgumentException("name is null.");

        return data.values().stream()
                .noneMatch(channel -> Objects.equals(channel.getName(), name));
    }

    @Override
    public Channel save(Channel channel) {
        if (channel == null) throw new IllegalArgumentException("channel is null.");
        if (channel.getName() == null) throw new IllegalArgumentException("name is null.");
        if (channel.getOwnerId() == null) throw new IllegalArgumentException("ownerId is null.");
        us.findById(channel.getOwnerId());

        if (!isUniqueName(channel.getName())) throw new IllegalStateException("name is duplicate.");

        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is null.");

        return Optional.ofNullable(data.get(id))
                .orElseThrow(() -> new NoSuchElementException("channel not found."));
    }

    @Override
    public Channel findByName(String name) {
        if (name == null) throw new IllegalArgumentException("name is null.");

        return data.values().stream()
                .filter(channel -> Objects.equals(channel.getName(), name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("channel not found."));
    }

    @Override
    public List<Channel> findByOwner(UUID ownerId) {
        if (ownerId == null) throw new IllegalArgumentException("ownerId is null.");

        List<Channel> list = new ArrayList<>();

        for (Channel value : data.values()) {
            if (value.getOwnerId().equals(ownerId)) list.add(value);
        }
        list.sort(Comparator.comparing(Channel::getUpdatedAt));
        return list;
    }

    @Override
    public List<Channel> findAll() {
        List<Channel> list = new ArrayList<>(data.values());
        list.sort(Comparator.comparing(Channel::getUpdatedAt));
        return list;
    }

    @Override
    public boolean update(UUID userId, UUID channelId, String name) {
        if (userId == null) throw new IllegalArgumentException("userId is null.");
        if (channelId == null) throw new IllegalArgumentException("channelId is null.");
        if (name == null) throw new IllegalArgumentException("name is null.");

        Channel channel = findById(channelId);

        if (channel.getName().equals(name) || name.isEmpty()) throw new IllegalArgumentException("name is same.");

        if (!isUniqueName(name) && !channel.getName().equals(name)) {
            throw new IllegalStateException("name is duplicate.");
        }

        if (!channel.getOwnerId().equals(userId)) return false;

        channel.update(name.trim());
        return true;
    }

    @Override
    public boolean delete(UUID userId, UUID channelId) {
        if (userId == null) throw new IllegalArgumentException("userId is null.");
        if (channelId == null) throw new IllegalArgumentException("channelId is null.");

        Channel channel = findById(channelId);

        if (!channel.getOwnerId().equals(userId)) return false;

        data.remove(channelId);
        return true;
    }
}
