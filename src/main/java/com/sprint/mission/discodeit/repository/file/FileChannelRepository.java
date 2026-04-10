package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Repository
@ConditionalOnProperty(name ="12-sprint-mission.repository.type", havingValue = "file")
public class FileChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data;
    private final Path path = Path.of(System.getProperty("user.dir"), "data_repo/channel.ser");

    public FileChannelRepository() {
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
    }

    private void saveMap(Map<UUID, Channel> map) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(map);
        } catch (Exception e) {
            throw new IllegalStateException("channel file save failed.", e);
        }
    }

    @Override
    public Channel save(Channel channel) {
        Map<UUID, Channel> temp = new HashMap<>(data);

        temp.put(channel.getId(), channel);
        saveMap(temp);
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<Channel> findByName(String name) {
        return data.values().stream()
                .filter(channel -> Objects.equals(channel.getName(), name))
                .findFirst();
    }

    @Override
    public List<Channel> findByOwnerId(UUID ownerId) {
        List<Channel> list = new ArrayList<>();

        for (Channel value : data.values()) {
            if (Objects.equals(value.getOwnerId(), ownerId)) list.add(value);
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
        Map<UUID, Channel> temp = new HashMap<>(data);

        temp.remove(id);
        saveMap(temp);
        data.remove(id);
    }
}
