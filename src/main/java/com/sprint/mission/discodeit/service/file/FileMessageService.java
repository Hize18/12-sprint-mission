package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileMessageService implements MessageService {
    private final Map<UUID, Message> data;
    private final ChannelService cs;
    private final UserService us;
    private final Path path = Path.of(System.getProperty("user.dir"), "data/message.ser");

    public FileMessageService(ChannelService cs, UserService us) {
        Map<UUID, Message> temp;
        try {
            Files.createDirectories(path.getParent());
            if (Files.exists(path)) {
                try (FileInputStream fis = new FileInputStream(path.toFile());
                     ObjectInputStream ois = new ObjectInputStream(fis)) {
                    temp = (Map<UUID, Message>) ois.readObject();
                }
            } else temp = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            temp = new HashMap<>();
        }
        this.data = temp;
        this.cs = cs;
        this.us = us;
    }

    private void saveMap() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(data);
        } catch (Exception e) {
            throw new IllegalStateException("message file save failed.", e);
        }
    }

    @Override
    public Message save(Message message) {
        if (message == null) throw new IllegalArgumentException("message is null.");
        if (message.getChannelId() == null) throw new IllegalArgumentException("channel is null.");
        cs.findById(message.getChannelId());
        if (message.getUserId() == null) throw new IllegalArgumentException("user is null.");
        us.findById(message.getUserId());

        data.put(message.getId(), message);
        saveMap();
        return message;
    }

    @Override
    public Message findById(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is null.");

        return Optional.ofNullable(data.get(id))
                .orElseThrow(() -> new NoSuchElementException("message not found."));
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        if (channelId == null) throw new IllegalArgumentException("channelId is null.");

        List<Message> list = new ArrayList<>();

        for (Message value : data.values()) {
            if (value.getChannelId().equals(channelId)) list.add(value);
        }
        list.sort(Comparator.comparing(Message::getUpdatedAt));
        return list;
    }

    @Override
    public List<Message> findByUserId(UUID userId) {
        if (userId == null) throw new IllegalArgumentException("userId is null.");

        List<Message> list = new ArrayList<>();

        for (Message value : data.values()) {
            if (value.getUserId().equals(userId)) list.add(value);
        }
        list.sort(Comparator.comparing(Message::getUpdatedAt));
        return list;
    }

    @Override
    public List<Message> findAll() {
        List<Message> list = new ArrayList<>(data.values());
        list.sort(Comparator.comparing(Message::getUpdatedAt));
        return list;
    }

    @Override
    public boolean update(UUID userId, UUID messageId, String content) {
        if (messageId == null) throw new IllegalArgumentException("messageId is null.");
        if (userId == null) throw new IllegalArgumentException("userId is null.");
        if (content == null) throw new IllegalArgumentException("content is null.");

        Message message = findById(messageId);

        if (!message.getUserId().equals(userId)) return false;

        message.update(content);
        saveMap();
        return true;
    }

    @Override
    public boolean delete(UUID userId, UUID messageId) {
        if (messageId == null) throw new IllegalArgumentException("messageId is null.");
        if (userId == null) throw new IllegalArgumentException("userId is null.");

        Message message = findById(messageId);

        if (!message.getUserId().equals(userId)) return false;

        data.remove(messageId);
        saveMap();
        return true;
    }
}
