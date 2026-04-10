package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileChannelService implements ChannelService {
    private final Map<UUID, Channel> data;
    private final UserService us;
    private final Path path = Path.of(System.getProperty("user.dir"), "data/channel.ser");

    public FileChannelService(UserService us) {
        Map<UUID, Channel> temp;
        try {
            Files.createDirectories(path.getParent());
            if (Files.exists(path)) {
                try (FileInputStream fis = new FileInputStream(path.toFile());
                     ObjectInputStream ois = new ObjectInputStream(fis)) {
                    temp = (Map<UUID, Channel>) ois.readObject();
                }
            } else temp = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            temp = new HashMap<>();
        }
        this.data = temp;
        this.us = us;
    }

    private void saveMap() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(data);
        } catch (Exception e) {
            throw new IllegalStateException("channel file save failed.", e);
        }
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
        saveMap();
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
        saveMap();
        return true;
    }

    @Override
    public boolean delete(UUID userId, UUID channelId) {
        if (userId == null) throw new IllegalArgumentException("userId is null.");
        if (channelId == null) throw new IllegalArgumentException("channelId is null.");

        Channel channel = findById(channelId);

        if (!channel.getOwnerId().equals(userId)) return false;

        data.remove(channelId);
        saveMap();
        return true;
    }
}
