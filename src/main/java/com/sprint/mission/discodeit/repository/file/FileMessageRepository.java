package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Repository
@ConditionalOnProperty(name ="12-sprint-mission.repository.type", havingValue = "file")
public class FileMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data;
    private final Path path = Path.of(System.getProperty("user.dir"), "data_repo/message.ser");

    public FileMessageRepository() {
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
    }

    private void saveMap(Map<UUID, Message> map) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(map);
        } catch (Exception e) {
            throw new IllegalStateException("message file save failed.", e);
        }
    }

    @Override
    public Message save(Message message) {
        Map<UUID, Message> temp = new HashMap<>(data);

        temp.put(message.getId(), message);
        saveMap(temp);
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        List<Message> list = new ArrayList<>();

        for (Message value : data.values()) {
            if (Objects.equals(value.getChannelId(), channelId)) list.add(value);
        }
        list.sort(Comparator.comparing(Message::getUpdatedAt));
        return list;
    }

    @Override
    public List<Message> findByUserId(UUID userId) {
        List<Message> list = new ArrayList<>();

        for (Message value : data.values()) {
            if (Objects.equals(value.getUserId(), userId)) list.add(value);
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
    public void delete(UUID id) {
        Map<UUID, Message> temp = new HashMap<>(data);

        temp.remove(id);
        saveMap(temp);
        data.remove(id);
    }
}
