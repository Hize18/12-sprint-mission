package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository cr;

    public BasicChannelService(ChannelRepository channelRepository) {cr = channelRepository;}

    @Override
    public boolean isUniqueHandle(String handle) {
        if(handle == null) throw new IllegalArgumentException("handle is null.");

        for (Channel value : cr.findAll()) {
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
        if (!isUniqueHandle(channel.getHandle())) throw new IllegalStateException("handle is duplicate.");

        return cr.save(channel);
    }

    @Override
    public Channel findById(UUID id) {
        if(id == null) throw new IllegalArgumentException("id is null.");

        return cr.findById(id);
    }

    @Override
    public Channel findByHandle(String handle) {
        if(handle == null) throw new IllegalArgumentException("handle is null.");

        return cr.findByHandle(handle);
    }

    @Override
    public List<Channel> findByOwner(UUID ownerId) {
        if(ownerId == null) throw new IllegalArgumentException("ownerId is null.");

        return cr.findByOwner(ownerId);
    }

    @Override
    public List<Channel> findByName(String name) {
        if(name == null) throw new IllegalArgumentException("name is null.");

        return cr.findByName(name);
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
        if (channel == null) throw new IllegalStateException("channel is null");
        if(channel.getName().equals(name) || name.isEmpty()) throw new IllegalStateException("name is same.");

        if(!channel.getOwner().getId().equals(userId)) return false;

        Channel tempChannel = new Channel(channel);

        tempChannel.update(name, createHandle(name));
        cr.save(tempChannel);
        return true;
    }

    @Override
    public boolean delete(UUID userId, UUID channelId) {
//        data.removeIf(c -> c.getId().equals(id));
        if (userId == null) throw new IllegalArgumentException("userId is null.");
        if (channelId == null) throw new IllegalArgumentException("channelId is null.");

        Channel channel = findById(channelId);
        if(channel == null) throw new IllegalStateException("channel not found.");

        if(!channel.getOwner().getId().equals(userId)) return false;

        cr.delete(channelId);
        return true;
    }
}
