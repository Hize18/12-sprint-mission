package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data;
    private final Path path = Path.of(System.getProperty("user.dir"),"data_repo/message.ser");

    public FileMessageRepository(){
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

    private void saveMap(Map<UUID, Message> map){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))){
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
    public Message findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        List<Message> list = new ArrayList<>();

        for (Message value : data.values()) {
            if(value.getChannel().getId().equals(channelId)) list.add(value);
        }
        list.sort(Comparator.comparing(Message::getUpdatedAt));
        return list;
    }

    @Override
    public List<Message> findByUserId(UUID userId) {
        List<Message> list = new ArrayList<>();

        for (Message value : data.values()) {
            if(value.getUser().getId().equals(userId)) list.add(value);
        }
        list.sort(Comparator.comparing(Message::getUpdatedAt));
        return list;
    }

    @Override
    public List<Message> findByKeyword(String keyword) {
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
