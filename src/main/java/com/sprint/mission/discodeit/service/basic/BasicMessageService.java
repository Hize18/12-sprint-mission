package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository mr;

    public BasicMessageService(MessageRepository messageRepository) {mr = messageRepository;}

    @Override
    public Message save(Message message) {
        if (message == null) throw new IllegalArgumentException("message is null.");
        if (message.getChannel() == null) throw new IllegalArgumentException("channel is null.");
        if (message.getUser() == null) throw new IllegalArgumentException("user is null.");

        return mr.save(message);
    }

    @Override
    public Message findById(UUID id) {
        if(id == null) throw new IllegalArgumentException("id is null.");

        return mr.findById(id);
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        if(channelId == null) throw new IllegalArgumentException("channelId is null.");

        return mr.findByChannelId(channelId);
    }

    @Override
    public List<Message> findByUserId(UUID userId) {
        if(userId == null) throw new IllegalArgumentException("userId is null.");

        return mr.findByUserId(userId);
    }

    @Override
    public List<Message> findByKeyword(String keyword) {
        if(keyword == null) throw new IllegalArgumentException("keyword is null.");

        return mr.findByKeyword(keyword);
    }

    @Override
    public List<Message> findAll() {
//        리스트 전체를 넘기는게 아니라 new로 넘겨줘야한다.
        return mr.findAll();
    }

    @Override
    public boolean update(UUID userId, UUID messageId, String content) {
        if (messageId == null) throw new IllegalArgumentException("messageId is null.");
        if (userId == null) throw new IllegalArgumentException("userId is null.");
        if (content == null) throw new IllegalArgumentException("content is null.");

        Message message = findById(messageId);
        if (message == null) throw new IllegalStateException("message not found.");

        if(!message.getUser().getId().equals(userId)) return false;

        Message tempMessage = new Message(message);

        tempMessage.update(content);
        mr.save(tempMessage);
        return true;
    }

    @Override
    public boolean delete(UUID userId, UUID messageId) {
        if (messageId == null) throw new IllegalArgumentException("messageId is null.");
        if (userId == null) throw new IllegalArgumentException("userId is null.");

        Message message = findById(messageId);
        if (message == null) throw new IllegalStateException("message not found.");

        if(!message.getUser().getId().equals(userId)) return false;

        mr.delete(messageId);
        return true;
    }
}
