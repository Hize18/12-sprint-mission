package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data;

    public JCFMessageRepository() {data = new HashMap<>();}

    @Override
    public Message save(Message message) {
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
        data.remove(id);
    }
}
