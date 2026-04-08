package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;

    public JCFChannelService() {this.data = new HashMap<>();}

    @Override
    public boolean isUniqueHandle(String handle) {
        if(handle == null) throw new IllegalArgumentException("handle is null.");

        for (Channel value : data.values()) {
            if(value.getHandle().equals(handle)) return false;
        }
        return true;
    }

    @Override
    public String createHandle(String name) {
        if(name == null) throw new IllegalArgumentException("name is null.");

        String handle;
        Random rand = new Random();
        int max_attempt = 0;
        while(max_attempt<100){
            int num = rand.nextInt(10000);
            handle = name + String.format("%04d",num);
            if(isUniqueHandle(handle)) return handle;
            max_attempt++;
        }
        throw new RuntimeException("handle 생성 실패.");
    }

    @Override
    public Channel save(Channel channel) {
        if (channel == null) throw new IllegalArgumentException("channel is null.");
        if (channel.getHandle() == null) throw new IllegalArgumentException("handle is null.");
        if (channel.getOwner() == null) throw new IllegalArgumentException("owner is null.");
        if (!isUniqueHandle(channel.getHandle())) throw new IllegalArgumentException("handle is duplicate.");

        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        if(id == null) throw new RuntimeException("id is null.");

        return data.get(id);
    }

    @Override
    public Channel findByHandle(String handle) {
        if(handle == null) throw new RuntimeException("handle is null.");

        for (Channel value : data.values()) {
            if(value.getHandle().equals(handle)) return value;
        }
        return null;
    }

    @Override
    public List<Channel> findByOwner(UUID ownerId) {
        if(ownerId == null) throw new RuntimeException("ownerId is null.");

        List<Channel> list = new ArrayList<>();
        for (Channel value : data.values()) {
            if(value.getOwner().getId().equals(ownerId)) list.add(value);
        }
        list.sort(Comparator.comparing(Channel::getUpdatedAt));
        return list;
    }

    @Override
    public List<Channel> findByName(String name) {
        if(name == null) throw new RuntimeException("name is null.");

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
    public boolean update(UUID userId, UUID channelId, String name) {
        if (userId == null) throw new IllegalArgumentException("userId is null.");
        if (channelId == null) throw new IllegalArgumentException("channelId is null.");
        if (name == null) throw new IllegalArgumentException("name is null.");

        Channel channel = findById(channelId);
        if (channel == null) throw new IllegalArgumentException("channel is null");
        if(channel.getName().equals(name) || name.isEmpty()) throw new IllegalArgumentException("name is same.");

        if(!channel.getOwner().getId().equals(userId)) return false;

        channel.update(name.trim(), createHandle(name.trim()));
        return true;
    }

    @Override
    public boolean delete(UUID userId, UUID channelId) {
//        data.removeIf(c -> c.getId().equals(id));
        if (userId == null) throw new IllegalArgumentException("userId is null.");
        if (channelId == null) throw new IllegalArgumentException("channelId is null.");

        Channel channel = findById(channelId);
        if(channel == null) throw new IllegalArgumentException("channel not found.");

        if(!channel.getOwner().getId().equals(userId)) return false;

        data.remove(channelId);
        return true;
    }
}
