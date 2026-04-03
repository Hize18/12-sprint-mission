package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data;

    public JCFChannelRepository() {this.data = new HashMap<>();}

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        return data.get(id);
    }

    @Override
    public Channel findByHandle(String handle) {
        for (Channel value : data.values()) {
            if(value.getHandle().equals(handle)) return value;
        }
        return null;
    }

    @Override
    public List<Channel> findByOwner(UUID ownerId) {
        List<Channel> list = new ArrayList<>();
        for (Channel value : data.values()) {
            if(value.getOwner().getId().equals(ownerId)) list.add(value);
        }
        list.sort(Comparator.comparing(Channel::getUpdatedAt));
        return list;
    }

    @Override
    public List<Channel> findByName(String name) {
        List<Channel> list = new ArrayList<>();
        for (Channel value : data.values()) {
            if(value.getName().equals(name)) list.add(value);
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
    public void delete(UUID id) {
        data.remove(id);
    }
}
