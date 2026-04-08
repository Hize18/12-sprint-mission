package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileMessageService implements MessageService {
    private final Map<UUID, Message> data;
    private final Path path = Path.of(System.getProperty("user.dir"),"data/message.ser");

    public FileMessageService(){
        Map<UUID, Message> temp;
        try {
            Files.createDirectories(path.getParent());

            if(Files.exists(path))
            {
                try(FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)) {
                    temp = (Map<UUID, Message>) ois.readObject();
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
    public Message save(Message message) {
        if (message == null) throw new IllegalArgumentException("message is null.");
        if (message.getChannel() == null) throw new IllegalArgumentException("channel is null.");
        if (message.getUser() == null) throw new IllegalArgumentException("user is null.");

        data.put(message.getId(), message);
        saveMap();
        return message;
    }

    @Override
    public Message findById(UUID id) {
        if(id == null) throw new RuntimeException("id is null.");

        return data.get(id);
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        if(channelId == null) throw new RuntimeException("channelId is null.");

        List<Message> list = new ArrayList<>();
        for (Message value : data.values()) {
            if(value.getChannel().getId().equals(channelId)) list.add(value);
        }
        list.sort(Comparator.comparing(Message::getUpdatedAt));
        return list;
    }

    @Override
    public List<Message> findByUserId(UUID userId) {
        if(userId == null) throw new RuntimeException("userId is null.");

        List<Message> list = new ArrayList<>();
        for (Message value : data.values()) {
            if(value.getUser().getId().equals(userId)) list.add(value);
        }
        list.sort(Comparator.comparing(Message::getUpdatedAt));
        return list;
    }

    @Override
    public List<Message> findByKeyword(String keyword) {
        if(keyword == null) throw new RuntimeException("keyword is null.");

        List<Message> list = new ArrayList<>();
        for (Message value : data.values()) {
            if(value.getUser().getNickname().contains(keyword) ||
                    value.getChannel().getName().contains(keyword) ||
                    value.getContent().contains(keyword)) list.add(value);
        }
        list.sort(Comparator.comparing(Message::getUpdatedAt));
        return list;
    }

    @Override
    public List<Message> findAll() {
//        리스트 전체를 넘기는게 아니라 new로 넘겨줘야한다.
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
        if (message == null) throw new IllegalArgumentException("message not found.");

        if(!message.getUser().getId().equals(userId)) return false;

        message.update(content);
        saveMap();
        return true;
    }

    @Override
    public boolean delete(UUID userId, UUID messageId) {
        if (messageId == null) throw new IllegalArgumentException("messageId is null.");
        if (userId == null) throw new IllegalArgumentException("userId is null.");

        Message message = findById(messageId);
        if (message == null) throw new IllegalArgumentException("message not found.");

        if(!message.getUser().getId().equals(userId)) return false;

        data.remove(messageId);
        saveMap();
        return true;
    }
}
