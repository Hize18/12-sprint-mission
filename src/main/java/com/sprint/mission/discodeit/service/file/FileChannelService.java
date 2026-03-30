package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileChannelService implements ChannelService {
    private final Map<UUID, Channel> data;
    private final Path path = Path.of(System.getProperty("user.dir"),"data/channel.ser");

    public FileChannelService() {
        Map<UUID, Channel> temp;
        try {
            Files.createDirectories(path.getParent());

            if(Files.exists(path))
            {
                try(FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)) {
                    temp = (Map<UUID, Channel>) ois.readObject();
                }
            }
            else temp = new HashMap<>();
        } catch (Exception e) {
            e.printStackTrace();
            temp = new HashMap<>();
        }
        this.data = temp;
    }

    private void saveMap(){
        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        saveMap();
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
        saveMap();
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
        saveMap();
        return true;
    }
}
