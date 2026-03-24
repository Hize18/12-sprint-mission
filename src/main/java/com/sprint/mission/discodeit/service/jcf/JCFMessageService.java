package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;

    public JCFMessageService() {data = new HashMap<>();}

    @Override
    public Message save(Message message) {
        if (message == null) throw new IllegalArgumentException("message is null.");
        if (message.getChannel() == null) throw new IllegalArgumentException("channel is null.");
        if (message.getUser() == null) throw new IllegalArgumentException("user is null.");

        data.put(message.getId(), message);
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
    public boolean update(UUID messageId, UUID userId, String content) {
        if (messageId == null) throw new IllegalArgumentException("messageId is null.");
        if (userId == null) throw new IllegalArgumentException("userId is null.");
        if (content == null) throw new IllegalArgumentException("content is null.");

        Message message = findById(messageId);
        if (message == null) throw new IllegalArgumentException("message not found.");

        if(!message.getUser().getId().equals(userId)) return false;

        message.update(content);
        return true;
    }

    @Override
    public boolean delete(UUID messageId, UUID userId) {
        if (messageId == null) throw new IllegalArgumentException("messageId is null.");
        if (userId == null) throw new IllegalArgumentException("userId is null.");

        Message message = findById(messageId);
        if (message == null) throw new IllegalArgumentException("message not found.");

        if(!message.getUser().getId().equals(userId)) return false;

        data.remove(messageId);
        return true;
    }
}
